package ru.medisov.home_finance.web.servlet;

import ru.medisov.home_finance.common.model.CurrencyModel;
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

@WebServlet("/edit_currency")
public class EditCurrencyServlet extends HttpServlet {
    CurrencyService service = new CurrencyServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long id = Long.parseLong(req.getParameter("currency"));
        CurrencyModel currencyModel = service.findById(id).orElse(null);
        req.getSession().setAttribute("currency", currencyModel);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/new_currency.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
        //req.setCharacterEncoding("UTF-8");
        //String name = req.getParameter("name");

        //processCurrency(req, resp, name);
    }

    public void processCurrency(HttpServletRequest req, HttpServletResponse resp, String name) throws ServletException, IOException {
        if (name != null && name.length() > 0) {
            updateCurrency(req);
            RequestDispatcher dispatcher = req.getRequestDispatcher("currencies");
            dispatcher.forward(req, resp);
        } else {
            PrintWriter writer = resp.getWriter();
            writer.print("input correct currency info");
            RequestDispatcher dispatcher = req.getRequestDispatcher("new_currency.jsp");
            dispatcher.include(req, resp);
        }
    }

    private void updateCurrency(HttpServletRequest req) {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String symbol = req.getParameter("symbol");
        service.update(new CurrencyModel().setName(name).setCode(code).setSymbol(symbol));
    }


}
