package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.converter.CategoryConverter;
import ru.medisov.home_finance.web.view.CategoryTransactionView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @Autowired
    private CategoryConverter categoryConverter;

    @GetMapping(UrlMapper.LIST_CATEGORY)
    public String showListCategory(Model model) {
        model.addAttribute("list_categories", getCategoryViewList());
        model.addAttribute("objectCategory", new CategoryTransactionView());
        return "category/listCategory";
    }

    @PostMapping(value = UrlMapper.SUBMIT_CATEGORY)
    public String doEditSaveCategory(@RequestParam(required = false) Long categoryId, @ModelAttribute CategoryTransactionView objectCategory) {
        objectCategory.setId(categoryId);
        CategoryTransactionModel categoryModel = categoryConverter.toCategoryModel(objectCategory);
        service.saveUpdate(categoryModel);

        return "redirect:" + UrlMapper.LIST_CATEGORY;
    }

    @PostMapping(value = UrlMapper.LIST_CATEGORY)
    public String doRemoveCategory(@RequestParam List<String> idCategories) {
        if(idCategories != null){
            for(String categoryIdStr : idCategories){
                Long categoryId = Long.parseLong(categoryIdStr);
                service.remove(categoryId);
            }
        }

        return "redirect:" + UrlMapper.LIST_CATEGORY;
    }

    private List<CategoryTransactionView> getCategoryViewList() {
        return service.findAllByCurrentUser().stream().map(model -> categoryConverter.toCategoryView(model)).collect(Collectors.toList());
    }
}
