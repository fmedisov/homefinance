package ru.medisov.home_finance.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.medisov.home_finance.web.config.UrlMapper;

@Controller
public class UserController {

    @GetMapping("/")
    public String welcome() {
        return "redirect:" + UrlMapper.LIST_TRANSACTION;
    }
}