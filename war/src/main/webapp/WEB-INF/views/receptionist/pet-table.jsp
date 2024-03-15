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
    <jsp:include page="/WEB-INF/components/receptionist/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <!-- back button -->
      <div class="flex items-center mb-5">
        <a
          href="${pageContext.request.contextPath}/receptionist/customers/view/1"
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
        Back
        </a>
      </div>
      <div class="container mx-auto mt-5">
        <div>
          <a
              href="${pageContext.request.contextPath}/receptionist/pets/new/${customerId}"
            class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
          Add Pet
          </a>
        </div>
      </div>
      
<div class="container mx-auto mt-10">
  <h1 class="text-xl font-bold mb-5">Pets</h1>
  <c:choose>
    <c:when test="${not empty pets}">
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
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Name</p>
                  </th>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Type</p>
                  </th>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Breed</p>
                  </th>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Action</p>
                  </th>
                </tr>
              </thead>
              <tbody class="overflow-y-auto">
                <c:forEach var="pet" items="${pets}">
                  <tr>
                    <td class="p-4 border-b border-blue-gray-50">${pet.id}</td>
                    <td class="p-4 border-b border-blue-gray-50">${pet.name}</td>
                    <td class="p-4 border-b border-blue-gray-50">${pet.type}</td>
                    <td class="p-4 border-b border-blue-gray-50">${pet.breed}</td>
                    <td class="p-4 border-b border-blue-gray-50">
<a href="${pageContext.request.contextPath}/receptionist/pets/edit/${pet.id}" class="inline-block px-6 py-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-blue-600 border border-transparent rounded-lg active:bg-blue-600 hover:bg-blue-700 focus:outline-none focus:shadow-outline-blue">
                            Edit
                            </a>
                    </td>
                    <td>
                      <!-- new appointment with pet -->
                      <a
                        href="${pageContext.request.contextPath}/receptionist/appointments/new?petId=${pet.id}"
                        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
                      >
                        New Appointment
                      </a>
                    </td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </c:when>
    <c:otherwise>
      <div class="text-center py-10">
        <p class="text-lg font-semibold">No pets found.</p>
        <p>Adjust your criteria or use the search form to find specific pets.</p>
      </div>
    </c:otherwise>
  </c:choose>
</div>

    </div>
    </div>
  </body>
</html>
