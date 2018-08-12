package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.service.TransactionService;
import ru.medisov.home_finance.service.TransactionServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/transactions")
public class ListTransactionServlet extends HttpServlet {
    TransactionService service = new TransactionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.getSession().setAttribute("list_transactions", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/view_transaction.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
