package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.web.converter.AccountConverter;
import ru.medisov.home_finance.web.converter.CurrencyConverter;
import ru.medisov.home_finance.web.view.AccountView;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.view.CurrencyView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AccountController {

    @Autowired
    private AccountService service;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private AccountConverter accountConverter;

    @Autowired
    private CurrencyConverter currencyConverter;

    @GetMapping(UrlMapper.LIST_ACCOUNT)
    public String showListAccount(Model model) {
        model.addAttribute("list_accounts", getAccountViewList());
        model.addAttribute("list_currencies", getCurrencyViewList());
        model.addAttribute("list_accountTypes", AccountType.values());
        model.addAttribute("objectAccount", new AccountView());
        return "account/listAccount";
    }

    @PostMapping(value = UrlMapper.SUBMIT_ACCOUNT)
    public String doEditSaveAccount(@RequestParam(required = false) Long accountId, @ModelAttribute AccountView objectAccount) {
        objectAccount.setId(accountId);
        AccountModel model = getModelFromView(objectAccount);
        service.saveUpdate(model);

        return "redirect:" + UrlMapper.LIST_ACCOUNT;
    }

    @PostMapping(value = UrlMapper.LIST_ACCOUNT)
    public String doRemoveAccount(@RequestParam List<String> idAccounts) {
        if(idAccounts != null){
            for(String accountIdStr : idAccounts){
                Long accountId = Long.parseLong(accountIdStr);
                service.remove(accountId);
            }
        }

        return "redirect:" + UrlMapper.LIST_ACCOUNT;
    }

    private List<AccountView> getAccountViewList() {
        return service.findAllByCurrentUser().stream().map(model -> accountConverter.toAccountView(model)).collect(Collectors.toList());
    }

    private List<CurrencyView> getCurrencyViewList() {
        return currencyService.findAllByCurrentUser().stream().map(model -> currencyConverter.toCurrencyView(model)).collect(Collectors.toList());
    }

    private AccountModel getModelFromView(AccountView accountView) {
        return accountConverter.toAccountModel(accountView);
    }
}
