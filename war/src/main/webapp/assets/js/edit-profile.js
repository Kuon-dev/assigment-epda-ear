// updateProfile.js
document.addEventListener('DOMContentLoaded', (event) => {
    const form = document.querySelector('form');
    
    if(form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();

            const formData = new FormData(form);
            const jsonData = Object.fromEntries(formData.entries());

            const actionURL = form.getAttribute('action');

            fetch(actionURL, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(jsonData),
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                throw new Error('Network response was not ok.');
            })
            .then(data => {
                console.log(data);
                // Handle success here (e.g., redirect or display a success message)
            })
            .catch(error => {
                console.error('There has been a problem with your fetch operation:', error);
            });
        });
    }
});
