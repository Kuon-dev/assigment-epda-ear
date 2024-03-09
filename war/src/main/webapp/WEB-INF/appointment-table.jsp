<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Appointments</title>
    <link
      href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="container mx-auto mt-5">
      <form
        action="${pageContext.request.contextPath}/appointments"
        method="get"
        class="mb-4"
      >
        <input
          type="text"
          name="search"
          placeholder="Search by customer name"
          class="border-2 border-gray-200 rounded-lg p-2 mr-2"
          value="${fn:escapeXml(param.search)}"
        />
        <button
          type="submit"
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        >
          Search
        </button>
      </form>
    </div>

    <div class="container mx-auto mt-10">
      <h1 class="text-xl font-bold mb-5">Appointments</h1>
      <c:choose>
        <c:when test="${not empty appointments}">
          <table class="table-auto w-full mb-5">
            <thead>
              <tr class="bg-blue-200">
                <th class="px-4 py-2">ID</th>
                <th class="px-4 py-2">Date</th>
                <th class="px-4 py-2">Pet Name</th>
                <th class="px-4 py-2">Customer Name</th>
                <th class="px-4 py-2">Diagnosis</th>
                <th class="px-4 py-2">Prognosis</th>
                <th class="px-4 py-2">Status</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="appointment" items="${appointments}">
                <tr>
                  <td class="border px-4 py-2">${appointment.id}</td>
                  <td class="border px-4 py-2">
                    ${formattedDates[appointment.id]}
                  </td>
                  <td class="border px-4 py-2">${appointment.pet.name}</td>
                  <td class="border px-4 py-2">
                    ${appointment.pet.customer.name}
                  </td>
                  <td class="border px-4 py-2">${appointment.diagnosis}</td>
                  <td class="border px-4 py-2">${appointment.prognosis}</td>
                  <td class="border px-4 py-2">${appointment.status}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
          <%-- Pagination here --%>
        </c:when>
        <c:otherwise>
          <div class="text-center py-10">
            <p class="text-lg font-semibold">No appointments found.</p>
            <p>
              Use the search form above to find specific appointments or adjust
              your criteria.
            </p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>
  </body>
</html>