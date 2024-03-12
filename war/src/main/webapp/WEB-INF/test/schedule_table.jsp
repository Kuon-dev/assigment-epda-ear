<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Weekly Schedule</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body>
    <h1>Weekly Veterinarian Schedule</h1>
        <div id="week-navigation">
        <%-- Calculate the previous and next week adjustments --%>
        <% int currentWeekAdjust = (Integer)request.getAttribute("currentWeekAdjust"); %>
        <a href="ScheduleController?weekAdjust=<%=currentWeekAdjust - 1%>">Previous Week</a>
        <span id="weekLabel"><fmt:formatDate value="${startOfWeek}" pattern="yyyy-MM-dd" /> - <fmt:formatDate value="${endOfWeek}" pattern="yyyy-MM-dd" /></span>
        <a href="ScheduleController?weekAdjust=<%=currentWeekAdjust + 1%>">Next Week</a>
    </div>

    <table>
    <thead>
        <tr>
            <th>Veterinarian Name</th>
            <th>Email</th>
            <c:forEach items="${weekDates}" var="date">
                <th><fmt:formatDate value="${date}" pattern="E MM/dd" /></th>
            </c:forEach>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${veterinarians}" var="vet">
            <tr>
                <td>${vet.name}</td>
                <td>${vet.email}</td>
                

<c:forEach items="${weekLocalDates}" var="localDate" varStatus="status">
    <td>
        <c:set var="shiftDisplay" value="NOT SCHEDULED" />
        <c:forEach items="${schedules}" var="schedule">
            <c:if test="${schedule.veterinarian.id == vet.id && schedule.date.isEqual(localDate)}">
                <c:set var="shiftDisplay" value="${schedule.shift}" />
            </c:if>
        </c:forEach>
        
        <button onclick="openDialog(this)" 
                data-vet-name="${vet.name}" 
                data-date="${localDate}" 
                data-shift="${shiftDisplay}" 
                data-vet-id="${vet.id}" 
                data-day-index="${status.index}"
                class="select-none rounded-lg bg-gradient-to-tr from-gray-900 to-gray-800 py-3 px-6 text-center align-middle font-sans text-xs font-bold uppercase text-white shadow-md shadow-gray-900/10 transition-all hover:shadow-lg hover:shadow-gray-900/20 active:opacity-[0.85] disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none">
            ${shiftDisplay}
        </button>
    </td>
</c:forEach>


            </tr>
        </c:forEach>
    </tbody>
</table>
<div
  id="dialog"
  class="pointer-events-none fixed inset-0 z-[999] grid h-screen w-screen place-items-center bg-black bg-opacity-60 
  backdrop-blur-sm transition-opacity duration-300 opacity-0 ">
  <div
    class="relative m-4 w-2/5 min-w-[40%] max-w-[40%] rounded-lg bg-white font-sans text-base font-light leading-relaxed text-blue-gray-500 antialiased shadow-2xl">
    <div
      id="dialog-title"
      class="flex items-center p-4 font-sans text-2xl antialiased font-semibold leading-snug shrink-0 text-blue-gray-900"
    >
      Its a simple dialog.
    </div>
    <div
      id="dialog-content"
      class="relative p-4 font-sans text-base antialiased font-light leading-relaxed border-t border-b border-t-blue-gray-100 border-b-blue-gray-100 text-blue-gray-500"></div>
    <div class="flex flex-wrap items-center justify-end p-4 shrink-0 text-blue-gray-500">
      <button
        onclick="closeDialog()"
        class="px-6 py-3 mr-1 font-sans text-xs font-bold text-red-500 uppercase transition-all rounded-lg middle none center hover:bg-red-500/10 active:bg-red-500/30 disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none">
        Cancel
      </button>
      <button data-ripple-light="true" data-dialog-close="true"
        class="middle none center rounded-lg bg-gradient-to-tr from-green-600 to-green-400 py-3 px-6 font-sans text-xs font-bold uppercase text-white shadow-md shadow-green-500/20 transition-all hover:shadow-lg hover:shadow-green-500/40 active:opacity-[0.85] disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none">
        Confirm
      </button>
    </div>
  </div>
</div>  

<script src="${pageContext.request.contextPath}/assets/js/schedule.js"></script>
</body>
</html>

