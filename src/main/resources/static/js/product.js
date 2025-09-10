const PRODUCTS_API_URL = "http://localhost:8080/api/v1/products";

function isLoggedIn() {
  return Boolean(sessionStorage.getItem("user"));
}

function isAdminLoggedIn() {
  const user = JSON.parse(sessionStorage.getItem("user") || "{}");
  return isLoggedIn() && user.role === "ADMIN";
}

window.addEventListener("load", () => {
  const urlParams = new URLSearchParams(window.location.search);
  const productId = urlParams.get("id");

  if (productId) fetchProduct(productId);
});

async function fetchProduct(productId) {
  try {
    const response = await fetch(`${PRODUCTS_API_URL}/${productId}`);
    if (!response.ok) throw new Error(`Error fetching product with ID: ${productId}`);

    const product = await response.json();
    displayProduct(product);
  } catch (error) {
    console.error(error);
    alert("Error fetching product details. Please try again later.");
  }
}

function displayProduct(product) {
  const productContainer = document.getElementById("productDetailsContainer");
  productContainer.innerHTML = `
      <div class="product-detail-card">
        <img src="${product.image}" alt="${product.title}" />
        <h2>${product.title}</h2>
        <p class="price">$${product.price.toFixed(2)}</p>
        <p><strong>Description:</strong> ${product.description}</p>
        <p><strong>Category:</strong> ${product.category}</p>
        <p class="rating"><strong>Rating:</strong> ${product.rating.rate} (Count: ${product.rating.count})</p>
        <div class="product-card-buttons-container" id="product-card-buttons">
          ${isAdminLoggedIn() ? `
            <button class="product-card-button edit-product-button" onclick="editProduct(${product.id})">✏️ Edit</button>
            <button class="product-card-button delete-product-button" onclick="deleteProduct(${product.id})">🗑️ Delete</button>
          ` : ''}
        </div>
      </div>
    `;
}

function editProduct(productId) {
  window.location.href = `edit.html?id=${productId}`;
}

async function deleteProduct(productId) {
  if (!confirm(`Are you sure you want to delete the product with ID: ${productId}?`)) return;

  try {
    const response = await fetch(`${PRODUCTS_API_URL}/${productId}`, { method: "DELETE" });
    if (!response.ok) throw new Error(`Error deleting product with ID: ${productId}`);

    alert("Product deleted successfully.");
    window.location.href = "index.html";
  } catch (error) {
    console.error(error);
    alert("Error deleting product. Please try again later.");
  }
}
