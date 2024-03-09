<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Customers</title>
  </head>
  <body>
    <%-- Optional: Search Form for filtering customers could go here --%>

    <%-- Customers Table --%>
    <table>
      <thead>
        <tr>
          <th>Customer ID</th>
          <th>Name</th>
          <th>Email</th>
          <%-- Add other customer attributes as needed --%>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="customer" items="${customers}">
          <tr>
            <td>${customer.id}</td>
            <td>${customer.name}</td>
            <td>${customer.email}</td>
            <%-- View Pets Button --%>
            <td>
              <a
                href="${pageContext.request.contextPath}/pets?customerId=${customer.id}"
                class="btn btn-primary"
                >View Pets</a
              >
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>
</html>
