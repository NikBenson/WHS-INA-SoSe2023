package de.whs.ni37900.ina.praktikum.inawebapp.controlers.hello;

import java.io.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet()
public class HelloServlet extends HttpServlet {
    String greeting;

    public void init() {
        greeting = "Hello";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String name = request.getParameter("name");
        if (name == null)
            name = "World";

        request.setAttribute("greeting", greeting);
        request.setAttribute("name", name);
        request.getRequestDispatcher("views/hello.jsp").include(request, response);
    }

    public void destroy() {
    }
}