<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <title>Pets</title>
    <link rel="stylesheet" href="//path/to/your/css" type="text/css" />
    <%-- Optional: Link to your CSS file --%>
  </head>
  <body>
    <%-- Search Form --%>
    <form action="pets" method="get">
      <input
        type="text"
        name="customer"
        placeholder="Search by customer name"
        value="${param.customer}"
      />
      <button type="submit">Search</button>
    </form>

    <%-- Pets Table --%>
    <table>
      <thead>
        <tr>
          <th>Pet ID</th>
          <th>Pet Name</th>
          <th>Customer Name</th>
          <%-- Add other pet attributes as needed --%>
        </tr>
      </thead>

      <tbody>
        <c:forEach var="pet" items="${pets}">
          <tr>
            <td>${pet.id}</td>
            <td>${pet.name}</td>
            <td>${pet.customer.name}</td>
            <%-- Additional pet attributes --%>
            <td>
              <a
                href="${pageContext.request.contextPath}/appointments/new?petId=${pet.id}&customerId=${pet.customer.id}"
                class="btn"
                >Make Appointment</a
              >
            </td>
            <td>
              <a
                href="${pageContext.request.contextPath}/pets/edit?petId=${pet.id}"
                class="btn btn-info"
                >Edit</a
              >
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </body>
</html>
