<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Pet</title>
    <link href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css" rel="stylesheet">
</head>
<body>

<div class="max-w-[85rem] px-4 py-10 sm:px-6 lg:px-8 lg:py-14 mx-auto">
  <div class="mx-auto max-w-2xl">
    <div class="text-center">
      <h2 class="text-xl text-gray-800 font-bold sm:text-3xl dark:text-white">
        Edit Pet Information
      </h2>
    </div>

    <!-- Card -->
    <div class="mt-5 p-4 relative z-10 bg-white border rounded-xl sm:mt-10 md:p-10 dark:bg-gray-800 dark:border-gray-700">
      <form action="${pageContext.request.contextPath}/pets/update" method="post">
        <input type="hidden" name="id" value="${pet.id}" />

        <div class="mb-4 sm:mb-8">
          <label for="name" class="block mb-2 text-sm font-medium dark:text-white">Name</label>
          <input type="text" id="name" name="name" value="${pet.name}" required class="py-3 px-4 block w-full border-gray-200 rounded-lg text-sm focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none dark:bg-slate-900 dark:border-gray-700 dark:text-gray-400 dark:focus:ring-gray-600"/>
        </div>

        
<div class="mb-4 sm:mb-8">
  <label for="type" class="block mb-2 text-sm font-medium dark:text-white">Type</label>
  <div class="relative h-10 w-72 min-w-[200px]">
    <select id="type" name="type" class="peer h-full w-full rounded-[7px] border border-blue-gray-200 border-t-transparent bg-transparent px-3 py-2.5 font-sans text-sm font-normal text-blue-gray-700 outline outline-0 transition-all placeholder-shown:border placeholder-shown:border-blue-gray-200 placeholder-shown:border-t-blue-gray-200 empty:!bg-gray-900 focus:border-2 focus:border-gray-900 focus:border-t-transparent focus:outline-0 disabled:border-0 disabled:bg-blue-gray-50">
      <option value="Dog" ${pet.type == 'Dog' ? 'selected' : ''}>Dog</option>
      <option value="Cat" ${pet.type == 'Cat' ? 'selected' : ''}>Cat</option>
      <option value="Bird" ${pet.type == 'Bird' ? 'selected' : ''}>Bird</option>
      <option value="Rabbit" ${pet.type == 'Rabbit' ? 'selected' : ''}>Rabbit</option>
      <option value="Fish" ${pet.type == 'Fish' ? 'selected' : ''}>Fish</option>
    </select>
  </div>
</div>


        <div class="mb-4 sm:mb-8">
          <label for="breed" class="block mb-2 text-sm font-medium dark:text-white">Breed</label>
          <input type="text" id="breed" name="breed" value="${pet.breed}" required class="py-3 px-4 block w-full border-gray-200 rounded-lg text-sm focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none dark:bg-slate-900 dark:border-gray-700 dark:text-gray-400 dark:focus:ring-gray-600"/>
        </div>

        <div class="mb-4 sm:mb-8">
          <label for="age" class="block mb-2 text-sm font-medium dark:text-white">Age</label>
          <input type="number" id="age" name="age" value="${pet.age}" required class="py-3 px-4 block w-full border-gray-200 rounded-lg text-sm focus:border-blue-500 focus:ring-blue-500 disabled:opacity-50 disabled:pointer-events-none dark:bg-slate-900 dark:border-gray-700 dark:text-gray-400 dark:focus:ring-gray-600"/>
        </div>

        <div class="mt-6 grid">
          <button type="submit" class="w-full py-3 px-4 inline-flex justify-center items-center gap-x-2 text-sm font-semibold rounded-lg border border-transparent bg-blue-600 text-white hover:bg-blue-700 disabled:opacity-50 disabled:pointer-events-none dark:focus:outline-none dark:focus:ring-1 dark:focus:ring-gray-600">Update Pet</button>
        </div>
      </form>
    </div>
    <!-- End Card -->
  </div>
</div>

</body>
</html>

