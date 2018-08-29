package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.common.model.TransactionType;
import ru.medisov.home_finance.common.utils.ModelUtils;
import ru.medisov.home_finance.service.*;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.converter.*;
import ru.medisov.home_finance.web.view.AccountView;
import ru.medisov.home_finance.web.view.CategoryTransactionView;
import ru.medisov.home_finance.web.view.TransactionView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService service;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountConverter accountConverter;

    @Autowired
    private CategoryConverter categoryConverter;

    @Autowired
    private TransactionConverter transactionConverter;

    @GetMapping(UrlMapper.LIST_TRANSACTION)
    public String showListTransaction(Model model) {
        model.addAttribute("list_transactions", getTransactionViewList());
        model.addAttribute("selectedCategory", "Не выбрано");
        model.addAttribute("selectedType", "Все");
        model.addAttribute("list_ieNoCategories", getSumsNoCategory(null, null));
        model.addAttribute("list_ieByCategories", getSumsByCategory(null, null));
        setModelParameters(model);
        return "transaction/listTransaction";
    }

    @GetMapping(UrlMapper.LIST_TRANSACTION_BY_PERIOD)
    public String showListTransactionByDate(@RequestParam String fromDate,
                                        @RequestParam String upToDate, Model model) {
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("upToDate", upToDate);
        model.addAttribute("selectedCategory", "Не выбрано");
        model.addAttribute("selectedType", "Все");
        model.addAttribute("list_transactions", listTransactionViewsByDate(fromDate, upToDate));
        model.addAttribute("list_ieNoCategories", getSumsNoCategory(fromDate, upToDate));
        model.addAttribute("list_ieByCategories", getSumsByCategory(fromDate, upToDate));
        setModelParameters(model);
        return "transaction/listTransaction";
    }

    @GetMapping(UrlMapper.LIST_TRANSACTION_BY_PERIOD_AND_TYPE)
    public String showListTransactionByDateAndType(@RequestParam String fromDate,
                                            @RequestParam String upToDate,
                                            @RequestParam String type, Model model) {
        model.addAttribute("fromDate", fromDate);
        model.addAttribute("upToDate", upToDate);
        model.addAttribute("selectedCategory", "Не выбрано");
        model.addAttribute("selectedType", type);
        model.addAttribute("list_transactions", listTransactionViewsByDateAndType(fromDate, upToDate, type));
        model.addAttribute("list_ieNoCategories", getSumsNoCategory(fromDate, upToDate));
        model.addAttribute("list_ieByCategories", getSumsByCategory(fromDate, upToDate));

        setModelParameters(model);
        return "transaction/listTransaction";
    }

    @GetMapping(UrlMapper.LIST_TRANSACTION_BY_CATEGORY)
    public String showListTransactionByCategory(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("selectedCategory", category);
        model.addAttribute("selectedType", "Все");
        model.addAttribute("list_transactions", listTransactionViewsByCategory(category));
        model.addAttribute("list_ieNoCategories", getSumsNoCategory(null, null));
        model.addAttribute("list_ieByCategories", getSumsByCategory(null, null));
        setModelParameters(model);
        return "transaction/listTransaction";
    }

    @PostMapping(value = UrlMapper.SUBMIT_TRANSACTION)
    public String doEditSaveTransaction(@RequestParam(required = false) Long transactionId, @ModelAttribute TransactionView objectTransaction) {
        objectTransaction.setId(transactionId);
        TransactionModel model = getModelFromView(objectTransaction);
        service.saveUpdate(model);

        return "redirect:" + UrlMapper.LIST_TRANSACTION;
    }

    @PostMapping(value = UrlMapper.LIST_TRANSACTION)
    public String doRemoveTransaction(@RequestParam List<String> idTransactions) {
        if(idTransactions != null){
            for(String transactionIdStr : idTransactions){
                Long transactionId = Long.parseLong(transactionIdStr);
                service.remove(transactionId);
            }
        }

        return "redirect:" + UrlMapper.LIST_TRANSACTION;
    }

    private List<TransactionView> getTransactionViewList() {
        return service.findAll().stream().map(model -> transactionConverter.toTransactionWiew(model)).collect(Collectors.toList());
    }

    private List<TransactionView> listTransactionViewsByDate(String fromDate, String upToDate) {
        LocalDateTime from = parseDate(fromDate, LocalDateTime.now().minusYears(1000));
        LocalDateTime to = parseDate(upToDate, LocalDateTime.now());
        List<TransactionView> viewList = new ArrayList<>();
        service.findByPeriod(from, to)
                .forEach(model -> viewList.add(transactionConverter.toTransactionWiew(model)));

        return viewList;
    }

    private List<TransactionView> listTransactionViewsByDateAndType(String fromDate, String upToDate, String type) {
        LocalDateTime from = parseDate(fromDate, LocalDateTime.now().minusYears(1000));
        LocalDateTime to = parseDate(upToDate, LocalDateTime.now());
        List<TransactionView> viewList = new ArrayList<>();
        service.getByPeriodAndType(from, to, type)
                .forEach(model -> viewList.add(transactionConverter.toTransactionWiew(model)));

        return viewList;
    }

    private LocalDateTime parseDate(String dateString, LocalDateTime defaultValue) {
        LocalDateTime result;
        if (dateString == null || "undefined".equals(dateString) || "".equals(dateString)) {
            result = defaultValue;
        } else {
            result = ModelUtils.parseDateTime(dateString);
        }

        return result;
    }

    private List<TransactionView> listTransactionViewsByCategory(String name) {
        List<TransactionView> viewList = new ArrayList<>();
        if (name != null) {
            service.findByCategory((categoryService.findByName(name)).orElse(null))
                    .forEach(model -> viewList.add(transactionConverter.toTransactionWiew(model)));
        }

        return viewList;
    }

    private List<CategoryTransactionView> getCategoryViewList() {
        return categoryService.findAll().stream().map(model -> categoryConverter.toCategoryView(model)).collect(Collectors.toList());
    }

    private List<AccountView> getAccountViewList() {
        return accountService.findAll().stream().map(model -> accountConverter.toAccountView(model)).collect(Collectors.toList());
    }

    private TransactionModel getModelFromView(TransactionView transactionView) {
        return transactionConverter.toTransactionModel(transactionView);
    }

    private void setModelParameters(Model model) {
        model.addAttribute("list_categories", getCategoryViewList());
        model.addAttribute("list_accounts", getAccountViewList());
        model.addAttribute("list_transactionTypes", TransactionType.values());
        model.addAttribute("objectTransaction", new TransactionView());
    }

    private Map<String, IncomeExpense> getSumsNoCategory(String dateFrom, String upToDate) {
        LocalDateTime from = parseDate(dateFrom, LocalDateTime.now().minusYears(1000));
        LocalDateTime to = parseDate(upToDate, LocalDateTime.now());
        return service.sumByPeriodNoCategories(from, to);
    }

    private Map<CategoryTransactionModel, IncomeExpense> getSumsByCategory(String dateFrom, String upToDate) {
        LocalDateTime from = parseDate(dateFrom, LocalDateTime.now().minusYears(1000));
        LocalDateTime to = parseDate(upToDate, LocalDateTime.now());
        return service.sumByPeriodByCategories(from, to);
    }
}
