<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Database Health Check</title>
</head>
<body>
    <h1>Database Health Check Result</h1>
    <p><%=request.getAttribute("dbStatus") %></p>
    <p><%=request.getAttribute("dbCheck") %></p>
</body>
</html>
