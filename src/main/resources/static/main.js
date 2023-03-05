document.getElementById('form_id').addEventListener('submit', check);
function check(event) {
    let form = document.getElementById('form_id');
    let checkboxes = form.roles;
    for (let i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            return true;
        }
    }
    event.preventDefault();
    alert("Заполните хотя бы одну роль!");
    return false;
}