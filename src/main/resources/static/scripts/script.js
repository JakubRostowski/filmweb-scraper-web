const menu = document.querySelector(".menu");
const menuOpenBtn = document.querySelector(".header .menu-icon");
const tiles = document.querySelectorAll(".tiles div");

menuOpenBtn.addEventListener("click", toggleMenu);

function toggleMenu() {
    menu.classList.toggle("active");
    menuOpenBtn.classList.toggle("active");
    tiles.forEach(tile => tile.classList.toggle("active"));
}