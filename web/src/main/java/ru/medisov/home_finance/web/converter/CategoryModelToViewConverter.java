package ru.medisov.home_finance.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.web.view.CategoryTransactionView;

public class CategoryModelToViewConverter implements Converter<CategoryTransactionModel, CategoryTransactionView> {

    public CategoryTransactionView convert(CategoryTransactionModel categoryModel) {
        CategoryTransactionView categoryView = new CategoryTransactionView();
        categoryView
                .setId(categoryModel.getId())
                .setName(categoryModel.getName());

        if (categoryModel.getParent() != null) {
            String parent = categoryModel.getParent().getName();
            categoryView.setParent(parent);
            categoryView.setParentView(convert(categoryModel.getParent()));
        }

        return categoryView;
    }
}