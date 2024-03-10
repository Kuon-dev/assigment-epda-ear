<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Users</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>

<div class="container mx-auto mt-5">
    <form action="${pageContext.request.contextPath}/users" method="get" class="mb-4">
        <input type="text" name="search" placeholder="Search by email" class="border-2 border-gray-200 rounded-lg p-2 mr-2" value="${fn:escapeXml(request.getParameter('search'))}"/>
        <button type="submit" class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Search
        </button>
    </form>
</div>

<div class="container mx-auto mt-10">
    <h1 class="text-xl font-bold mb-5">Users</h1>
    <table class="table-auto w-full mb-5">
        <thead>
        <tr class="bg-blue-200">
            <th class="px-4 py-2">ID</th>
            <th class="px-4 py-2">Name</th>
            <th class="px-4 py-2">Email</th>
            <th class="px-4 py-2">Phone</th>
            <th class="px-4 py-2">Created At</th>
            <th class="px-4 py-2">Updated At</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${users}">
            <tr>
                <td class="border px-4 py-2">${user.id}</td>
                <td class="border px-4 py-2">${user.name}</td>
                <td class="border px-4 py-2">${user.email}</td>
                <td class="border px-4 py-2">${user.phone}</td>
                <td class="border px-4 py-2"><fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td class="border px-4 py-2"><fmt:formatDate value="${user.updatedAt}" pattern="dd/MM/yyyy HH:mm"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <%-- Include Pagination Here --%>
    <c:if test="${totalPages > 1}">
        <div class="flex justify-center">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}managing-staff/users/view/${currentPage - 1}${not empty searchQuery ? '?search=' + searchQuery : ''}" class="mr-2">Previous</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="${pageContext.request.contextPath}managing-staff/users/view/${i}${not empty searchQuery ? '?search=' + searchQuery : ''}" class="${i == currentPage ? 'text-red-500' : ''} mr-2">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}managing-staff/users/view/${currentPage + 1}${not empty searchQuery ? '?search=' + searchQuery : ''}">Next</a>
            </c:if>
        </div>
    </c:if>
</div>

</body>
</html>
