const menu = document.querySelector(".menu");
const menuOpenBtn = document.querySelector(".header .menu-icon");

menuOpenBtn.addEventListener("click", toggleMenu);

function toggleMenu() {
    menu.classList.toggle("active");
}