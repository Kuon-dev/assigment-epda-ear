
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Audit Log</title>
    <link rel="stylesheet" href="https://cdn.tailwindcss.com">
</head>
<body class="bg-gray-100">

<div class="container mx-auto mt-8">
    <div class="flex justify-between items-center">
        <h1 class="text-2xl font-bold">Audit Log</h1>
    </div>
    <div class="mt-4">
        <div class="overflow-x-auto relative shadow-md sm:rounded-lg">
            <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
                <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                    <th scope="col" class="py-3 px-6">Entity Name</th>
                    <th scope="col" class="py-3 px-6">Entity ID</th>
                    <th scope="col" class="py-3 px-6">Operation</th>
                    <th scope="col" class="py-3 px-6">Timestamp</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${auditLogs}" var="log">
                    <tr class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
                        <td class="py-4 px-6">${log.entityName}</td>
                        <td class="py-4 px-6">${log.entityId}</td>
                        <td class="py-4 px-6">${log.operation}</td>
                        <td class="py-4 px-6">${log.timestamp}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
        <c:if test="${empty auditLogs}">
            <div class="text-center py-10">
                <p class="text-lg font-semibold">No audit logs found.</p>
            </div>
        </c:if>
    </div>
</div>

</body>
</html>

