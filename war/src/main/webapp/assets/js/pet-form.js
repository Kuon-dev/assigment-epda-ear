document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('petForm');
  
    form.addEventListener('submit', function(e) {
      e.preventDefault(); // Prevent default form submission
      submitPetForm(); // Call a function to submit the form via AJAX
    });
  });
  
  function submitPetForm() {
    const petId = getPetIdFromUrl(); // Get the pet ID from the URL
    if (!petId) {
      console.error('Pet ID could not be found in the URL.');
      return; // Early return if the pet ID is not found
    }
    const formData = {
      name: document.getElementById('petName').value,
      type: document.getElementById('petType').value,
      breed: document.getElementById('petBreed').value,
      age: document.getElementById('petAge').value,
    };
  
    fetch(`http://localhost:8080/api/pet/${petId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(formData),
    })
    .then(response => {
      if(response.ok) {
        return response.json();
      }
      throw new Error('Network response was not ok.');
    })
    .then(data => {
      console.log(data);
          Toastify({
            text: "Pet profile updated successfully!",
            duration: 3000,
            newWindow: true,
            close: true,
            gravity: "top", // `top` or `bottom`
            position: 'right', // `left`, `center` or `right`
            stopOnFocus: true, // Prevents dismissing of toast on hover
            style: {
              background: "linear-gradient(to right, #00b09b, #96c93d)",
            },
            onClick: function(){} // Callback after click
          }).showToast();
    })
    .catch(error => {
      console.error('There has been a problem with your fetch operation:', error);
      Toastify({
        text: `An error occurred: ${error.message}`,
        duration: 3000,
        newWindow: true,
        close: true,
        gravity: "top", // `top` or `bottom`
        position: 'right', // `left`, `center` or `right`
        stopOnFocus: true, // Prevents dismissing of toast on hover
        style: {
          background: "linear-gradient(to right, #ff416c, #ff4b2b)",
        },
        onClick: function(){} // Callback after click
      }).showToast();
    });
  }
  
  function getPetIdFromUrl() {
    const pathArray = window.location.pathname.split('/');
    // Assuming the ID is always after '/edit/', which is true based on your example URL
    const petIdIndex = pathArray.indexOf('edit') + 1;
    return petIdIndex < pathArray.length ? pathArray[petIdIndex] : null;
  }
