<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>
    <script src="https://cdn.tailwindcss.com"></script>
    <title>APU Veterinary Clinic System</title>
  </head>
  <body class="bg-gray-50">
    <%-- ========== MAIN CONTENT ========== --%>
    <jsp:include page="/WEB-INF/components/managing-staff/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
<h2>Reports Dashboard</h2>
<form action="/managing-staff/report/view" method="GET">
    <select name="reportType">
        <option value="petDistribution">Pet Distribution</option>
        <option value="appointmentStatus">Appointment Status Distribution</option>
        <option value="veterinarianExpertise">Veterinarian Expertise Distribution</option>
        <option value="customerDemographics">Customer Demographics</option>
        <option value="monthlyAppointments">Monthly Appointment Trends</option>
    </select>
    <button type="submit">Generate Report</button>
</form>
<c:choose>
    <c:when test="${reportType == 'petDistribution'}">
        <jsp:include page="/WEB-INF/views/managing-staff/report/pet-distribution.jsp"/>
    </c:when>
    <c:when test="${reportType == 'appointmentStatus'}">
        <jsp:include page="/WEB-INF/views/managing-staff/report/appointment-status.jsp"/>
    </c:when>
    <c:when test="${reportType == 'veterinarianExpertise'}">
        <jsp:include page="/WEB-INF/views/managing-staff/report/expertise-distribution.jsp"/>
    </c:when>
    <c:when test="${reportType == 'customerDemographics'}">
        <jsp:include page="/WEB-INF/views/managing-staff/report/customer-age-distribution.jsp"/>
    </c:when>
    <c:when test="${reportType == 'monthlyAppointments'}">
        <jsp:include page="/WEB-INF/views/managing-staff/report/appointment-trend.jsp"/>
    </c:when>
    <c:otherwise>
        Select a report to view.
    </c:otherwise>
</c:choose>

    </div>
  </body>
</html>
