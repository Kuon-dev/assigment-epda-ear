<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>Weekly Schedule</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">

</head>
<body>

<div class="bg-white shadow-md rounded-lg overflow-hidden">
    <h1 class="text-xl font-semibold text-gray-800 p-5">Weekly Veterinarian Schedule</h1>
    <div id="week-navigation" class="flex justify-between items-center bg-blue-100 p-4">
        <% int currentWeekAdjust = (Integer)request.getAttribute("currentWeekAdjust"); %>
        <a href="ScheduleController?weekAdjust=<%=currentWeekAdjust - 1%>" class="text-blue-600 hover:text-blue-800 transition duration-300 ease-in-out">
            Previous Week
        </a>
        <span id="weekLabel" class="text-lg font-medium text-gray-700">
            <fmt:formatDate value="${startOfWeek}" pattern="yyyy-MM-dd" /> - 
            <fmt:formatDate value="${endOfWeek}" pattern="yyyy-MM-dd" />
        </span>
        <a href="ScheduleController?weekAdjust=<%=currentWeekAdjust + 1%>" class="text-blue-600 hover:text-blue-800 transition duration-300 ease-in-out">
            Next Week
        </a>
    </div>
</div>
    
<div class="relative flex flex-col w-full h-full overflow-auto text-gray-700 bg-white shadow-md rounded-xl">
  <table class="w-full text-left table-auto">
    <thead>
      <tr>
        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
          <p class="font-sans text-sm font-normal leading-none text-blue-gray-900 opacity-70">Veterinarian Name</p>
        </th>
        <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
          <p class="font-sans text-sm font-normal leading-none text-blue-gray-900 opacity-70">Email</p>
        </th>
        <c:forEach items="${weekDates}" var="date">
          <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
            <fmt:formatDate value="${date}" pattern="E MM/dd" type="both" dateStyle="short" />
          </th>
        </c:forEach>
      </tr>
    </thead>
    <tbody>
       <tr class="bg-blue-100">
        <th class="p-4 border-t border-blue-gray-100" colspan="2">Expertise Available:</th>
        <c:forEach items="${weekLocalDates}" var="date" varStatus="status">
<td class="p-4 border-t border-blue-gray-100">
    <div class="flex flex-col space-y-2">
        <div class="flex justify-between">
            <div class="flex-1">
                <h3 class="font-bold text-sm">Morning</h3>
                
            <ul class="list-disc pl-5">
              <h3>Morning</h3>
              <c:forEach items="${expertisesPerDayMorning[date]}" var="expertise">
                <li>${expertise}</li>
              </c:forEach>
            </ul>

            </div>
            <div class="flex-1">
                <h3 class="font-bold text-sm">Afternoon</h3>
            <ul class="list-disc pl-5">
              <h3>Afternoon</h3>
              <c:forEach items="${expertisesPerDayAfternoon[date]}" var="expertise">
                <li>${expertise}</li>
              </c:forEach>
            </ul>

            </div>
        </div>
    </div>
</td>
        </c:forEach>
      </tr>     

<c:forEach items="${veterinarians}" var="vet">
    <tr>
        <td class="p-4 border-b border-blue-gray-50">
            <p class="font-sans text-sm font-normal leading-normal text-blue-gray-900">${vet.name}</p>
        </td>
        <td class="p-4 border-b border-blue-gray-50">
            <p class="font-sans text-sm font-normal leading-normal text-blue-gray-900">${vet.email}</p>
        </td>
        <c:forEach items="${weekLocalDates}" var="localDate" varStatus="status">
            <td class="p-4 border-b border-blue-gray-50">
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
                        class="select-none rounded-lg bg-gradient-to-tr from-gray-900 to-gray-800 py-3 px-6 text-center align-middle font-sans text-xs font-bold uppercase text-white shadow-md shadow-gray-900/10 transition-all hover:shadow-lg hover:shadow-gray-900/20 active:opacity-[0.85] disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none w-full">
                    ${shiftDisplay}
                </button>
            </td>
        </c:forEach>
    </tr>
</c:forEach>

    </tbody>
  </table>
</div>

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
      <button
        id="confirmButton"
        class="middle none center rounded-lg bg-gradient-to-tr from-green-600 to-green-400 py-3 px-6 font-sans text-xs font-bold uppercase text-white shadow-md shadow-green-500/20 transition-all hover:shadow-lg hover:shadow-green-500/40 active:opacity-[0.85] disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none">
        Confirm
      </button>
    </div>
  </div>
</div>  

<script src="${pageContext.request.contextPath}/assets/js/schedule.js"></script>
</body>
</html>

