const CART_API_URL = "http://localhost:8080/api/v1/cart";
const PRODUCTS_API_URL = "http://localhost:8080/api/v1/products";

// Check if the user is logged in and show "Add Product" button if the user is an admin
function showAddProductButton() {
    const navButton = document.getElementById("add-product-button");
    if (navButton) {
        navButton.style.display = isAdminLoggedIn() ? "inline" : "none";
    }
}

// Fetch cart items for the logged-in user
async function fetchCartItems() {
    const user = JSON.parse(sessionStorage.getItem("user"));
    const userId = user ? user.userID : null;

    if (!userId) {
        // alert("User not logged in");
        // window.location.href = "login.html";
        return;
    }

    try {
        const response = await fetch(`${CART_API_URL}/user/${userId}`);
        if (!response.ok) throw new Error("Failed to fetch cart items");

        const cartItems = await response.json();
        const cartItemsWithDetails = await Promise.all(
            cartItems.map(async item => {
                const product = await fetchProductDetails(item.productId);
                return { ...item, product };
            })
        );

        renderCartItems(cartItemsWithDetails);
    } catch (error) {
        console.error("Error fetching cart items:", error);
        alert("Error fetching cart items. Please try again later.");
    }
}

// Fetch product details by productId
async function fetchProductDetails(productId) {
    try {
        const response = await fetch(`${PRODUCTS_API_URL}/${productId}`);
        if (!response.ok) throw new Error(`Failed to fetch product with ID: ${productId}`);
        return await response.json();
    } catch (error) {
        console.error("Error fetching product details:", error);
        return null; // In case of an error, return null so we can handle it in the cart display
    }
}

// Render cart items in the cart HTML with a "Remove" button for each item
function renderCartItems(cartItems) {
    const cartItemsContainer = document.getElementById("cartItems");
    const totalPriceElement = document.getElementById("totalPrice");

    // if cart is empty show a message
    if (cartItems.length === 0) {
        cartItemsContainer.innerHTML = "<h2 style='text-align: center;'>Your cart is empty.</h2>";
        return;
    }

    let total = 0;
    cartItemsContainer.innerHTML = cartItems.map(item => {
        if (!item.product) return ''; // If product details are missing, skip this item

        const subtotal = item.product.price;
        total += subtotal;

        return `
            <div class="cart-item">
                <img src="${item.product.image}" alt="${item.product.title}" class="cart-item-image">
                <div class="cart-item-details">
                    <h3>${item.product.title}</h3>
                    <p>Price: $${item.product.price.toFixed(2)}</p>
                    <button onclick="removeFromCart(${item.product.id})" class="remove-from-cart-button">Remove</button>
                </div>
            </div>
        `;
    }).join("");

    totalPriceElement.textContent = `Total: $${total.toFixed(2)}`;
}

async function removeFromCart(productId) {
    // In removeFromCart, retrieve the userID from sessionStorage using JSON.parse(sessionStorage.getItem("user"))
    const user = JSON.parse(sessionStorage.getItem("user"));
    const userId = user.userID;

    if (!userId) {
        alert("User not logged in");
        return;
    }

    try {
        // Send a DELETE request to ${CART_API_URL}/user/${userId}/product/${productId} to remove the item
        const response = await fetch(`${CART_API_URL}/user/${userId}/product/${productId}`, { method: "DELETE"})

        // Display a success message when the item is removed and refresh the cart items by calling fetchCartItems()
        if(response.ok){
            alert("Product successfully removed from cart");
            fetchCartItems();
        } else{
            throw new Error("Failed to remove item from cart");
        }
        



    } catch (error) {
        console.error("Error removing item from cart:", error);
        alert("Error removing item from cart. Please try again later.");
    }
}

// Initialize the cart page
window.addEventListener("load", () => {
    // Show "Add Product" button if the user is an admin
    showAddProductButton();
    // Fetch and display cart items
    fetchCartItems();
});
