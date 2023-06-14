package de.whs.ni37900.ina.praktikum.inawebapp.controlers.feed.edit;

import de.whs.ni37900.ina.praktikum.inawebapp.services.feed.edit.EditFeedHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet
public class EditFeedServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditFeedHelper.require(request.getSession()).doGet(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditFeedHelper.require(request.getSession()).doPost(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EditFeedHelper.require(request.getSession()).doDelete(request, response);
    }
}
