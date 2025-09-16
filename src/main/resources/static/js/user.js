const UPDATE_API_URL = "http://localhost:8080/user/update";

// Check if the user is logged in and show "Add Product" button if the user is an admin
function showAddProductButton() {
    const navButton = document.getElementById("add-product-button");
    if (navButton) {
        navButton.style.display = isAdminLoggedIn() ? "inline" : "none";
    }
}

// Toggle password visibility
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    if(inputId == "current-password"){
    input.type = input.type === "password" ? "text" : "password";
    // Toggle the eye icon
    const eyeIcon = document.getElementById("eye-icon-pass-img");
    eyeIcon.src = input.type === "password" ? "images/opened-eye.png" : "images/closed-eye.png";
    }
    else if(inputId == "new-password"){
        input.type = input.type === "password" ? "text" : "password";
        // Toggle the eye icon
        const eyeIcon = document.getElementById("eye-icon-confirm-pass-img");
        eyeIcon.src = input.type === "password" ? "images/opened-eye.png" : "images/closed-eye.png";
    }
}

async function updateUser(event) {
    // Use event.preventDefault() to prevent the default action from being executed
    event.preventDefault();

    // Get the username and password from session storage
    const user = JSON.parse(sessionStorage.getItem("user"));
    const userID = user.userID;
    const username = user.username;
    var password = user.password;

    // Get the current password and new password from the input fields
    var currentPassword = document.getElementById("current-password").value;
    var newPassword = document.getElementById("new-password").value;

    // Make sure current password and new password are not empty
    if(!currentPassword || !newPassword){
        if(!currentPassword){
        // error message element
            const currPassErrorMessageElement = document.getElementById("error-message-curr-pass");
            passErrorMessageElement.style.display = "inline";
            // Change input border color to red
            const passInputElement = document.getElementById("current-password");
            passInputElement.style.borderColor = "red";}
        if(!newPassword){
            const newPassErrorMessageElement = document.getElementById("error-message-new-pass");
            newPassErrorMessageElement.style.display = "inline";
            const confirmInputElement = document.getElementById("new-password");
            confirmInputElement.style.borderColor = "red";
        }
        return;
    }

    console.log("Current Password:", currentPassword);
    console.log("Password in Session Storage:", password);
    // Check if the current password matches the password in session storage
    if(currentPassword !== password){
        // Display error message
        const errorMessageElement = document.getElementById("error-message");
        errorMessageElement.textContent = "Current password is incorrect.";
        const errorMessageContainer = document.getElementById("error-message-container");
        errorMessageContainer.style.display = "block";
        // Change input border color to red
        const passInputElement = document.getElementById("current-password");
        passInputElement.style.borderColor = "red";
        return;
    }
    password = newPassword;
    try {
        // Create a POST request to UPDATE_API_URL to send the user data { username, password, role: "USER" }
        const response = await fetch(UPDATE_API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json"},
            body: JSON.stringify({
                id: userID,
                username,
                password
            })
        })
        if(!response.ok){
            throw new Error("Update failed!");
        }
        // If update is successful, display a success alert and redirect the user to the login.html page
        alert("Update successful");
        // Update the password in session storage
        sessionStorage.setItem("user", JSON.stringify({userID, username, password, role: user.role}))
        window.location.href = "user.html";

    } catch (error) {
        alert(error.message);

    }
}

// Initialize the cart page
window.addEventListener("load", () => {
    // Display the username in the navbar
    const user = JSON.parse(sessionStorage.getItem("user"));
    // document.getElementById("username-display").textContent = user.username;
    const usernameDisplays = document.getElementsByClassName("username-display");
    Array.from(usernameDisplays).forEach(element => {
        element.textContent = user.username;
    });
    // Show "Add Product" button if the user is an admin
    showAddProductButton();
});