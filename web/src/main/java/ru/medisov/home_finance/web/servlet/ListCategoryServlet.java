package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.CategoryServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/categories")
public class ListCategoryServlet extends HttpServlet {
    CategoryService service = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getSession().setAttribute("list_categories", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/view_category.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
