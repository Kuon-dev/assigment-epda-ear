<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script src="https://cdn.tailwindcss.com"></script>
    <title>Registration Error</title>
  </head>
<body>
    <h2>Registration Error</h2>
    <c:if test="${not empty errorMsgs}">
        <div class="text-xs text-red-600">
            <ul>
                <c:forEach items="${errorMsgs}" var="msg">
                    <li><c:out value="${msg}"/></li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
    <a href="<c:url value='/register.jsp'/>">Back to Registration</a>
</body>
</html>
