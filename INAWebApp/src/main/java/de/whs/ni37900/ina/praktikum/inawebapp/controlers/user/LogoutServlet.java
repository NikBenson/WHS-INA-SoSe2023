package de.whs.ni37900.ina.praktikum.inawebapp.controlers.user;

import de.whs.ni37900.ina.praktikum.inawebapp.services.user.AuthHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet
public class LogoutServlet extends HttpServlet {
    //! This is a workaround as forms do not support DELETE methode
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuthHelper.require(request.getSession()).doDelete(request, response);
    }
}
