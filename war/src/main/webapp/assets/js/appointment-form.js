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
      const dropdownButton = document.getElementById('dropdown-button');
      const dropdownMenu = document.getElementById('dropdown-menu');
      const searchInput = document.getElementById('search-input');
      let isOpen = false; // Initial state of the dropdown
      function toggleDropdown() {
        isOpen = !isOpen;
        dropdownMenu.classList.toggle('hidden', !isOpen);
      }
      dropdownButton.addEventListener('click', () => {
        toggleDropdown();
      });
      searchInput.addEventListener('input', () => {
        const searchTerm = searchInput.value.toLowerCase();
        const items = dropdownMenu.querySelectorAll('.vet-item');
        items.forEach((item) => {
          const text = item.textContent.toLowerCase();
          if (text.includes(searchTerm)) {
            item.style.display = 'block';
          } else {
            item.style.display = 'none';
          }
        });
      });
      // Optional: Handling veterinarian selection to populate a hidden input field for form submission
      document.querySelectorAll('.vet-item').forEach(item => {
        item.addEventListener('click', (e) => {
          e.preventDefault();
          const vetId = item.getAttribute('data-id');
          const vetName = item.textContent.trim();
          // Update the hidden input field
          document.querySelector('input[name="veterinarianId"]').value = vetId;
          // Update the dropdown button text
          document.getElementById('dropdown-button').textContent = vetName;
          // Hide the dropdown menu
          document.getElementById('dropdown-menu').classList.add('hidden');
        });
      });
