document.addEventListener('DOMContentLoaded', function() {
  const options = {
    actions: {
      clickDay(event, self) {
        // Assuming self.selectedDates is an array of date strings like ["2024-03-11"]
        if (self.selectedDates.length > 0) {
          const selectedDateStr = self.selectedDates[0]; // Get the first selected date as a string
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

