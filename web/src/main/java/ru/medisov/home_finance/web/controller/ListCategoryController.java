package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.medisov.home_finance.service.CategoryService;

import java.util.ArrayList;

@Controller
public class ListCategoryController {

    @Autowired
    CategoryService service;

    @GetMapping("/category/list")
    public String showListCategory(Model model) {
        model.addAttribute("list_categories", new ArrayList<>(service.findAll()));
        return "category/listCategory";
    }
}
