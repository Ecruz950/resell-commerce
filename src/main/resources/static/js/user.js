const UPDATE_API_URL = "http://localhost:8080/user/update";



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
    event.preventDefault();

    // Get user info from Spring Security
    let userID, username;
    try {
        const authResponse = await fetch('/api/auth/status');
        const authData = await authResponse.json();
        
        if (!authData.authenticated) {
            alert('Please log in to update password');
            window.location.href = '/login';
            return;
        }
        
        userID = authData.userId;
        username = authData.username;
    } catch (error) {
        alert('Please log in to update password');
        window.location.href = '/login';
        return;
    }

    // Get the current password and new password from the input fields
    var currentPassword = document.getElementById("current-password").value;
    var newPassword = document.getElementById("new-password").value;

    // Make sure current password and new password are not empty
    if(!currentPassword || !newPassword){
        if(!currentPassword){
            const currPassErrorMessageElement = document.getElementById("error-message-curr-pass");
            currPassErrorMessageElement.style.display = "inline";
            const passInputElement = document.getElementById("current-password");
            passInputElement.style.borderColor = "red";
        }
        if(!newPassword){
            const newPassErrorMessageElement = document.getElementById("error-message-new-pass");
            newPassErrorMessageElement.style.display = "inline";
            const confirmInputElement = document.getElementById("new-password");
            confirmInputElement.style.borderColor = "red";
        }
        return;
    }

    // Note: Current password validation should be done server-side with Spring Security
    // For now, we'll send the request and let the server handle validation
    try {
        // Create a POST request to UPDATE_API_URL to send current and new passwords
        const response = await fetch(UPDATE_API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json"},
            credentials: "same-origin",
            body: JSON.stringify({
                id: userID,
                username,
                currentPassword: currentPassword,
                newPassword: newPassword
            })
        })
        if(!response.ok){
            throw new Error("Update failed!");
        }
        // If update is successful, display a success alert
        alert("Password updated successfully");
        // Clear form
        document.getElementById("current-password").value = "";
        document.getElementById("new-password").value = "";

    } catch (error) {
        alert(error.message);

    }
}

// Initialize the user page
window.addEventListener("load", () => {
    // Add event listener to prevent duplicate form submission
    document.getElementById("updateForm").addEventListener("submit", updateUser);
});