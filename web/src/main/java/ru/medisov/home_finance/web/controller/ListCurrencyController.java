package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.medisov.home_finance.service.CurrencyService;

import java.util.ArrayList;

@Controller
public class ListCurrencyController {

    @Autowired
    CurrencyService service;

    @GetMapping("/currency/list")
    public String showListCurrency(Model model) {
        model.addAttribute("list_currencies", new ArrayList<>(service.findAll()));
        return "currency/listCurrency";
    }
}
