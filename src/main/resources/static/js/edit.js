const PRODUCTS_API_URL = "http://localhost:8080/api/v1/products";
const productId = new URLSearchParams(window.location.search).get("id");

window.addEventListener("load", () => {
    // Display the username in the navbar
    const user = JSON.parse(sessionStorage.getItem("user"));
    document.getElementById("username-display").textContent = user.username;

    if (productId) fetchProductForEdit(productId);

    document.getElementById("editProductForm").addEventListener("submit", event => {
        event.preventDefault();
        updateProduct(productId);
    });
});

async function fetchProductForEdit(productId) {
    try {
        const response = await fetch(`${PRODUCTS_API_URL}/${productId}`);
        if (!response.ok) throw new Error(`Error fetching product with ID: ${productId}`);

        const product = await response.json();
        preFillFormData(product);
    } catch (error) {
        alert("Error fetching product from API");
    }
}

function preFillFormData(product) {
    document.getElementById("title").value = product.title;
    document.getElementById("price").value = product.price;
    document.getElementById("description").value = product.description;
    document.getElementById("productCategory").value = product.category;
    console.log(product.category);
    document.getElementById("imageURL").value = product.image;
    document.getElementById("rating").value = product.rating.rate;
    document.getElementById("ratingCount").value = product.rating.count;
}

async function updateProduct(productId) {
    const updatedProduct = {
        title: document.getElementById("title").value,
        price: parseFloat(document.getElementById("price").value),
        description: document.getElementById("description").value,
        category: document.getElementById("productCategory").value,
        image: document.getElementById("imageURL").value,
        rating: {
            rate: parseFloat(document.getElementById("rating").value),
            count: parseInt(document.getElementById("ratingCount").value)
        }
    };

    try {
        const response = await fetch(`${PRODUCTS_API_URL}/${productId}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(updatedProduct)
        });
        if (!response.ok) throw new Error("Error updating product");

        alert("Product updated successfully");
        window.location.href = `product.html?id=${productId}`;
    } catch (error) {
        alert("Error updating product in API");
    }
}
