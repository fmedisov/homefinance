package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.HomeFinanceServiceException;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.converter.CurrencyModelToViewConverter;
import ru.medisov.home_finance.web.converter.CurrencyViewToModelConverter;
import ru.medisov.home_finance.web.exception.HomeFinanceWebException;
import ru.medisov.home_finance.web.utils.ViewUtils;
import ru.medisov.home_finance.web.view.CurrencyView;

import java.util.List;

@Controller
public class CurrencyController {

    @Autowired
    private CurrencyService service;

    @GetMapping(UrlMapper.LIST_CURRENCY)
    public String showListCurrency(Model model) {
        model.addAttribute("list_currencies", currencyViewList());
        model.addAttribute("objectCurrency", new CurrencyView());

        return "currency/listCurrency";
    }

    @PostMapping(value = UrlMapper.SUBMIT_CURRENCY)
    //todo remove value from requestparam
    public String doEditSaveCurrency(@RequestParam Long currencyId, @ModelAttribute CurrencyView objectCurrency) {
        objectCurrency.setId(currencyId);
        //todo autowire converters
        CurrencyModel currencyModel = new CurrencyViewToModelConverter().convert(objectCurrency);

        try {
            CurrencyModel updated = service.update(currencyModel);
            if (updated.getId() < 1) {
                throw new HomeFinanceWebException();
            }
        } catch (HomeFinanceServiceException | HomeFinanceWebException e) {
           try {
               //todo move to services
               service.save(currencyModel);
           } catch (HomeFinanceServiceException | HomeFinanceWebException e1) {
               e1.printStackTrace();
           }
        }

        return "redirect:" + UrlMapper.LIST_CURRENCY;
    }

    //todo remove produces
    @PostMapping(value = UrlMapper.LIST_CURRENCY, produces = MediaType.APPLICATION_JSON_VALUE)
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

    //todo implement with stream
    //todo rename
    private List<CurrencyView> currencyViewList() {
        return ViewUtils.listViews(service, new CurrencyModelToViewConverter());
    }
}