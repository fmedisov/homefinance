package ru.medisov.home_finance.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.HomeFinanceServiceException;
import ru.medisov.home_finance.web.view.CategoryTransactionView;

public class CategoryViewToModelConverter implements Converter<CategoryTransactionView, CategoryTransactionModel> {

    private final CategoryService categoryService;

    @Autowired
    //todo implement without constructor
    public CategoryViewToModelConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryTransactionModel convert(CategoryTransactionView categoryView) {
        CategoryTransactionModel categoryModel = new CategoryTransactionModel();
        categoryModel
                .setId(categoryView.getId())
                .setName(categoryView.getName())
                .setParent(getParent(categoryView));

        return categoryModel;
    }

    private CategoryTransactionModel getParent(CategoryTransactionView categoryView) {
        CategoryTransactionModel defaultValue = null;
        try {
            return categoryService.findByName(categoryView.getParent()).orElse(defaultValue);
        } catch (HomeFinanceServiceException e) {
            return defaultValue;
        }
    }
}