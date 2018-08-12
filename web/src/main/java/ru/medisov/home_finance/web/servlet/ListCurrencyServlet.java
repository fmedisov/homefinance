package ru.medisov.home_finance.web.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import ru.medisov.home_finance.service.CurrencyService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/currencies")
public class ListCurrencyServlet extends HttpServlet {

    @Autowired
    CurrencyService service;

    public ListCurrencyServlet() {}

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getSession().setAttribute("list_currencies", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/view_currency.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
