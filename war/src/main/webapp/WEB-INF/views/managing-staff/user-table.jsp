<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
  <head>
    <title>APU Veterinary Clinic System</title>
    <script src="https://cdn.tailwindcss.com"></script>
  </head>
  <body class="bg-gray-50">
    <jsp:include page="/WEB-INF/components/managing-staff/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <div class="container mx-auto mt-5">
        <c:if test="${not empty sessionScope.operationMessage}">
          <div class="text-emerald-500">
            ${sessionScope.operationMessage}
          </div>
          <% session.removeAttribute("operationMessage"); %>
        </c:if>
        <form action="${pageContext.request.contextPath}/managing-staff/users/view" method="get" class="mb-4">
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
              <th class="px-4 py-2">Role</th>
              <th class="px-4 py-2">Action</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="user" items="${users}">
              <tr>
                <td class="border px-4 py-2">${user.id}</td>
                <td class="border px-4 py-2">${user.name}</td>
                <td class="border px-4 py-2">${user.email}</td>
                <td class="border px-4 py-2">${user.phone}</td>
                 <td class="border px-4 py-2">${userRoles[user.id]}</td>
                <td class="border px-4 py-2">
                  <a href="${pageContext.request.contextPath}/managing-staff/users/edit/${user.id}" class="text-blue-500 mr-2">Edit</a>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <%-- Include Pagination Here --%>
        <c:if test="${totalPages > 1}">
          <div class="flex justify-center">
            <c:if test="${currentPage > 1}">
              <a href="${pageContext.request.contextPath}/managing-staff/users/view/${currentPage - 1}${not empty searchQuery ? '?search=' + searchQuery : ''}" class="mr-2">Previous</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
              <a href="${pageContext.request.contextPath}/managing-staff/users/view/${i}${not empty searchQuery ? '?search=' + searchQuery : ''}" class="${i == currentPage ? 'text-red-500' : ''} mr-2">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
              <a href="${pageContext.request.contextPath}/managing-staff/users/view/${currentPage + 1}${not empty searchQuery ? '?search=' + searchQuery : ''}">Next</a>
            </c:if>
          </div>
        </c:if>
      </div>
    </div>
  </body>
</html>
