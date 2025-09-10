const PRODUCTS_API_URL = "http://localhost:8080/api/v1/products";

async function addProduct(event) {
    event.preventDefault();

    const product = {
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
        const response = await fetch(PRODUCTS_API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(product)
        });
        if (!response.ok) throw new Error("Error adding product");

        alert("Product added successfully");
        window.location.href = "index.html";
    } catch (error) {
        alert("Error adding product to API");
    }
}

window.addEventListener("load", () => {
    document.getElementById("addProductForm").addEventListener("submit", addProduct);
});
