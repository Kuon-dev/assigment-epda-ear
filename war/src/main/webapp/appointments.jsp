<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Appointments</title>
    <!-- Include Tailwind CSS -->
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>

<div class="container mx-auto mt-10">
    <h1 class="text-xl font-bold mb-5">Appointments</h1>
    <table class="table-auto w-full mb-5">
        <thead>
            <tr class="bg-blue-200">
                <th class="px-4 py-2">ID</th>
                <th class="px-4 py-2">Date</th>
                <th class="px-4 py-2">Diagnosis</th>
                <th class="px-4 py-2">Prognosis</th>
                <th class="px-4 py-2">Status</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="appointment" items="${appointments}">
                <tr>
                    <td class="border px-4 py-2">${appointment.id}</td>
                    <td class="border px-4 py-2">${appointment.appointmentDate}</td>
                    <td class="border px-4 py-2">${appointment.diagnosis}</td>
                    <td class="border px-4 py-2">${appointment.prognosis}</td>
                    <td class="border px-4 py-2">${appointment.status}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="flex justify-center">
        <nav aria-label="Page navigation">
            <ul class="inline-flex -space-x-px">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li>
                        <a class="${currentPage == i ? 'text-white bg-blue-600' : 'text-blue-600 bg-white'} border px-3 py-1" href="appointments?page=${i}">
                            ${i}
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </div>
</div>

</body>
</html>
