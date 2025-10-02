/**
 * UPDATED FOR SPRING SECURITY INTEGRATION
 * 
 * REMOVED:
 * - Manual API login logic (login() function)
 * - Client-side session management (sessionStorage)
 * - Authentication state checking (isLoggedIn(), isAdminLoggedIn())
 * - Form event listeners for login
 * 
 * REASON: Spring Security now handles all authentication server-side
 * - Form submission goes directly to Spring Security's /login endpoint
 * - Session management is handled by Spring Security
 * - Authentication state is available via Thymeleaf sec: attributes
 */

// Simple logout function - redirects to Spring Security's logout endpoint
function logout() {
    window.location.href = "/logout";
}

// Form validation for login
function validateLoginForm(event) {
    console.log("Form submitted!");
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();
    let hasError = false;

    // Reset previous error states
    document.getElementById("error-message-user").style.display = "none";
    document.getElementById("error-message-pass").style.display = "none";
    document.getElementById("username").style.borderColor = "";
    document.getElementById("password").style.borderColor = "";

    // Check username
    if (!username) {
        document.getElementById("error-message-user").style.display = "inline";
        document.getElementById("username").style.borderColor = "red";
        hasError = true;
    }

    // Check password
    if (!password) {
        document.getElementById("error-message-pass").style.display = "inline";
        document.getElementById("password").style.borderColor = "red";
        hasError = true;
    }

    // Prevent form submission if there are errors
    if (hasError) {
        event.preventDefault();
        return false;
    }
    return true;
}

// Add event listener for form validation
window.addEventListener("load", () => {
    const loginForm = document.getElementById("loginForm");
    if (loginForm) {
        loginForm.addEventListener("submit", validateLoginForm);
    }
});