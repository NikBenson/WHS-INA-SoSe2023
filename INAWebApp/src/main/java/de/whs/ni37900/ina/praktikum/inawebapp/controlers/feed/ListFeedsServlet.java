package de.whs.ni37900.ina.praktikum.inawebapp.controlers.feed;

import de.whs.ni37900.ina.praktikum.inawebapp.services.feed.ListFeedHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet
public class ListFeedsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ListFeedHelper.require(request.getSession()).doGet(request, response);
    }
}
