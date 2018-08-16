package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.medisov.home_finance.service.AccountService;

import java.util.ArrayList;

@Controller
public class ListAccountController {

    @Autowired
    AccountService service;

    @GetMapping("/account/list")
    public String showListAccount(Model model) {
        model.addAttribute("list_accounts", new ArrayList<>(service.findAll()));
        return "account/listAccount";
    }
}
