package ru.medisov.home_finance.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.HomeFinanceServiceException;
import ru.medisov.home_finance.web.view.CategoryTransactionView;

@Component
public class CategoryConverter {

    private final CategoryService categoryService;

    @Autowired
    public CategoryConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public CategoryTransactionView toCategoryView(CategoryTransactionModel categoryModel) {
        CategoryTransactionView categoryView = new CategoryTransactionView();
        categoryView
                .setId(categoryModel.getId())
                .setName(categoryModel.getName());

        if (categoryModel.getParent() != null) {
            String parent = categoryModel.getParent().getName();
            categoryView.setParent(parent);
            categoryView.setParentView(toCategoryView(categoryModel.getParent()));
        }

        return categoryView;
    }

    public CategoryTransactionModel toCategoryModel(CategoryTransactionView categoryView) {
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
