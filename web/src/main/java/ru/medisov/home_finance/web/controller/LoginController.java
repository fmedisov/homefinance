package ru.medisov.home_finance.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.medisov.home_finance.web.config.UrlMapper;

@Controller
public class LoginController {

    @RequestMapping(UrlMapper.LOGIN)
    public String login() {
        return "login/login";
    }
}
