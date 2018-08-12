package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.common.model.*;
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
import java.util.Arrays;

@WebServlet("/add_transaction")
public class AddTransactionServlet extends HttpServlet {
    TransactionService service = new TransactionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getSession().setAttribute("list_accounts", new ArrayList<>(new AccountServiceImpl().findAll()));
        req.getSession().setAttribute("list_categories", new ArrayList<>(new CategoryServiceImpl().findAll()));
        req.getSession().setAttribute("list_transactionTypes", Arrays.asList(TransactionType.values()));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/new_transaction.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String amount = req.getParameter("amount");

        if (amount != null && amount.length() > 0) {
            saveTransaction(req);
            RequestDispatcher dispatcher = req.getRequestDispatcher("transactions");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct transaction info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("new_transaction.jsp");
            dispatcher.include(req, resp);
        }
    }

    private void saveTransaction(HttpServletRequest req) {
        service.save(service.makeFromTextFields(
                req.getParameter("name"), req.getParameter("amount"), req.getParameter("account"),
                req.getParameter("category"), req.getParameter("transaction_date"),
                req.getParameter("tags"), req.getParameter("transactionType"))
        );
    }
}
