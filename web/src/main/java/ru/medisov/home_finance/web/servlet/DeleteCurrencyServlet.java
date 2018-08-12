package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.CurrencyServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/delete_currency")
public class DeleteCurrencyServlet extends HttpServlet {
    private CurrencyService service = new CurrencyServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getSession().setAttribute("list_currencies", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/delete_currency.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("currency"));

        if (id > 0) {
            service.remove(id);
            RequestDispatcher dispatcher = req.getRequestDispatcher("currencies");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct currency info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("delete_currency.jsp");
            dispatcher.include(req, resp);
        }
    }
}
