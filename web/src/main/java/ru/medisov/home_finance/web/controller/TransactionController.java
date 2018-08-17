package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.medisov.home_finance.service.TransactionService;
import ru.medisov.home_finance.web.config.UrlMapper;

import java.util.ArrayList;

@Controller
//todo rename controller classes
public class TransactionController {

    @Autowired
    private TransactionService service;

    @GetMapping(UrlMapper.LIST_TRANSACTION)
    public String showListTransaction(Model model) {
        model.addAttribute("list_transactions", new ArrayList<>(service.findAll()));
        return "transaction/listTransaction";
    }
}
