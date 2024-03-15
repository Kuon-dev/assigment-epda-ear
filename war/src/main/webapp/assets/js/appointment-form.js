document.addEventListener('DOMContentLoaded', function() {
  const options = {
  type: 'default',
  settings: {
    selected: {
      dates: [document.getElementById('appointmentDate').value.split('T')[0]],
    },
  },
    actions: {
      clickDay(event, self) {
        if (self.selectedDates.length > 0) {
          const selectedDateStr = self.selectedDates[self.selectedDates.length - 1]; // Get the first selected date as a string
          // self.selectedDates.shift();
          document.getElementById('appointmentDate').value = selectedDateStr;
        }
      },
    },
  };
  const calendar = new VanillaCalendar('#calendar', options);
  calendar.init();
})

// JavaScript to toggle the dropdown and search functionality
document.addEventListener('DOMContentLoaded', function() {
  const dropdownButton = document.getElementById('dropdown-button');
  const dropdownMenu = document.getElementById('dropdown-menu');
  const searchInput = document.getElementById('search-input');
  const vetList = document.getElementById('vet-list');
  const hiddenInput = document.querySelector('input[name="veterinarianId"]');
  const hiddenNameInput = document.querySelector('input[name="veterinarianName"]');
  const selectedVetName = hiddenNameInput.value; // Add this line to get the selected veterinarian's name

  // Update the dropdown button text if a veterinarian is selected
  if (selectedVetName && selectedVetName !== '') {
    dropdownButton.textContent = selectedVetName;
  }

  dropdownButton.addEventListener('click', function() {
    dropdownMenu.classList.toggle('hidden');
  });

  searchInput.addEventListener('input', function() {
    const searchValue = this.value.toLowerCase();
    const items = vetList.getElementsByClassName('vet-item');

    for (let i = 0; i < items.length; i++) {
      const item = items[i];
      const textValue = item.textContent || item.innerText;
      if (textValue.toLowerCase().indexOf(searchValue) > -1) {
        item.style.display = "";
      } else {
        item.style.display = "none";
      }
    }
  });

  vetList.addEventListener('click', function(e) {
    if(e.target && e.target.matches(".vet-item")) {
      hiddenInput.value = e.target.getAttribute('data-id');
      dropdownButton.textContent = e.target.textContent;
      dropdownMenu.classList.add('hidden');
    }
  });

  // Clicking outside the dropdown closes it
  document.addEventListener('click', function(e) {
    if (!dropdownButton.contains(e.target) && !dropdownMenu.contains(e.target)) {
      dropdownMenu.classList.add('hidden');
    }
  });
});

document.addEventListener('DOMContentLoaded', function() {
  const form = document.getElementById('appointmentForm');

  form.addEventListener('submit', function(e) {
    e.preventDefault(); // Prevent default form submission
    submitForm(); // Call a function to submit the form via AJAX
  });
});

function submitForm() {
  const appointmentId = getAppointmentIdFromUrl(); // Get the appointment ID from the URL
  if (!appointmentId) {
    console.error('Appointment ID could not be found in the URL.');
    return; // Early return if the appointment ID is not found
  }
  const formData = {
    // Assuming you've collected the rest of the form data as before
    petId: document.querySelector('input[name="petId"]').value,
    veterinarianId: document.querySelector('input[name="veterinarianId"]').value,
    timeSlot: document.getElementById('timeSlot').value,
    status: document.getElementById('status').value,
    diagnosis: document.getElementById('diagnosis').value,
    prognosis: document.getElementById('prognosis').value,
    appointmentDate: document.getElementById('appointmentDate').value,
  };

  fetch(`http://localhost:8080/api/appointment/${appointmentId}`, {
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
          text: "Successfully edited apppointment!",
          duration: 3000,
          style: {
            background: "green",
            textColor: "white"
          }
        }).showToast();
  })
  .catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
        const err = document.getElementById('servletException').textContent;
        Toastify({
          text: `An error occured! ${err}`,
          duration: 3000,
          style: {
            background: "red",
            textColor: "white"
          }
        }).showToast();

    // Handle the error
  });
}

function getAppointmentIdFromUrl() {
  const pathArray = window.location.pathname.split('/');
  // Assuming the ID is always after '/edit/', which is true based on your example URL
  const appointmentIdIndex = pathArray.indexOf('edit') + 1;
  return appointmentIdIndex < pathArray.length ? pathArray[appointmentIdIndex] : null;
}
