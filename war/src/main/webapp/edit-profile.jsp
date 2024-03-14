<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- Card Section --%>

<script>
  document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form')

    if (form) {
      form.addEventListener('submit', function (e) {
        const formData = new FormData(form)

        const jsonData = Object.fromEntries(formData.entries())

        const actionURL = form.getAttribute('action')
        e.preventDefault()

        fetch(actionURL, {
          method: 'PUT',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(jsonData)
        })
          .then((response) => {
            if (response.ok) {
              return response.json()
            }
            throw new Error('Network response was not ok.')
          })
          .then((data) => {
            console.log(data)
          Toastify({
            text: "Profile updated successfully!",
            duration: 3000,
            newWindow: true,
            close: true,
            gravity: "top", // `top` or `bottom`
            position: 'right', // `left`, `center` or `right`
            stopOnFocus: true, // Prevents dismissing of toast on hover
            style: {
              background: "linear-gradient(to right, #00b09b, #96c93d)",
            },
            onClick: function(){} // Callback after click
          }).showToast();
          })

            // Handle success here (e.g., redirect or display a success message)
          })
          .catch((error) => {
            console.error(
              'There has been a problem with your fetch operation:',
              error
            )
          })
      Toastify({
        text: `An error occurred: ${error.message}`,
        duration: 3000,
        newWindow: true,
        close: true,
        gravity: "top", // `top` or `bottom`
        position: 'right', // `left`, `center` or `right`
        stopOnFocus: true, // Prevents dismissing of toast on hover
        style: {
          background: "linear-gradient(to right, #ff416c, #ff4b2b)",
        },
        onClick: function(){} // Callback after click
      }).showToast();
      })
    }
  })
</script>
<div class="max-w-4xl px-4 py-10 sm:px-6 lg:px-8 lg:py-14 mx-auto">
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
      action="${pageContext.request.contextPath}/api/${userRole}/${user.id}"
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
      </div>
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
