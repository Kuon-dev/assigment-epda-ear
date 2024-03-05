<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
  </head>
  <body>
    <form action="calculation.jsp" method='POST'>
      Birth Year: <input type="text" name="x" size="20">
      <p><input type="submit" value="age calculation"></p>
    </form>




    <!-- jsp error handing if input is string -->
    <%
      String x = request.getParameter("x");
      int year = 0;
      try {
        year = Integer.parseInt(x);
      } catch (Exception e) {
        out.println("Sorry, an exccption occured!");
        out.println("<br>")
        out.println("Exception is: " + e);
      }
      %>




    <!-- jsp exception handing if input less than 1924 for 2023 -->
    <%
      String x = request.getParameter("x");
      try {
        int year = Integer.parseInt(x);
        if (year < 1924) {
          throw new Exception();
        }
      } catch (Exception e) {
        out.println("Sorry, an exccption occured!");
        out.println("<br>")
        out.println("Exception is: " + e);
      }}
    %>





    <!--  if the input is valid, show age -->
    <%
      String x = request.getParameter("x");
      int year = Integer.parseInt(x);
      int age = 2023 - year;
      out.println("<br>")
      out.println("You are " + age + " years old.");
    %>
  </body>
</html>
