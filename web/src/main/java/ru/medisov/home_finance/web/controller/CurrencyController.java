package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.converter.CurrencyConverter;
import ru.medisov.home_finance.web.view.CurrencyView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService service;

    @GetMapping(UrlMapper.LIST_CURRENCY)
    public String showListCurrency(Model model) {
        model.addAttribute("list_currencies", getCurrencyViewList());
        model.addAttribute("objectCurrency", new CurrencyView());

        return "currency/listCurrency";
    }

    @PostMapping(value = UrlMapper.SUBMIT_CURRENCY)
    public String doEditSaveCurrency(@RequestParam Long currencyId, @ModelAttribute CurrencyView objectCurrency) {
        objectCurrency.setId(currencyId);
        //todo autowire converters
        CurrencyModel currencyModel = new CurrencyConverter().toCurrencyModel(objectCurrency);
        service.saveUpdate(currencyModel);

        return "redirect:" + UrlMapper.LIST_CURRENCY;
    }

    @PostMapping(value = UrlMapper.LIST_CURRENCY)
    public String doRemoveCurrency(@RequestParam List<String> idCurrencies) {
        if(idCurrencies != null){
            for(String currencyIdStr : idCurrencies){
                Long currencyId = Long.parseLong(currencyIdStr);
                //todo implement method for remove list models
                service.remove(currencyId);
            }
        }

        return "redirect:" + UrlMapper.LIST_CURRENCY;
    }

    private List<CurrencyView> getCurrencyViewList() {
        return service.findAll().stream().map(model -> new CurrencyConverter().toCurrencyView(model)).collect(Collectors.toList());
    }
}
