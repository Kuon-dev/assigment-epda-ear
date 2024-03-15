<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <script src="https://cdn.tailwindcss.com"></script>
    <title>APU Veterinary Clinic System</title>
  </head>
  <body class="bg-gray-50">
    <jsp:include page="/WEB-INF/components/veterinarian/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <div class="flex items-center justify-between">
        <h1 class="text-2xl font-semibold">Dashboard</h1>
        <div class="flex items-center space-x-4">
          <button onclick="window.history.back()" class="inline-block px-6 py-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-blue-600 border border-transparent rounded-lg active:bg-blue-600 hover:bg-blue-700 focus:outline-none focus:shadow-outline-blue">
            Back
          </button>
        </div>
      </div>
      <div class="container mx-auto mt-10">
        <h1 class="text-xl font-bold mb-5">Appointments</h1>
        <c:choose>
          <c:when test="${not empty appointments}">
            <div class="relative flex flex-col w-full max-h-[60vh]">
              <div class="overflow-x-auto">
                <div class="inline-block min-w-full overflow-hidden">
                  <table class="w-full text-left table-auto">
                    <thead class="sticky top-0 z-10 bg-white">
                      <tr>
                        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                          <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">ID</p>
                        </th>
                        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                          <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Date</p>
                        </th>
                        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                          <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Pet Name</p>
                        </th>
                        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                          <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Customer Name</p>
                        </th>
                        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                          <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Diagnosis</p>
                        </th>
                        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                          <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Status</p>
                        </th>
                        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                          <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Actions</p>
                        </th>
                      </tr>
                    </thead>
                    <tbody class="overflow-y-auto">
                      <c:forEach var="appointment" items="${appointments}">
                        <tr>
                          <td class="p-4 border-b border-blue-gray-50">${appointment.id}</td>
                          <td class="p-4 border-b border-blue-gray-50">${formattedDates[appointment.id]}</td>
                          <td class="p-4 border-b border-blue-gray-50">${appointment.pet.name}</td>
                          <td class="p-4 border-b border-blue-gray-50">${appointment.pet.customer.name}</td>
                          <td class="p-4 border-b border-blue-gray-50">${appointment.diagnosis}</td>
                          <td class="p-4 border-b border-blue-gray-50 
                            ${appointment.status == 'COMPLETED' ? 'bg-emerald-600' : 
                            appointment.status == 'SCHEDULED' ? 'bg-sky-600' : 'bg-red-600'}">
                            <p class="text-white text-sm font-medium">${appointment.status}</p>
                          </td>
                          <td class="p-4 border-b border-blue-gray-50">
                            <a href="${pageContext.request.contextPath}/veterinarian/appointments/edit/${appointment.id}" class="inline-block px-6 py-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-blue-600 border border-transparent rounded-lg active:bg-blue-600 hover:bg-blue-700 focus:outline-none focus:shadow-outline-blue">
                            Edit
                            </a>
                          </td>
                        </tr>
                      </c:forEach>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <%-- Pagination here --%>
          </c:when>
          <c:otherwise>
            <div class="text-center py-10">
              <p class="text-lg font-semibold">No appointments found for the week</p>
            </div>
          </c:otherwise>
        </c:choose>
    </div>
    </div>
  </body>
</html>
