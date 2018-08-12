package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.AccountServiceImpl;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/delete_account")
public class DeleteAccountServlet extends HttpServlet {
    AccountService service = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getSession().setAttribute("list_accounts", new ArrayList<>(service.findAll()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/delete_account.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("account"));

        if (id > 0) {
            service.remove(id);
            RequestDispatcher dispatcher = req.getRequestDispatcher("accounts");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct account info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("delete_account.jsp");
            dispatcher.include(req, resp);
        }
    }
}
