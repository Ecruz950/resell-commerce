const REGISTER_API_URL = "http://localhost:8080/user/register";

// Toggle password visibility
function togglePassword(inputId) {
    const input = document.getElementById(inputId);
    if(inputId == "password"){
    input.type = input.type === "password" ? "text" : "password";
    // Toggle the eye icon
    const eyeIcon = document.getElementById("eye-icon-pass-img");
    eyeIcon.src = input.type === "password" ? "images/opened-eye.png" : "images/closed-eye.png";
    }
    else if(inputId == "confirm-password"){
        input.type = input.type === "password" ? "text" : "password";
        // Toggle the eye icon
        const eyeIcon = document.getElementById("eye-icon-confirm-pass-img");
        eyeIcon.src = input.type === "password" ? "images/opened-eye.png" : "images/closed-eye.png";
    }
}

// Real-time password confirmation validation
function validatePasswordMatch() {
    console.log("Validating password match");
    const password = document.getElementById("password").value;
    const confirmPassword = document.getElementById("confirm-password").value;
    const errorElement = document.getElementById("error-message-confirm");
    const confirmInput = document.getElementById("confirm-password");
    
    if (confirmPassword && password !== confirmPassword) {
        errorElement.style.display = "inline";
        confirmInput.style.borderColor = "red";
    } else {
        errorElement.style.display = "none";
        confirmInput.style.borderColor = "";
    }
}



async function registerUser(event) {
    // Use event.preventDefault() to prevent the default action from being executed
    event.preventDefault();
    // Inside the registerUser() function, retrieve the form values: username, password, and confirmPassword
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var confirmPassword = document.getElementById("confirm-password").value;

    // Make sure username and password are not empty
    if(!username || !password || !confirmPassword){
        if(!username){
        // error message element
            const userErrorMessageElement = document.getElementById("error-message-user");
            userErrorMessageElement.style.display = "inline";
            // Change input border color to red
            const userInputElement = document.getElementById("username");
            userInputElement.style.borderColor = "red";}
        if(!password){
        // error message element
            const passErrorMessageElement = document.getElementById("error-message-pass");
            passErrorMessageElement.style.display = "inline";
            // Change input border color to red
            const passInputElement = document.getElementById("password");
            passInputElement.style.borderColor = "red";}
        if(!confirmPassword){
            const confirmErrorMessageElement = document.getElementById("error-message-confirm");
            confirmErrorMessageElement.textContent = "Confirm password is required";
            confirmErrorMessageElement.style.display = "inline";
            const confirmInputElement = document.getElementById("confirm-password");
            confirmInputElement.style.borderColor = "red";
        }
        return;
    }

    // Add form validation to ensure that password and confirmPassword match. If they don’t, display an alert: Passwords do not match
    if(password !== confirmPassword){
        alert("Passwords do not match!");
        return;
    }

    try {
        // If passwords match, create a POST request to REGISTER_API_URL to send the user data { username, password, role: "USER" }
        const response = await fetch(REGISTER_API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json"},
            body: JSON.stringify({
                username,
                password,
                role: "USER"
            })
        })
        if(!response.ok){
            // Clear input fields
            document.getElementById("username").value = "";
            document.getElementById("password").value = "";
            document.getElementById("confirm-password").value = "";
            // If the response is not ok, fill the error message html element with the error message from the response
            const errorMessage = await response.text();
            document.getElementById("error-invalid-container").style.display = "block";
            document.getElementById("error-message-invalid").textContent = errorMessage;
            return;
        }
        // If registration is successful, display a success alert and redirect the user to the login page
        alert("Registration successful");
        window.location.href = "/login";

    } catch (error) {
        alert(error.message);

    }
}