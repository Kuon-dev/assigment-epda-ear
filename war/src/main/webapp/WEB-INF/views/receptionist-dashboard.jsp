<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <script src="https://cdn.tailwindcss.com"></script>
    <title>APU Veterinary Clinic System</title>
  </head>
  <body class="bg-gray-50slate-900">
    <%-- ========== MAIN CONTENT ========== --%>
    <jsp:include page="/WEB-INF/components/receptionist/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <jsp:include page="/edit-profile.jsp" />
    </div>
  </body>
</html>
