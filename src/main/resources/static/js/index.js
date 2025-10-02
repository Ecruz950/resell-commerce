const PRODUCTS_API_URL = "http://localhost:8080/api/v1/products";
const CART_API_URL = "http://localhost:8080/api/v1/cart";
let products = [];
let filteredProducts = []; // Add this to store filtered products

async function addToCart(productId) {
  // Check if user is logged in and get user ID from Spring Security
  let userId;
  try {
    const authResponse = await fetch('/api/auth/status');
    const authData = await authResponse.json();
    
    if (!authData.authenticated) {
      alert('Please log in to add items to cart');
      window.location.href = '/login';
      return;
    }
    
    userId = authData.userId;
  } catch (error) {
    alert('Please log in to add items to cart');
    window.location.href = '/login';
    return;
  }

  const cart = {
    userId: userId,
    productId: productId
  }

  // Send a POST request to CART_API_URL to add the item to the cart
  try {
    const response = await fetch(CART_API_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json"},
      body: JSON.stringify(cart)
    })
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to add item to cart: ${errorText}`);
    }

    alert("Product added to cart!");
  } catch (error) {
    // Display an error alert using ${error.message} and log it to console
    alert(`Error Alert: ${error.message}`);

  }
}

window.addEventListener("load", () => {
  // Fetch products from the API when the page loads
  fetchProducts();

  // Add an event listener to the searchForm to handle form submissions. Prevent the default submission behavior
  document.getElementById("searchForm").addEventListener("submit", (event) => {
    event.preventDefault();
    document.getElementById("error-message-container").style.display = "none"; // Hide error message container on new search

    // Retrieve the search query from the searchInput field, and convert it to lowercase for a case-insensitive search
    const query = document.getElementById("searchInput").value.trim().toLowerCase();

    // Filter products to include only items where the title or category matches the query
    if (query) {
      filteredProducts = products.filter(product => 
        product.title.toLowerCase().includes(query) ||
        product.category.toLowerCase().includes(query)
      );
      if (filteredProducts.length === 0) {
        // If no products match the search query, display error message html element
        document.getElementById("error-message-container").style.display = "block";
        document.getElementById("error-message").textContent = "No products found matching your search.";
      }
    } else {
      // If query is empty, show all products
      filteredProducts = products;
    }

    // Pass the filtered products list to renderProducts() to display the search results
    renderProducts(filteredProducts);

  });
});

async function fetchProducts() {
  try {
    const response = await fetch(PRODUCTS_API_URL);
    if (!response.ok) throw new Error("Error fetching products from API");
    products = await response.json();
    filteredProducts = products; // Initialize filteredProducts with all products
    renderProducts(products);
  } catch (error) {
    alert(error.message);
  }
}

// Modify renderProducts to accept an argument (defaulting to products)
function renderProducts(productList = products) {
  const container = document.getElementById("productContainer");
  container.innerHTML = productList.map(product => `
      <div class="product-card">
        <a href="/product?id=${product.id}">
          <img src="${product.image}" alt="${product.title}" />
          <p>${product.title}</p>
        </a>
        <p>$${product.price.toFixed(2)} | Rating: ${product.rating.rate}</p>
        <div class="product-card-buttons-container">
          <button onclick="addToCart(${product.id})" class="add-to-cart-button"> Add to Cart</button>
        </div>
      </div>
    `).join('');
}
