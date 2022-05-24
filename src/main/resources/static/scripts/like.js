const likeBtns = document.querySelectorAll('.like-icon');

likeBtns.forEach(btn => btn.addEventListener("click", toggleLike2));

function toggleLike2(e) {
    const id = e.target.getAttribute('data-icon-id');
    document.querySelector('[data-form-id="' + id + '"]').submit();
}