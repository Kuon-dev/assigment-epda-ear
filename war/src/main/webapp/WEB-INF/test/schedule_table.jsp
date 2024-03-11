<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Weekly Schedule</title>
    <link rel="stylesheet" type="text/css" href="yourStylesheet.css">
    <script src="yourJavascriptFile.js"></script>
</head>
<body>

    <h1>Weekly Veterinarian Schedule</h1>
    <div id="week-navigation">
        <button onclick="changeWeek(-1)">Previous Week</button>
        <span id="weekLabel">${startOfWeek} - ${endOfWeek}</span>
        <button onclick="changeWeek(1)">Next Week</button>
    </div>

    <table>
        <thead>
            <tr>
                <th>Veterinarian Name</th>
                <th>Email</th>
                <c:forEach begin="1" end="7" var="i">
                    <th><fmt:formatDate value="${startOfWeek.plusDays(i - 1)}" pattern="E MM/dd" /></th>
                </c:forEach>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${schedules}" var="schedule">
                <tr>
                    <td>${schedule.veterinarian.name}</td>
                    <td>${schedule.veterinarian.email}</td>
                    <c:forEach begin="1" end="7" var="i">
                        <td>
                            <select name="shift_${schedule.id}_${i}">
                                <option value="MORNING" ${schedule.timeSlot == 'MORNING' ? 'selected' : ''}>Morning</option>
                                <option value="AFTERNOON" ${schedule.timeSlot == 'AFTERNOON' ? 'selected' : ''}>Afternoon</option>
                                <option value="ALL_DAY" ${schedule.timeSlot == 'ALL_DAY' ? 'selected' : ''}>All Day</option>
                            </select>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
