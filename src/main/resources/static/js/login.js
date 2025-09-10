const LOGIN_API_URL = "http://localhost:8080/user/login";

async function login(event) {
    // Prevent the default form submission behavior inside the login() function
    event.preventDefault();

    // Retrieve the username and password entered by the user
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;


    // Check if username and password are provided
    if (!username || !password) {
        if (!username) {
            // alert("Please enter both username and password.");
            // error message element
            const userErrorMessageElement = document.getElementById("error-message-user");
            userErrorMessageElement.style.display = "inline";
            // Change input border color to red
            const userInputElement = document.getElementById("username");
            userInputElement.style.borderColor = "red";
        }
        if (!password) {
            // alert("Please enter both username and password.");
            // error message element
            const passErrorMessageElement = document.getElementById("error-message-pass");
            passErrorMessageElement.style.display = "inline";
            // Change input border color to red
            const passInputElement = document.getElementById("password");
            passInputElement.style.borderColor = "red";
        }
        return;
    }

    try {
        // Send a POST request to LOGIN_API_URL with the login credentials
        const response = await fetch(LOGIN_API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json"},
            body: JSON.stringify({
                username,
                password
            })
        })

        // If the response is successful, store the user ID and role in sessionStorage { userID, username, role } and redirect to index.html.
        //         Show an error message if log in fails
        if(!response.ok){
            // Display error message on the page
            const loginErrorMessageElement = document.getElementById("error-invalid-container");
            loginErrorMessageElement.style.display = "block";
            // throw new Error("Login Failed");
            return;
        }
        const responseData = await response.json();
        // console.log("Login successful:", responseData);
        // Store userID, username, and role in sessionStorage
        sessionStorage.setItem("user", JSON.stringify({userID: responseData.userID, username, role: responseData.role}))
        window.location.href = "index.html";
    } catch (error) {
        alert(error.message);
    }
}

function isLoggedIn() {
    return Boolean(sessionStorage.getItem("user"));
}

function isAdminLoggedIn() {
    const user = JSON.parse(sessionStorage.getItem("user") || "{}");
    console.log("User from sessionStorage:", user);
    return isLoggedIn() && user.role === "ADMIN";
}

function logout() {
    sessionStorage.removeItem("user");
    window.location.href = "login.html";
}

window.addEventListener("load", () => {
    const addProductButton = document.getElementsByClassName("add-product-button");
//    const addProductButton = document.getElementById("add-product-button");
    if (addProductButton[0] && addProductButton[1]) {
//        addProductButton.style.display = isAdminLoggedIn() ? "inline" : "none";
        addProductButton[0].style.display = isAdminLoggedIn() ? "inline" : "none";
        addProductButton[1].style.display = isAdminLoggedIn() ? "inline" : "none";
    }

    // Add event listener to the login form
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", login);
    }

    // Add event listener for the logout button
    const logoutButton = document.getElementById("logout-button");
    if (logoutButton) {
        logoutButton.addEventListener("click", (event) => {
            event.preventDefault();
            logout();
        });
    }
});
