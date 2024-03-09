<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Make Appointment</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.js" defer></script>
  </head>
  <body>
    <div class="max-w-lg mx-auto p-8">
      <h2 class="text-xl font-semibold mb-4">Schedule Appointment</h2>
      <form action="${pageContext.request.contextPath}/appointments/create" method="post">
        <input type="hidden" name="petId" value="${param.petId}" />
        <input type="hidden" name="customerId" value="${param.customerId}" />
        <!-- Time Slot -->
        <div class="mb-4">
          <label for="timeSlot" class="block text-sm font-medium mb-1">Time Slot:</label>
          <select id="timeSlot" name="timeSlot" class="... your classes ...">
            <c:forEach var="timeSlot" items="${timeSlots}">
              <option value="${timeSlot}">${timeSlot}</option>
            </c:forEach>
          </select>
        </div>
        <!-- Appointment Status Dropdown -->
        <div class="mb-4">
          <label for="status" class="block text-sm font-medium mb-1">Status:</label>
          <select id="status" name="status" class="... your classes ...">
            <c:forEach var="status" items="${appointmentStatuses}">
              <option value="${status}">${status}</option>
            </c:forEach>
          </select>
        </div>
        <!-- Diagnosis -->
        <div class="mb-4">
          <label for="diagnosis" class="block text-sm font-medium mb-1">Diagnosis:</label>
          <input type="text" id="diagnosis" name="diagnosis" required class="w-full border-gray-300 rounded-md shadow-sm" />
        </div>
        <!-- Prognosis -->
        <div class="mb-4">
          <label for="prognosis" class="block text-sm font-medium mb-1">Prognosis:</label>
          <input type="text" id="prognosis" name="prognosis" required class="w-full border-gray-300 rounded-md shadow-sm" />
        </div>
        <!-- Appointment Date -->
        <div class="mb-4">
          <label for="appointmentDate" class="block text-sm font-medium mb-1">Appointment Date:</label>
          <div id="calendar"></div>
          <input type="hidden" id="appointmentDate" name="appointmentDate" />
        </div>
        <div class="flex items-center justify-center">
          <div class="relative group">
            <button type="button" id="dropdown-button" class="inline-flex justify-center w-full px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-100 focus:ring-blue-500">
              <span class="mr-2">Select Veterinarian</span>
              <!-- SVG icon -->
            </button>
            <!-- Dropdown menu -->
            <select id="dropdown-menu" class="hidden absolute right-0 mt-2 rounded-md shadow-lg bg-white ring-1 ring-black ring-opacity-5 p-1 space-y-1">
              <!-- Search input -->
              <input id="search-input" class="block w-full px-4 py-2 text-gray-800 border rounded-md border-gray-300 focus:outline-none" type="text" placeholder="Search veterinarians" autocomplete="off">
              <!-- Check if the veterinarians list is empty -->
              <c:choose>
                <c:when test="${not empty veterinarians}">
                  <c:forEach items="${veterinarians}" var="vet">
                    <option value="${vet.id}" ${vet.id==selectedVetId ? 'selected' : '' } class="vet-item block px-4 py-2 text-gray-700 hover:bg-gray-100 active:bg-blue-100 cursor-pointer rounded-md">${vet.name}</option>
                  </c:forEach>
                </c:when>
                <c:otherwise>
                  <div class="px-4 py-2 text-gray-700">No veterinarians found</div>
                </c:otherwise>
              </c:choose>
            </select>
          </div>
        </div>
        <button type="submit" class="mt-4 w-full bg-blue-500 text-white rounded-md py-2 font-medium">Submit</button>
      </form>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/appointment-form.js"></script>
  </body>
</html>
