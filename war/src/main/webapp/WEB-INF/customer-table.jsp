<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
  <head>
    <script src="https://cdn.tailwindcss.com"></script>
    <title>APU Veterinary Clinic System</title>
  </head>
  <body class="bg-gray-50slate-900">
    <%-- ========== MAIN CONTENT ========== --%>
    <%-- Sidebar Toggle --%>
    <div
      class="sticky top-0 inset-x-0 z-20 bg-white border-y px-4 sm:px-6 md:px-8 lg:hidden"
      >
      <div class="flex items-center py-4">
        <%-- Navigation Toggle --%>
        <button
          type="button"
          class="text-gray-500 hover:text-gray-600"
          data-hs-overlay="#application-sidebar"
          aria-controls="application-sidebar"
          aria-label="Toggle navigation"
          >
          <span class="sr-only">Toggle Navigation</span>
          <svg
            class="size-5"
            width="16"
            height="16"
            fill="currentColor"
            viewBox="0 0 16 16"
            >
            <path
              fill-rule="evenodd"
              d="M2.5 12a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5zm0-4a.5.5 0 0 1 .5-.5h10a.5.5 0 0 1 0 1H3a.5.5 0 0 1-.5-.5z"
              />
          </svg>
        </button>
        <%-- End Navigation Toggle --%>
        <%-- Breadcrumb --%>
        <ol
          class="ms-3 flex items-center whitespace-nowrap"
          aria-label="Breadcrumb"
          >
          <li
            class="flex items-center text-sm text-gray-800"
            >
            Application Layout
          </li>
          <li
            class="text-sm font-semibold text-gray-800 truncate"
            aria-current="page"
            >
            Dashboard
          </li>
        </ol>
        <%-- End Breadcrumb --%>
      </div>
    </div>
    <%-- End Sidebar Toggle --%>
    <%-- Sidebar --%>
    <div
      id="application-sidebar"
      class="hs-overlay hs-overlay-open:translate-x-0 -translate-x-full transition-all duration-300 transform hidden fixed top-0 start-0 bottom-0 z-[60] w-64 bg-white border-e border-gray-200 pt-7 pb-10 overflow-y-auto lg:block lg:translate-x-0 lg:end-auto lg:bottom-0 [&::-webkit-scrollbar]:w-2 [&::-webkit-scrollbar-thumb]:rounded-full [&::-webkit-scrollbar-track]:bg-gray-100 [&::-webkit-scrollbar-thumb]:bg-gray-300scrollbar-track]:bg-slate-700scrollbar-thumb]:bg-slate-500"
      >
      <div class="px-6">
        <a
          class="flex-none text-xl font-semibold"
          href="#"
          aria-label="Brand"
          >Brand</a
          >
      </div>
      <nav
        class="hs-accordion-group p-6 w-full flex flex-col flex-wrap"
        data-hs-accordion-always-open
        >
        <ul class="space-y-1.5">
          <li>
            <a
              class="flex items-center gap-x-3.5 py-2 px-2.5 bg-gray-100 text-sm text-slate-700 rounded-lg hover:bg-gray-100s:outline-nones:ring-1s:ring-gray-600"
              href="#"
              >
              <svg
                class="flex-shrink-0 size-4"
                xmlns="http://www.w3.org/2000/svg"
                width="24"
                height="24"
                viewBox="0 0 24 24"
                fill="none"
                stroke="currentColor"
                stroke-width="2"
                stroke-linecap="round"
                stroke-linejoin="round"
                >
                <path d="m3 9 9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z" />
                <polyline points="9 22 9 12 15 12 15 22" />
              </svg>
              Dashboard
            </a>
          </li>
          <li>
            <a
              class="w-full flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-slate-700 rounded-lg hover:bg-gray-100slate-400slate-300s:outline-nones:ring-1s:ring-gray-600"
              href="/receptionist/appointments/view/1"
              >
            Appointments
            </a>
          </li>
          <li>
            <a
              class="w-full flex items-center gap-x-3.5 py-2 px-2.5 text-sm text-slate-700 rounded-lg hover:bg-gray-100slate-400slate-300s:outline-nones:ring-1s:ring-gray-600"
              href="/logout"
              >
            Logout
            </a>
          </li>
        </ul>
      </nav>
    </div>
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <div class="container mx-auto mt-5">
        <form
          action="${pageContext.request.contextPath}/appointments"
          method="get"
          class="mb-4"
          >
          <input
            type="text"
            name="search"
            placeholder="Search by customer name"
            class="border-2 border-gray-200 rounded-lg p-2 mr-2"
            value="${fn:escapeXml(param.search)}"
            />
          <button
            type="submit"
            class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
          Search
          </button>
        </form>
      </div>
      
<div class="container mx-auto mt-10">
  <h1 class="text-xl font-bold mb-5">Customers</h1>
  <div class="relative flex flex-col w-full h-full overflow-scroll text-gray-700 bg-white shadow-md rounded-xl bg-clip-border max-h-[60vh]">
    <table class="w-full text-left table-auto min-w-max">
      <thead>
        <tr>
          <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">ID</th>
          <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">Name</th>
          <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">Email</th>
          <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">Phone</th>
          <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">Age</th>
          <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">Actions</th>
        </tr>
      </thead>
      <tbody>
        <c:forEach var="customer" items="${customers}">
          <tr>
            <td class="p-4 border-b border-blue-gray-50">${customer.id}</td>
            <td class="p-4 border-b border-blue-gray-50">${customer.name}</td>
            <td class="p-4 border-b border-blue-gray-50">${customer.email}</td>
            <td class="p-4 border-b border-blue-gray-50">${customer.phone}</td>
            <td class="p-4 border-b border-blue-gray-50">${customer.age}</td>
            <td class="p-4 border-b border-blue-gray-50">
              <a href="${pageContext.request.contextPath}/customers/${customer.id}/pets" class="inline-block px-4 py-2 mr-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-green-600 border border-transparent rounded-lg hover:bg-green-700 focus:outline-none focus:shadow-outline-green">View Pets</a>
              <a href="${pageContext.request.contextPath}/customers/edit/${customer.id}" class="inline-block px-4 py-2 text-sm font-medium leading-5 text-white transition-colors duration-150 bg-blue-600 border border-transparent rounded-lg hover:bg-blue-700 focus:outline-none focus:shadow-outline-blue">Edit Profile</a>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </div>
</div>

      </c:if>
    </div>
    </div>
  </body>
</html>
