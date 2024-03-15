<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    <%-- ========== MAIN CONTENT ========== --%>
    <jsp:include page="/WEB-INF/components/receptionist/sidebar.jsp" />
    <div class="w-full pt-10 px-4 sm:px-6 md:px-8 lg:ps-72">
      <jsp:include page="/edit-profile.jsp" />
    </div>
  </body>
</html>
