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
            <select name="timeSlot" id="timeSlotSelect">
                <option value="MORNING" ${shift === 'MORNING' ? 'selected' : ''}>Morning</option>
                <option value="AFTERNOON" ${shift === 'AFTERNOON' ? 'selected' : ''}>Afternoon</option>
                <option value="ALL_DAY" ${shift === 'ALL_DAY' ? 'selected' : ''}>All Day</option>
                <option value="NOT_SCHEDULED" ${shift === 'NOT SCHEDULED' ? 'selected' : ''}>Not Scheduled</option>
            </select>
            <input type="hidden" name="vetId" value="${vetId}">
            <input type="hidden" name="date" value="${date}">
            <input type="hidden" name="dayIndex" value="${dayIndex}">
            <button type="submit">Update</button>
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


