// Wait for page to load before adding event listeners
document.addEventListener('DOMContentLoaded', function() {
    // Get references to menu elements
    const hamburgerMenu = document.querySelector('.hamburger-menu-container');
    const sideMenu = document.getElementById('side-menu');
    const overlay = document.getElementById('menu-overlay');

    // Toggle menu visibility and overlay
    function toggleMenu() {
        sideMenu.classList.toggle('side-menu-visible'); // Show/hide sidebar
        overlay.classList.toggle('active'); // Show/hide dark overlay
    }

    // Open/close menu when hamburger icon is clicked
    hamburgerMenu.addEventListener('click', toggleMenu);
    // Close menu when overlay is clicked
    overlay.addEventListener('click', toggleMenu);
    // Close menu when close icon is clicked
    const closeIcon = document.getElementById('close-side-menu-icon');
    if (closeIcon) { // Check if element exists to prevent errors
        closeIcon.addEventListener('click', function(event) {
            event.stopPropagation(); // Prevent event from bubbling to parent elements
            toggleMenu(); // Close the menu
        });
    }
});

window.addEventListener("load", () => {
      if(isLoggedIn()) {
        // Navbar adjustments for when the user is not logged in
        // Attach the logout function to the logout button if it exists
        const navLogoutLoginButton = document.getElementById("nav-logout-login-button");
        if (navLogoutLoginButton) { // Check if element exists to prevent errors
            navLogoutLoginButton.addEventListener("click", logout);
        }
        // Display the username in the navbar if they are logged in and if the element exists
        const user = JSON.parse(sessionStorage.getItem("user"));
        const usernameDisplay = document.getElementById("username-display");
        if (usernameDisplay) { // Check if element exists to prevent errors
            usernameDisplay.textContent = user.username;
        }

        // Side-Menu adjustments for when the user is logged in
        // Attach the logout function to the side-menu logout button if it exists
        const sideMenuLogoutLoginButton = document.getElementById("side-menu-logout-login-button");
        if (sideMenuLogoutLoginButton){ // Check if element exists to prevent errors
            sideMenuLogoutLoginButton.addEventListener("click", logout);
        }
      } else {
        // Navbar adjustments for when the user is not logged in
        // If not logged in, set the username display to "Sign In" if it exists
        const usernameDisplay = document.getElementById("username-display");
        if (usernameDisplay) { // Check if element exists to prevent errors
            usernameDisplay.textContent = "Guest";
        }
        // set the href attribute of the profile button to point to login.html if it exists
        const profileButton = document.getElementById("profile-button");
        if (profileButton) { // Check if element exists to prevent errors
            profileButton.setAttribute("href", "login.html");
        }
        // change the logout button text to "Login" if it exists
        const navLogoutLoginText = document.getElementById("nav-logout-login-text");
        if (navLogoutLoginText) { // Check if element exists to prevent errors
            navLogoutLoginText.textContent = "Login";
        }

        // Side-Menu adjustments for when the user is not logged in
        // change the side-menu logout button text to "Login" if it exists
        const sideMenuLogoutLoginText = document.getElementById("side-menu-logout-login-text");
        if (sideMenuLogoutLoginText) { // Check if element exists to prevent errors
            sideMenuLogoutLoginText.textContent = "Login";
        }
        // change account button display style to "none" if it exists
        const accountButton = document.getElementById("account-button");
        if (accountButton) { // Check if element exists to prevent errors
            accountButton.style.display = "none";
        }
      }
}
);