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