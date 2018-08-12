package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.service.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/add_category")
public class AddCategoryServlet extends HttpServlet {
    CategoryService service = new CategoryServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getSession().setAttribute("list_categories", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/new_category.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");

        if (name != null && name.length() > 0) {
            saveCategory(req);
            RequestDispatcher dispatcher = req.getRequestDispatcher("categories");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct category info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("new_category.jsp");
            dispatcher.include(req, resp);
        }
    }

    private void saveCategory(HttpServletRequest req) {
        String name = req.getParameter("name");
        String parent = req.getParameter("parent_category");
        service.save(service.makeFromTextFields(name, parent));
    }
}
