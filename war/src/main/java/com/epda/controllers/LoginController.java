package com.epda.controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.epda.model.User;
import com.epda.model.UserFacade;

@WebServlet(name = "LoginController", urlPatterns = {"/Login"})
public class LoginController extends HttpServlet {

    @EJB
    private UserFacade myCustomerFacade;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String a = request.getParameter("x");
        String b = request.getParameter("y");
        
        try (PrintWriter out = response.getWriter()) {
            try{
                User found = myCustomerFacade.find(a);
                if(found == null){
                    throw new Exception();      //error 1
                }
                int x = Integer.parseInt(b);        //error 2
                if(x != found.getPassword()){
                    throw new Exception();      //error 3
                }
//                double y = Double.parseDouble(c);   //error 3
//                if(y <= 0){
//                    throw new Exception();      //error 4
//                }
//                myCustomerFacade.create(new User(a,x,y));
                HttpSession s = request.getSession();
                s.setAttribute("user", found);
                request.getRequestDispatcher("link.jsp").include(request, response);
                out.println("<br><br><br>Hi "+a+", welcome to APU!");
            }catch(Exception e){
                request.getRequestDispatcher("login.jsp").include(request, response);
                out.println("<br><br><br>Invalid input!");
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
