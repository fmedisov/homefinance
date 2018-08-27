package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.HomeFinanceServiceException;
import ru.medisov.home_finance.web.converter.AccountModelToViewConverter;
import ru.medisov.home_finance.web.converter.AccountViewToModelConverter;
import ru.medisov.home_finance.web.converter.CurrencyModelToViewConverter;
import ru.medisov.home_finance.web.exception.HomeFinanceWebException;
import ru.medisov.home_finance.web.utils.ViewUtils;
import ru.medisov.home_finance.web.view.AccountView;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.view.CurrencyView;

import java.util.List;

@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CurrencyService currencyService;

    @GetMapping(UrlMapper.LIST_ACCOUNT)
    public String showListAccount(Model model) {
        model.addAttribute("list_accounts", listAccountViews());
        model.addAttribute("list_currencies", listCurrencyViews());
        model.addAttribute("list_accountTypes", AccountType.values());
        model.addAttribute("objectAccount", new AccountView());
        return "account/listAccount";
    }

    @PostMapping(value = UrlMapper.SUBMIT_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public String doEditSaveAccount(@RequestParam(value = "accountId") Long accountId, @ModelAttribute AccountView objectAccount) {
        objectAccount.setId(accountId);
        AccountModel model = getModelFromView(objectAccount);

        try {
            AccountModel updated = accountService.update(model);
            if (updated.getId() < 1) {
                throw new HomeFinanceWebException();
            }
        } catch (HomeFinanceServiceException | HomeFinanceWebException e) {
            try {
                accountService.save(model);
            } catch (HomeFinanceServiceException | HomeFinanceWebException e1) {
                e1.printStackTrace();
            }
        }

        return "redirect:" + UrlMapper.LIST_ACCOUNT;
    }

    @PostMapping(value = UrlMapper.LIST_ACCOUNT, produces = MediaType.APPLICATION_JSON_VALUE)
    public String doRemoveAccount(@RequestParam("idChecked") List<String> idAccounts) {
        if(idAccounts != null){
            for(String accountIdStr : idAccounts){
                Long accountId = Long.parseLong(accountIdStr);
                accountService.remove(accountId);
            }
        }

        return "redirect:" + UrlMapper.LIST_ACCOUNT;
    }

    private List<AccountView> listAccountViews() {
        return ViewUtils.listViews(accountService, new AccountModelToViewConverter());
    }

    private List<CurrencyView> listCurrencyViews() {
        return ViewUtils.listViews(currencyService, new CurrencyModelToViewConverter());
    }

    private AccountModel getModelFromView(AccountView accountView) {
        return new AccountViewToModelConverter(currencyService).convert(accountView);
    }
}
