<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <title>APU Veterinary Clinic System</title>
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
    <div color="" id="servletException"></div>
    <jsp:include page="/WEB-INF/components/managing-staff/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <div class="max-w-4xl px-4 py-10 sm:px-6 lg:px-8 lg:py-14 mx-auto">
        <c:if test="${not empty errorMessage}">
          <div class="alert alert-danger">
            ${errorMessage}
          </div>
        </c:if>
        <%-- back button --%> 
         <a href="${pageContext.request.contextPath}/managing-staff/users/view/1" class="text-blue-600 hover:underline">Back</a> 
        <%-- Card --%>
        <div class="rounded-xl shadow p-4 sm:p-7">
          <div class="mb-8">
            <h2 class="text-xl font-bold text-gray-800">
              Profile
            </h2>
            <p class="text-sm text-gray-600">
              Manage your name, password and account settings.
            </p>
          </div>
          <form
            action="${pageContext.request.contextPath}/managing-staff/users/edit/${user.id}"
            method="post"
            >
            <%-- Grid --%>
            <div class="grid sm:grid-cols-12 gap-2 sm:gap-6">
              <%-- End Col --%>
              <div class="sm:col-span-3">
                <label
                  for="af-account-full-name"
                  class="inline-block text-sm text-gray-800 mt-2.5"
                  >
                Full name
                </label>
              </div>
              <%-- End Col --%>
              <div class="sm:col-span-9">
                <div class="sm:flex">
                  <input
                    id="name"
                    name="name"
                    type="text"
                    class="py-2 px-3 pe-11 block w-full border-gray-200 shadow-sm text-sm rounded-lg focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none"
                    placeholder="John Doe"
                    value="${user.name}"
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
                  value="${user.email}"
                  />
              </div>
              <%-- End Col --%>
              <div class="sm:col-span-3">
                <label
                  for="password"
                  class="inline-block text-sm text-gray-800 mt-2.5 "
                  >
                Password
                </label>
              </div>
              <%-- End Col --%>
              <div class="sm:col-span-9">
                <div class="space-y-2">
                  <input
                    required
                    type="password"
                    id="password"
                    name="password"
                    class="py-2 px-3 pe-11 block w-full border-gray-200 shadow-sm rounded-lg text-sm focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none "
                    placeholder="Enter new password"
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
                    value="${user.phone}"
                    />
                </div>
              </div>
              <div class="sm:col-span-3">
                <div class="inline-block">
                  <label
                    for="af-account-status"
                    class="inline-block text-sm text-gray-800 mt-2.5 "
                    >
                  Account Status
                  </label>
                </div>
              </div>
              <%-- End Col --%>
              <div class="sm:col-span-9">
                <div class="sm:flex">
                  <select name="status" id="status">
                    <c:forEach items="${allStatuses}" var="status">
                      <option value="${status}" ${user.status == status ? 'selected' : ''}>${status}</option>
                    </c:forEach>
                  </select>
                  <input type="hidden" name="userId" value="${user.id}"/>
                </div>
              </div>
            </div>
            <%-- Expertise dropdown for Veterinarians --%>
            <c:if test="${user.getClass().getSimpleName() == 'Veterinarian'}">
              <div class="sm:col-span-3">
                <label for="expertise" class="inline-block text-sm text-gray-800 mt-2.5">Expertise</label>
              </div>
              <div class="sm:col-span-9">
                <c:forEach items="${allExpertise}" var="expertise">
                  <div class="flex items-center mb-2">
                    <input
                    type="checkbox"
                    id="${expertise}"
                    name="expertise"
                    value="${expertise}"
                    <c:if test="${fn:contains(user.expertises, expertise)}">checked</c:if>
                    class="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded">
                    <label for="${expertise}" class="ml-2 block text-sm text-gray-900">${expertise}</label>
                  </div>
                </c:forEach>
              </div>
            </c:if>
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
    </div>
  </body>
</html>
