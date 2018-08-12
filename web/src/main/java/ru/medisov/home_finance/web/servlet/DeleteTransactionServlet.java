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
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/delete_transaction")
public class DeleteTransactionServlet extends HttpServlet {
    TransactionService service = new TransactionServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getSession().setAttribute("list_transactions", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/delete_transaction.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("transaction"));

        if (id > 0) {
            service.remove(id);
            RequestDispatcher dispatcher = req.getRequestDispatcher("transactions");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct transaction info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("delete_transaction.jsp");
            dispatcher.include(req, resp);
        }
    }
}
