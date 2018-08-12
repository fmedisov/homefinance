package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.AccountServiceImpl;
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
import java.util.Arrays;

@WebServlet("/add_account")
public class AddAccountServlet extends HttpServlet {
    AccountService service = new AccountServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        req.getSession().setAttribute("list_currencies", new ArrayList<>(new CurrencyServiceImpl().findAll()));
        req.getSession().setAttribute("list_accountTypes", Arrays.asList(AccountType.values()));
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/new_account.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");

        if (name != null && name.length() > 0) {
            saveAccount(req);
            RequestDispatcher dispatcher = req.getRequestDispatcher("accounts");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct account info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("new_account.jsp");
            dispatcher.include(req, resp);
        }
    }

    private void saveAccount(HttpServletRequest req) {
        service.save(service.makeFromTextFields(
                req.getParameter("name"), req.getParameter("currency"),
                req.getParameter("accountType"), req.getParameter("amount"))
        );
    }
}
