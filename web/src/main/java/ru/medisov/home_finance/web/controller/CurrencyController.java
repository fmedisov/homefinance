package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.HomeFinanceServiceException;
import ru.medisov.home_finance.web.config.UrlMapper;

import java.util.ArrayList;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService service;

//    @ModelAttribute("currencyForm")
//    public CurrencyModel constructCurrency() {
//        return new CurrencyModel();
//    }

    @GetMapping(UrlMapper.LIST_CURRENCY)
    public String showListCurrency(Model model) {
        model.addAttribute("list_currencies", new ArrayList<>(service.findAll()));
        model.addAttribute("objectCurrency", new CurrencyModel());

        return "currency/listCurrency";
    }

    @PostMapping(value = "/currency/submit", headers = "content-type=application/x-www-form-urlencoded", produces="text/plain;charset=UTF-8")
    public String doEditSaveCurrency(@RequestParam(value = "currencyId") Long currencyId, @ModelAttribute CurrencyModel objectCurrency) {
        objectCurrency.setId(currencyId);

        try {
           service.update(objectCurrency);
        } catch (HomeFinanceServiceException e) {
           try {
               service.save(objectCurrency);
           } catch (HomeFinanceServiceException e1) {
               e.printStackTrace();
           }
        }

        return "redirect:" + UrlMapper.LIST_CURRENCY;
    }
}
