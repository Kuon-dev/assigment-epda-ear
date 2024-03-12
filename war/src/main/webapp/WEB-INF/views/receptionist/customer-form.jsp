<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Make Appointment</title>
    <script src="https://cdn.tailwindcss.com"></script>
    <link href="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/vanilla-calendar-pro/build/vanilla-calendar.min.js" defer></script>
    <script src="
      https://cdn.jsdelivr.net/npm/@tailwindcss/forms@0.5.7/src/index.min.js
      "></script>
    <script type="text/javascript" src="https://cdn.jsdelivr.net/npm/toastify-js"></script>
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/toastify-js/src/toastify.min.css">
  </head>
  <body class="bg-gray-50">
    <c:if test="${not empty errorMessage}">
        <div id="servletException" style="color: red;">${errorMessage}</div>
    </c:if>

    <c:if test="${not empty successMessage}">
        <div id="servletSuccess" style="color: green;">${successMessage}</div>
    </c:if>

    <jsp:include page="/WEB-INF/components/receptionist/sidebar.jsp" />
<div class="max-w-4xl px-4 py-10 sm:px-6 lg:px-8 lg:py-14 mx-auto">
  <%-- Card --%>
  <div class="rounded-xl shadow p-4 sm:p-7">
    <div class="mb-8">
      <h2 class="text-xl font-bold text-gray-800">
        New Customer Profile
      </h2>
    </div>

    <form
        action="${pageContext.request.contextPath}/receptionist/customers/new/"
        method="POST"
    >
      <%-- Grid --%>
      <div class="grid sm:grid-cols-12 gap-2 sm:gap-6">
        <%-- End Col --%>

        <div class="sm:col-span-3">
          <label
            for="af-account-full-"
            class="inline-block text-sm text-gray-800 mt-2.5"
          >
            Full 
          </label>
        </div>
        <%-- End Col --%>

        <div class="sm:col-span-9">
          <div class="sm:flex">
            <input
              id=""
              name="name"
              type="text"
              class="py-2 px-3 pe-11 block w-full border-gray-200 shadow-sm text-sm rounded-lg focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none"
              placeholder="John Doe"
              required
            />
          </div>
        </div>
        <%-- End Col --%>

        <div class="sm:col-span-3">
          <label
            for="af-account-email"
            class="inline-block text-sm text-gray-800 mt-2.5"
          >
            Email
          </label>
        </div>
        <%-- End Col --%>

        <div class="sm:col-span-9">
          <input
            required
            id="email"
            name="email"
            type="email"
            class="py-2 px-3 pe-11 block w-full border-gray-200 shadow-sm text-sm rounded-lg focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none"
            placeholder="maria@site.com"
          />
        </div>
        <%-- End Col --%>

        <div class="sm:col-span-3">
          <label
            for="age"
            class="inline-block text-sm text-gray-800 mt-2.5 "
          >
            Age
          </label>
        </div>
        <%-- End Col --%>

        <div class="sm:col-span-9">
          <div class="space-y-2">
            <input
              required
              type="number"
              id="age"
              name="age"
              class="py-2 px-3 pe-11 block w-full border-gray-200 shadow-sm rounded-lg text-sm focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none "
              placeholder="Enter age"
            />
          </div>
        </div>
        <%-- End Col --%>

        <div class="sm:col-span-3">
          <div class="inline-block">
            <label
              for="af-account-phone"
              class="inline-block text-sm text-gray-800 mt-2.5 "
            >
              Phone
            </label>
          </div>
        </div>
        <%-- End Col --%>

        <div class="sm:col-span-9">
          <div class="sm:flex">
            <input
              required
              id="phone"
              name="phone"
              type="text"
              class="py-2 px-3 pe-11 block w-full border-gray-200 shadow-sm rounded-lg text-sm focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none "
              placeholder="+x(xxx)xxx-xx-xx"
            />
          </div>
        </div>
      </div>
      <%-- End Grid --%>

      <div class="mt-5 flex justify-end gap-x-2">
        <button
          type="button"
          class="py-2 px-3 inline-flex items-center gap-x-2 text-sm font-medium rounded-lg border border-gray-200 bg-white text-gray-800 shadow-sm hover:bg-gray-50 disabled:opacity-50 disabled:pointer-events-none "
        >
          Cancel
        </button>
        <button
          type="submit"
          class="py-2 px-3 inline-flex items-center gap-x-2 text-sm font-semibold rounded-lg border border-transparent bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50 disabled:pointer-events-none "
        >
          Save changes
        </button>
      </div>
    </form>
  </div>
</div>

  </body>
</html>
