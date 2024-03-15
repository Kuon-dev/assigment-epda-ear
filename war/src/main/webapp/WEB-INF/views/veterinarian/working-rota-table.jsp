<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.LocalDate" %>
<!DOCTYPE html>
<html>
  <head>
    <script src="https://cdn.tailwindcss.com"></script>
    <title>APU Veterinary Clinic System</title>
  </head>
  <body class="bg-gray-50">
    <jsp:include page="/WEB-INF/components/veterinarian/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
    <div class="container mx-auto px-4 py-8">
      <h1 class="text-xl font-semibold mb-4">Your Weekly Rota</h1>
      <form action="${pageContext.request.contextPath}/veterinarian/weekly-rota" method="get">
        <label for="selectedRotaId">Select Week:</label>
        <select name="selectedRotaId" onchange="this.form.submit()">
          <c:forEach items="${futureRotas}" var="rota" varStatus="loop">
            <option value="${loop.index}" 
              <c:if test="${selectedRota.id == rota.id}">selected</c:if>
            >
            ${rota.startDate} to ${rota.endDate}
            </option>
          </c:forEach>
        </select>
      </form>
      <c:choose>
        <c:when test="${not empty schedulesForSelectedRota}">
          <div class="relative flex flex-col w-full max-h-[60vh]">
            <div class="overflow-x-auto">
              <div class="inline-block min-w-full overflow-hidden">
                <table class="w-full text-left table-auto">
                  <thead class="sticky top-0 z-10 bg-white">
                    <tr>
                      <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                        <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Date</p>
                      </th>
                      <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                        <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Shift</p>
                      </th>
                      <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                        <p block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70>
                        Action</p>
                      </th>
                    </tr>
                  </thead>
                  <tbody class="overflow-y-auto">
                    <c:forEach var="schedule" items="${schedulesForSelectedRota}">
                      <tr>
                        <td class="p-4 border-b border-blue-gray-50">
                          ${formattedDate[schedule.id]} 
                        </td>
                        <td class="p-4 border-b border-blue-gray-50">${dayOfWeek[schedule.id]}</td>
                        <td class="p-4 border-b border-blue-gray-50">${schedule.shift}</td>
                        <td class="p-4 border-b border-blue-gray-50">
                          <a href="${pageContext.request.contextPath}/veterinarian/appointments/view/${selectedRota.id}?dayOfWeek=${dayOfWeek[schedule.id]}" class="text-blue-500">View Schedule</a>
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
          <div class="text-center py-4">
            <p class="text-lg font-semibold">No schedules found for the selected week.</p>
          </div>
        </c:otherwise>
      </c:choose>
      <c:if test="${not empty errorMessage}">
        <div class="text-red-500">${errorMessage}</div>
      </c:if>
    </div>
    </div>
  </body>
</html>
