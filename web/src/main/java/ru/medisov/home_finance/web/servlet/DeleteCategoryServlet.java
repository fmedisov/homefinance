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
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/delete_category")
public class DeleteCategoryServlet extends HttpServlet {
    CategoryService service = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getSession().setAttribute("list_categories", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/delete_category.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("category"));

        if (id > 0) {
            service.remove(id);
            RequestDispatcher dispatcher = req.getRequestDispatcher("categories");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct category info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("delete_category.jsp");
            dispatcher.include(req, resp);
        }
    }
}
