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
  <body class="bg-gray-50">
    <jsp:include page="/WEB-INF/components/receptionist/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <div class="container mx-auto mt-5">
        <form
          action="${pageContext.request.contextPath}/receptionist/customers/view/"
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
        <div>
          <a
            href="${pageContext.request.contextPath}/receptionist/customers/add"
            class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
          Add Appointment
          </a>
        </div>
      </div>
      
<div class="container mx-auto mt-10">
  <h1 class="text-xl font-bold mb-5">Customers</h1>
  <c:choose>
    <c:when test="${not empty customers}">
      <div class="relative flex flex-col w-full max-h-[60vh]">
        <div class="overflow-x-auto">
          <div class="inline-block min-w-full overflow-hidden">
            <table class="w-full text-left table-auto">
              <thead class="sticky top-0 z-10 bg-white">
                <tr>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">ID</p>
                  </th>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Name</p>
                  </th>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Email</p>
                  </th>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Phone</p>
                  </th>
                  <th class="p-4 border-b border-blue-gray-100 bg-blue-gray-50">
                    <p class="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">Age</p>
                  </th>
                </tr>
              </thead>
              <tbody class="overflow-y-auto">
                <c:forEach var="customer" items="${customers}">
                  <tr>
                    <td class="p-4 border-b border-blue-gray-50">${customer.id}</td>
                    <td class="p-4 border-b border-blue-gray-50">${customer.name}</td>
                    <td class="p-4 border-b border-blue-gray-50">${customer.email}</td>
                    <td class="p-4 border-b border-blue-gray-50">${customer.phone}</td>
                    <td class="p-4 border-b border-blue-gray-50">${customer.age}</td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </c:when>
    <c:otherwise>
      <div class="text-center py-10">
        <p class="text-lg font-semibold">No customers found.</p>
        <p>Use the search form above to find specific customers or adjust your criteria.</p>
      </div>
    </c:otherwise>
  </c:choose>
</div>

      <c:if test="${empty param.search}">
          <div class="flex items-center justify-center gap-4 mt-5">
            <!-- Previous Button -->
            <c:choose>
              <c:when test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/receptionist/customers/view/${currentPage - 1}" class="flex items-center gap-2 px-6 py-3 ...">
                  <!-- SVG for Previous -->
                  Previous
                </a>
              </c:when>
              <c:otherwise>
                <button disabled class="flex items-center gap-2 px-6 py-3 ..." type="button">
                  <!-- SVG for Previous -->
                  Previous
                </button>
              </c:otherwise>
            </c:choose>
            <!-- Page Number Buttons -->
            <div class="flex items-center gap-2">
              <c:forEach begin="${startPage}" end="${endPage}" var="i">
                <a href="${pageContext.request.contextPath}/receptionist/customers/view/${i}${not empty param.search ? '?search=' + fn:escapeXml(param.search) : ''}" class="relative h-10 w-10 ... ${currentPage == i ? 'bg-gray-900 text-white' : 'text-gray-900'}">
                <span class="absolute transform -translate-x-1/2 -translate-y-1/2 top-1/2 left-1/2">${i}</span>
                </a>
              </c:forEach>
            </div>
            <!-- Next Button -->
            <c:choose>
              <c:when test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/receptionist/customers/view/${currentPage + 1}" class="flex items-center gap-2 px-6 py-3 ...">
                  <!-- SVG for Next -->
                  Next
                </a>
              </c:when>
              <c:otherwise>
                <button disabled class="flex items-center gap-2 px-6 py-3 ..." type="button">
                  <!-- SVG for Next -->
                  Next
                </button>
              </c:otherwise>
            </c:choose>
          </div>
      </div>
      </c:if>
    </div>
    </div>
  </body>
</html>
