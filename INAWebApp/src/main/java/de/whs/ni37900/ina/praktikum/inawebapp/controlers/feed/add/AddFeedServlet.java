package de.whs.ni37900.ina.praktikum.inawebapp.controlers.feed.add;

import de.whs.ni37900.ina.praktikum.inawebapp.services.feed.add.AddFeedHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet
public class AddFeedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AddFeedHelper.require(request.getSession()).doGet(request, response);
    }
}
