package ru.medisov.home_finance.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.HomeFinanceServiceException;
import ru.medisov.home_finance.web.config.UrlMapper;
import ru.medisov.home_finance.web.converter.CategoryModelToViewConverter;
import ru.medisov.home_finance.web.converter.CategoryViewToModelConverter;
import ru.medisov.home_finance.web.exception.HomeFinanceWebException;
import ru.medisov.home_finance.web.utils.ViewUtils;
import ru.medisov.home_finance.web.view.CategoryTransactionView;

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping(UrlMapper.LIST_CATEGORY)
    public String showListCategory(Model model) {
        model.addAttribute("list_categories", listCategoryViews());
        model.addAttribute("objectCategory", new CategoryTransactionView());
        return "category/listCategory";
    }

    @PostMapping(value = UrlMapper.SUBMIT_CATEGORY)
    public String doEditSaveCategory(@RequestParam(value = "categoryId") Long categoryId, @ModelAttribute CategoryTransactionView objectCategory) {
        objectCategory.setId(categoryId);
        CategoryTransactionModel categoryModel = new CategoryViewToModelConverter(service).convert(objectCategory);

        try {
            CategoryTransactionModel updated = service.update(categoryModel);
            if (updated.getId() < 1) {
                throw new HomeFinanceWebException();
            }
        } catch (HomeFinanceServiceException | HomeFinanceWebException e) {
            try {
                service.save(categoryModel);
            } catch (HomeFinanceServiceException | HomeFinanceWebException e1) {
                e1.printStackTrace();
            }
        }

        return "redirect:" + UrlMapper.LIST_CATEGORY;
    }

    @PostMapping(value = UrlMapper.LIST_CATEGORY, produces = MediaType.APPLICATION_JSON_VALUE)
    public String doRemoveCategory(@RequestParam("idChecked") List<String> idCategories) {
        if(idCategories != null){
            for(String categoryIdStr : idCategories){
                Long categoryId = Long.parseLong(categoryIdStr);
                service.remove(categoryId);
            }
        }

        return "redirect:" + UrlMapper.LIST_CATEGORY;
    }

    private List<CategoryTransactionView> listCategoryViews() {
        return ViewUtils.listViews(service, new CategoryModelToViewConverter());
    }
}
