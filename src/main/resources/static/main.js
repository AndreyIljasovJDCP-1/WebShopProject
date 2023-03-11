document.getElementById('form_id').addEventListener('submit', (event) => {
    let checkboxes = document.querySelectorAll('input[name="roles"]');
    for (const checkbox of checkboxes) {
        if (checkbox.checked) {
            return true;
        }
    }
    event.preventDefault();
    alert("Отметьте хотя бы одну роль!");
    return false;
});