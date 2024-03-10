<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Pet</title>
</head>
<body>

<h2>Edit Pet</h2>

<form action="${pageContext.request.contextPath}/pets/update" method="post">
    <input type="hidden" name="petId" value="${pet.id}" />

    <label for="name">Pet Name:</label>
    <input type="text" id="name" name="name" value="${pet.name}" required /><br/>

    <!-- Additional fields for editing other pet attributes like breed, age, etc. -->

    <button type="submit">Update Pet</button>
</form>

</body>
</html>
