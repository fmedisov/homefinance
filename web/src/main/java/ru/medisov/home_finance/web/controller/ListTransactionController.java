package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.medisov.home_finance.service.TransactionService;

import java.util.ArrayList;

@Controller
public class ListTransactionController {

    @Autowired
    TransactionService service;

    @GetMapping("/transaction/list")
    public String showListTransaction(Model model) {
        model.addAttribute("list_transactions", new ArrayList<>(service.findAll()));
        return "transaction/listTransaction";
    }
}
