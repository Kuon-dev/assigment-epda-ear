<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <title>New Pet Profile</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.js" defer></script>
    <script src="https://cdn.jsdelivr.net/npm/@tailwindcss/forms@0.5.7/src/index.min.js"></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
  </head>
  <body class="bg-gray-50">

    <jsp:include page="/WEB-INF/components/receptionist/sidebar.jsp" />
  <div class="max-w-4xl px-4 py-10 sm:px-6 lg:px-8 lg:py-14 mx-auto">
    <div class="rounded-xl shadow p-4 sm:p-7">
      <div class="flex items-center justify-between mb-8">
        <button 
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          onclick="window.history.back();"
        >
          <span>Back</span>
        </button>
      </div>
      <div class="mb-8">
        <h2 class="text-xl font-bold text-gray-800">Edit Pet Profile</h2>
        <c:if test="${not empty errorMessage}">
          <div id="servletException" style="color: red;">${errorMessage}</div>
        </c:if>

        <c:if test="${not empty successMessage}">
          <div id="servletSuccess" style="color: green;">${successMessage}</div>
        </c:if>
      </div>

      <form id="petForm" action="#" method="POST">
        <div class="grid sm:grid-cols-12 gap-2 sm:gap-6">
          <div class="sm:col-span-3">
            <label for="petName" class="inline-block text-sm text-gray-800 mt-2.5">
              Pet Name
            </label>
          </div>
          <div class="sm:col-span-9">
            <input id="petName" name="name" type="text" class="py-2 px-3 block w-full border-gray-200 shadow-sm text-sm rounded-lg focus:border-blue-500 focus:ring-blue-500" placeholder="Pet's name" required value="${pet.name}" />
          </div>

          <div class="sm:col-span-3">
            <label for="petType" class="inline-block text-sm text-gray-800 mt-2.5">
              Type
            </label>
          </div>
          <div class="sm:col-span-9">

<div class="mb-4 sm:mb-8">
  <label for="type" class="block mb-2 text-sm font-medium dark:text-white">Type</label>
  <div class="relative h-10 w-72 min-w-[200px]">
<select id="petType" name="petType" class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md">
    <c:forEach items="${allExpertise}" var="expertise">
        <option value="${expertise}" ${pet.type == expertise ? 'selected' : ''}>${expertise.name().replace('_', ' ')}</option>
    </c:forEach>
</select>
  </div>
</div>
          </div>

          <div class="sm:col-span-3">
            <label for="petBreed" class="inline-block text-sm text-gray-800 mt-2.5">
              Breed
            </label>
          </div>
          <div class="sm:col-span-9">
            <input id="petBreed" name="breed" type="text" class="py-2 px-3 block w-full border-gray-200 shadow-sm text-sm rounded-lg focus:border-blue-500 focus:ring-blue-500" placeholder="Breed of the pet" required value="${pet.breed}" />
          </div>

          <div class="sm:col-span-3">
            <label for="petAge" class="inline-block text-sm text-gray-800 mt-2.5">
              Age
            </label>
          </div>
          <div class="sm:col-span-9">
            <input id="petAge" name="age" type="number" class="py-2 px-3 block w-full border-gray-200 shadow-sm text-sm rounded-lg focus:border-blue-500 focus:ring-blue-500" placeholder="Pet's age" required value="${pet.age}" />
          </div>
        </div>

        <div class="mt-5 flex justify-end gap-x-2">
          <button type="button" class="py-2 px-3 inline-flex items-center gap-x-2 text-sm font-medium rounded-lg border border-gray-200 bg-white text-gray-800 hover:bg-gray-50">
            Cancel
          </button>
          <button type="submit" class="py-2 px-3 inline-flex items-center gap-x-2 text-sm font-semibold rounded-lg border border-transparent bg-blue-600 text-white hover:bg-blue-700">
            Save changes
          </button>
        </div>
      </form>
    </div>
  </div>
    <script src="${pageContext.request.contextPath}/assets/js/pet-form.js"></script>
  </body>
</html>
