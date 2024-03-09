<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Appointments</title>
    <!-- Include Tailwind CSS -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>

<div class="container mx-auto mt-5">
    <form action="${pageContext.request.contextPath}/appointments" method="get" class="mb-4">
        <input type="text" name="search" placeholder="Search by customer name" class="border-2 border-gray-200 rounded-lg p-2 mr-2" value="${fn:escapeXml(param.search)}"/>
        <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
          Search
        </button>
    </form>
</div>

<div class="container mx-auto mt-10">
    <h1 class="text-xl font-bold mb-5">Appointments</h1>
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
            <td class="border px-4 py-2">${formattedDates[appointment.id]}</td>
            <td class="border px-4 py-2">${appointment.pet.name}</td>
            <td class="border px-4 py-2">${appointment.pet.customer.name}</td>
            <td class="border px-4 py-2">${appointment.diagnosis}</td>
            <td class="border px-4 py-2">${appointment.prognosis}</td>
            <td class="border px-4 py-2">${appointment.status}</td>
        </tr>
    </c:forEach>
</tbody>

    </table>
<c:if test="${empty param.search}">
<div class="flex items-center justify-center gap-4 mt-5">
    <!-- Previous Button -->
    <c:choose>
        <c:when test="${currentPage > 1}">
            <a href="appointments?page=${currentPage - 1}" class="flex items-center gap-2 px-6 py-3 ...">
                <!-- SVG for Previous -->
                Previous
            </a>
        </c:when>
        <c:otherwise>
            <button disabled class="flex items-center gap-2 px-6 py-3 ..." type="button">
                <!-- SVG for Previous -->
                Previous
            </button>
        </c:otherwise>
    </c:choose>

    <!-- Page Number Buttons -->
    <div class="flex items-center gap-2">
        <c:forEach begin="${startPage}" end="${endPage}" var="i">
            <a href="${pageContext.request.contextPath}/appointments/view/${i}${not empty param.search ? '?search=' + fn:escapeXml(param.search) : ''}" class="relative h-10 w-10 ... ${currentPage == i ? 'bg-gray-900 text-white' : 'text-gray-900'}">
                <span class="absolute transform -translate-x-1/2 -translate-y-1/2 top-1/2 left-1/2">${i}</span>
            </a>
        </c:forEach>
    </div>

    <!-- Next Button -->
    <c:choose>
        <c:when test="${currentPage < totalPages}">
            <a href="appointments?page=${currentPage + 1}" class="flex items-center gap-2 px-6 py-3 ...">
                <!-- SVG for Next -->
                Next
            </a>
        </c:when>
        <c:otherwise>
            <button disabled class="flex items-center gap-2 px-6 py-3 ..." type="button">
                <!-- SVG for Next -->
                Next
            </button>
        </c:otherwise>
    </c:choose>
</div>
</div>
</c:if>

</body>
</html>
