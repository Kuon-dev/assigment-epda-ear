function openDialog(buttonElement) {
    var vetName = buttonElement.getAttribute('data-vet-name');
    var date = buttonElement.getAttribute('data-date');
    var shift = buttonElement.getAttribute('data-shift');
    var vetId = buttonElement.getAttribute('data-vet-id');
    var dayIndex = buttonElement.getAttribute('data-day-index');

    document.getElementById('dialog').style.opacity = '1';
    document.getElementById('dialog').style.pointerEvents = 'auto';
    document.getElementById('dialog-title').innerText = vetName + ' - ' + date;

    // Set the dialog content to a select form for time slots
    let dialogContent = document.getElementById('dialog-content');
    dialogContent.innerHTML = `
        <form id="scheduleForm">
            <select name="shift" id="timeSlotSelect">
                <option value="MORNING" ${shift === 'MORNING' ? 'selected' : ''}>Morning</option>
                <option value="AFTERNOON" ${shift === 'AFTERNOON' ? 'selected' : ''}>Afternoon</option>
                <option value="ALL_DAY" ${shift === 'ALL_DAY' ? 'selected' : ''}>All Day</option>
                <option value="NOT_SCHEDULED" ${shift === 'NOT SCHEDULED' ? 'selected' : ''}>Not Scheduled</option>
            </select>
            <input type="hidden" name="vet_id" value="${vetId}">
            <input type="hidden" name="date" value="${date}">
        </form>
    `;
}

function closeDialog() {
    document.getElementById('dialog').style.opacity = '0';
    document.getElementById('dialog').style.pointerEvents = 'none';
    // Optional: Clear the dialog contents
    document.getElementById('dialog-title').innerText = '';
    document.getElementById('dialog-content').innerHTML = '';
}

// check submit schedule for publishing working rota
document.getElementById('scheduleBtn').addEventListener('click', function() {
  // get start of week and end of week
  var startOfWeek = document.getElementById('startOfWeek').innerText;
  var endOfWeek = document.getElementById('endOfWeek').innerText;
  // remove white spaces
  startOfWeek = startOfWeek.replace(/\s/g, '');
  endOfWeek = endOfWeek.replace(/\s/g, '');

  // check if start of week and end of week are empty
  if (startOfWeek === '' || endOfWeek === '') {
    alert('Error: Start and end dates are required.');
    return;
  }

  fetch('/api/managing-staff/schedules/publish', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ startOfWeek, endOfWeek }),
  })
  .then(response => {
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    return response.json();
  })
  .then(() => {
    Toastify({
      text: 'Successfully published working rota!',
      duration: 3000,
      style: {
        background: 'green',
        color: 'white',
      },
    }).showToast();
    window.location.reload();
  })
  .catch((error) => {
    console.error('Error:', error);
    Toastify({
      text: 'Error publishing working rota.',
      duration: 3000,
      style: {
        background: 'red',
        color: 'white',
      },
    }).showToast();
  })
})


document.getElementById("confirmButton").addEventListener("click", function() {
    const formData = new FormData(document.getElementById("scheduleForm"));
    const shift = formData.get("shift");
    const vetId = formData.get("vet_id");
    const date = formData.get("date");

    if (shift === "NOT_SCHEDULED") {
        // Perform DELETE request
        fetch(`/api/schedule/?vet_id=${vetId}&date=${date}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(response => {
        // response 204
            if (response.status !== 204) {
                throw new Error('Network response was not ok');
            }
        })
        .then(() => {
            Toastify({
                text: "Appointment successfully deleted!",
                duration: 3000,
                style: {
                    background: "green",
                    color: "white"
                }
            }).showToast();
            closeDialog(); // Close the dialog
            window.location.reload(); // Refresh the page to reflect changes
        })
        .catch((error) => {
            console.error('Error:', error);
            Toastify({
                text: "Error deleting appointment.",
                duration: 3000,
                style: {
                    background: "red",
                    color: "white"
                }
            }).showToast();
        });
    } else {
        // Perform POST request for updating/creating the schedule
        const data = Object.fromEntries(formData.entries());
        fetch('/api/schedule/', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(() => {
            Toastify({
                text: "Successfully edited appointment!",
                duration: 3000,
                style: {
                    background: "green",
                    color: "white"
                }
            }).showToast();
            closeDialog(); // Close the dialog
            window.location.reload(); // Refresh the page to reflect changes
        })
        .catch((error) => {
            console.error('Error:', error);
            Toastify({
                text: "Error editing appointment.",
                duration: 3000,
                style: {
                    background: "red",
                    color: "white"
                }
            }).showToast();
        });
    }
});


