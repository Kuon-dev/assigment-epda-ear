<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <title>APU Veterinary Clinic System</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.js" defer></script>
    <script src="
      https://cdn.jsdelivr.net/npm/@tailwindcss/forms@0.5.7/src/index.min.js
      "></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
  </head>
  <body class="bg-gray-50">
    <div color="" id="servletException"></div>
    <jsp:include page="/WEB-INF/components/veterinarian/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <div class="container mx-auto mt-5">
        <h2 class="text-xl font-semibold mb-4">Schedule Appointment</h2>
        <%-- back button --%>
        <button 
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          onclick="window.history.back();"
        >
          <span>Back</span>
        </button>

        <form id="appointmentForm">
          <input type="hidden" name="petId" value="${pet.getId()}" />
          <input type="hidden" name="customerId" value="${customer.getId()}" />
          <input type="hidden" name="appointmentDate" id="appointmentDate" value="${appointmentDate}" />
          <!-- Time Slot -->
          <div class="mb-4">
            <label for="timeSlot" class="block text-sm font-medium mb-1">Time Slot:</label>
            <select id="timeSlot" name="timeSlot" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" disabled>
              <c:forEach var="timeSlot" items="${timeSlots}">
                <option value="${timeSlot}" ${timeSlot == appointment.timeSlot ? 'selected' : ''}>${timeSlot}</option>
              </c:forEach>
            </select>
          </div>
          <!-- Appointment Status Dropdown -->
          <div class="mb-4">
            <label for="status" class="block text-sm font-medium mb-1">Status:</label>
            <select id="status" name="status" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5" disabled>
              <c:forEach var="status" items="${appointmentStatuses}">
                <option value="${status}" ${status == appointment.status ? 'selected' : ''}>${status}</option>
              </c:forEach>
            </select>
          </div>
          <div class="flex items-center justify-start gap-5">
            <label for="diagnosis" class="block text-sm font-medium mb-1">Veterinarian: </label>
            <div class="relative group">
              <button id="dropdown-button" type="button" class="inline-flex justify-center w-full px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-md shadow-sm focus:outline-none" disabled>
                <span>Select Veterinarian</span>
                <!-- SVG icon here if needed -->
              </button>
              <input type="hidden" name="veterinarianId" value="${selectedVetId}"/>
              <input type="hidden" name="veterinarianName" value="${selectedVetName}"/>
              <div id="dropdown-menu" class="hidden absolute z-10 mt-2 w-56 rounded-md bg-white shadow-lg">
                <div class="py-1">
                  <input id="search-input" class="block w-full px-4 py-2 text-gray-800 border rounded-md border-gray-300 focus:outline-none" type="text" placeholder="Search veterinarians" autocomplete="off">
                  <div id="vet-list" class="mt-2 max-h-32 overflow-auto">
                    <c:if test="${not empty veterinarians}">
                      <c:forEach items="${veterinarians}" var="vet">
                        <div class="vet-item block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer" data-id="${vet.id}">${vet.name}</div>
                      </c:forEach>
                    </c:if>
                    <c:if test="${empty veterinarians}">
                      <div class="text-center py-2 text-sm text-gray-700">No veterinarians found</div>
                    </c:if>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- Diagnosis -->
          <div class="mb-4">
            <label for="diagnosis" class="block text-sm font-medium mb-1">Diagnosis:</label>
            <input type="text" id="diagnosis" name="diagnosis" required class="mt-1 w-full rounded-md border-gray-200 shadow-sm sm:text-sm" value="${diagnosis}" />
          </div>
          <!-- Prognosis -->
          <div class="mb-4">
            <label for="prognosis" class="block text-sm font-medium mb-1">Prognosis:</label>
            <input type="text" id="prognosis" name="prognosis" required class="mt-1 w-full rounded-md border-gray-200 shadow-sm sm:text-sm" value="${prognosis}" />
          </div>
          <!-- Appointment Date -->
          <div class="mb-4">
            <label for="appointmentDate" class="block text-sm font-medium mb-1">Appointment Date:</label>
            <div id="calendar"></div>
            <input type="hidden" id="appointmentDate" name="appointmentDate" />
          </div>
          <!-- dropdown -->
          <button type="submit" class="mt-4 w-full bg-blue-500 text-white rounded-md py-2 font-medium">Submit</button>
        </form>
      </div>
    </div>
    <script src="${pageContext.request.contextPath}/assets/js/appointment-form-vet.js"></script>
  </body>
</html>
