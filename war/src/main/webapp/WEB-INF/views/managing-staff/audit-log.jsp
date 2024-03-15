<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
<head>
    <title>Audit Log - APU Veterinary Clinic System</title>
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
      </div>
      <div class="container mx-auto mt-10">
        <h1 class="text-xl font-bold mb-5">Audit Logs</h1>
        <table class="table-auto w-full mb-5">
          <thead class="bg-blue-200">
            <tr>
              <th class="px-4 py-2">Entity Name</th>
              <th class="px-4 py-2">Entity ID</th>
              <th class="px-4 py-2">Operation</th>
              <th class="px-4 py-2">Timestamp</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach items="${auditLogs}" var="log">
              <tr>
                <td class="border px-4 py-2">${log.entityName}</td>
                <td class="border px-4 py-2">${log.entityId}</td>
                <td class="border px-4 py-2">${log.operation}</td>
                <td class="border px-4 py-2">${formattedDates[log.id]}</td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
        <%-- Pagination logic here, similar to the first JSP --%>
        <c:if test="${totalPages > 1}">
          <div class="flex justify-center">
            <c:if test="${currentPage > 1}">
              <a href="${pageContext.request.contextPath}/managing-staff/audit-logs/view/${currentPage - 1}${not empty searchQuery ? '?search=' + searchQuery : ''}" class="mr-2">Previous</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
              <a href="${pageContext.request.contextPath}/managing-staff/audit-logs/view/${i}${not empty searchQuery ? '?search=' + searchQuery : ''}" class="${i == currentPage ? 'text-red-500' : ''} mr-2">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
              <a href="${pageContext.request.contextPath}/managing-staff/audit-logs/view/${currentPage + 1}${not empty searchQuery ? '?search=' + searchQuery : ''}">Next</a>
            </c:if>
          </div>
        </c:if>
      </div>
    </div>
</body>
</html>

