package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.common.model.TransactionType;
import ru.medisov.home_finance.common.utils.ModelUtils;
import ru.medisov.home_finance.service.*;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.converter.AccountModelToViewConverter;
import ru.medisov.home_finance.web.converter.CategoryModelToViewConverter;
import ru.medisov.home_finance.web.converter.TransactionModelToViewConverter;
import ru.medisov.home_finance.web.converter.TransactionViewToModelConverter;
import ru.medisov.home_finance.web.exception.HomeFinanceWebException;
import ru.medisov.home_finance.web.utils.ViewUtils;
import ru.medisov.home_finance.web.view.AccountView;
import ru.medisov.home_finance.web.view.CategoryTransactionView;
import ru.medisov.home_finance.web.view.TransactionView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping(UrlMapper.LIST_TRANSACTION)
    public String showListTransaction(Model model) {
        model.addAttribute("list_transactions", listTransactionViews());
        setModelParameters(model);
        return "transaction/listTransaction";
    }

    @GetMapping(UrlMapper.LIST_TRANSACTION_BY_PERIOD)
    public String showListTransactionByDate(@RequestParam(value = "fromDate") String fromDate,
                                        @RequestParam(value = "upToDate") String upToDate, Model model) {
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("upToDate", upToDate);
        model.addAttribute("list_transactions", listTransactionViewsByDate(fromDate, upToDate));
        setModelParameters(model);
        return "transaction/listTransaction";
    }

    @PostMapping(value = UrlMapper.SUBMIT_TRANSACTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public String doEditSaveAccount(@RequestParam(value = "transactionId") Long transactionId, @ModelAttribute TransactionView objectTransaction) {
        objectTransaction.setId(transactionId);
        TransactionModel model = getModelFromView(objectTransaction);

        try {
            TransactionModel updated = service.update(model);
            if (updated.getId() < 1) {
                throw new HomeFinanceWebException();
            }
        } catch (HomeFinanceServiceException | HomeFinanceWebException e) {
            try {
                service.save(model);
            } catch (HomeFinanceServiceException | HomeFinanceWebException e1) {
                e1.printStackTrace();
            }
        }

        return "redirect:" + UrlMapper.LIST_TRANSACTION;
    }

    @PostMapping(value = UrlMapper.LIST_TRANSACTION, produces = MediaType.APPLICATION_JSON_VALUE)
    public String doRemoveAccount(@RequestParam("idChecked") List<String> idTransactions) {
        if(idTransactions != null){
            for(String transactionIdStr : idTransactions){
                Long transactionId = Long.parseLong(transactionIdStr);
                service.remove(transactionId);
            }
        }

        return "redirect:" + UrlMapper.LIST_TRANSACTION;
    }

    private List<TransactionView> listTransactionViews() {
        return ViewUtils.listViews(service, new TransactionModelToViewConverter());
    }

    private List<TransactionView> listTransactionViewsByDate(String fromDate, String upToDate) {
        LocalDateTime from;
        LocalDateTime to;
        if ("undefined".equals(fromDate) || "".equals(fromDate)) {
            from = LocalDateTime.now().minusYears(1000);
        } else {
            from = ModelUtils.parseDateTime(fromDate);
        }
        if ("undefined".equals(upToDate) || "".equals(upToDate)) {
            to = LocalDateTime.now();
        } else {
            to = ModelUtils.parseDateTime(upToDate);
        }
        List<TransactionView> viewList = new ArrayList<>();
        service.findByPeriod(from, to)
                .forEach(model -> viewList.add(new TransactionModelToViewConverter().convert(model)));

        return viewList;
    }

    private List<CategoryTransactionView> listCategoryViews() {
        return ViewUtils.listViews(categoryService, new CategoryModelToViewConverter());
    }

    private List<AccountView> listAccountViews() {
        return ViewUtils.listViews(accountService, new AccountModelToViewConverter());
    }

    private TransactionModel getModelFromView(TransactionView transactionView) {
        return new TransactionViewToModelConverter(categoryService, currencyService, accountService).convert(transactionView);
    }

    private void setModelParameters(Model model) {
        model.addAttribute("list_categories", listCategoryViews());
        model.addAttribute("list_accounts", listAccountViews());
        model.addAttribute("list_transactionTypes", TransactionType.values());
        model.addAttribute("objectTransaction", new TransactionView());
    }
}
