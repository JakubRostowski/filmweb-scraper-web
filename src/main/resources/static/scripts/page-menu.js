const input = document.querySelector(".page-menu input");
const maxPage = document.querySelector(".page-menu .page-max").textContent;

input.addEventListener('keyup', redirectIfCorrect);

function redirectIfCorrect(e) {
    if (input.value) {
        if (input.value < 1) {
            input.value = 1;
        } else if (input.value > maxPage) {
            input.value = parseInt(maxPage);
        } else if (e.key === 'Enter') {
            location.replace(location.origin + '/admin-panel/page/' + input.value);
        }
    }
}
