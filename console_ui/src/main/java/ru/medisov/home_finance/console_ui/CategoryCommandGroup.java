package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.console_ui.exception.HomeFinanceUIException;
import ru.medisov.home_finance.service.CategoryService;
import ru.medisov.home_finance.service.CategoryServiceImpl;

import java.util.*;

public class CategoryCommandGroup implements CommandGroup<CategoryTransactionModel> {
    private Scanner scanner = new Scanner(System.in);
    private CategoryService categoryService = new CategoryServiceImpl();

    @Override
    public CategoryTransactionModel save() {
        System.out.println("New category. Enter your category information. ");
        final CategoryTransactionModel newCategory = new CategoryTransactionModel();
        setCategoryName(newCategory);
        setCategoryParent(newCategory);
        CategoryTransactionModel savedModel = categoryService.save(newCategory);
        System.out.println("Saved model: " + savedModel);
        return savedModel;
    }

    @Override
    public CategoryTransactionModel update() {
        System.out.println("Enter category for update:");
        final CategoryTransactionModel category = requestCategory();
        setCategoryName(category);
        setCategoryParent(category);
        CategoryTransactionModel updatedModel = categoryService.update(category);
        System.out.println("Updated model: " + updatedModel);
        return updatedModel;
    }

    @Override
    public Optional<CategoryTransactionModel> remove() {
        CategoryTransactionModel category = requestCategory();
        Optional<CategoryTransactionModel> optionalModel = Optional.empty();

        if (categoryService.remove(category.getId())) {
            optionalModel = Optional.of(category);
        }

        return optionalModel;
    }

    @Override
    public CategoryTransactionModel find() {
        System.out.println("Find category. Enter your category id");
        String userChoice = scanner.nextLine();
        Long id = Long.parseLong(userChoice);
        Optional<CategoryTransactionModel> byId = categoryService.findById(id);
        CategoryTransactionModel found = byId.orElseThrow(HomeFinanceUIException::new);
        System.out.println("Выбрана категория - " + found);
        return found;
    }

    public void findAll() {
        System.out.println("List of categories");
        Collection<CategoryTransactionModel> byName = categoryService.findAll();
        byName.forEach(System.out::println);
    }

    public CategoryTransactionModel requestCategory() {
        final List<CategoryTransactionModel> categories = new ArrayList<>(categoryService.findAll());
        int size = categories.size();

        System.out.println("enter number of category: ");

        for (int i = 0; i < size; i++) {
            System.out.println(i + " - " + categories.get(i).getName());
        }

        Scanner in = new Scanner(System.in);

        while (true) {
            String userChoice = in.nextLine();

            if (userChoice.length() == 0) {
                break;  // return to the top menu
            }

            try {
                int categoryNum = Integer.parseInt(userChoice);

                return categories.get(categoryNum);
            } catch (Exception e) {
                System.out.println("Введите номер команды (Enter - для возврата к предыдущему пункту): ");
            }
        }

        return new CategoryTransactionModel();
    }

    private void setCategoryName(CategoryTransactionModel category) {
        System.out.println("Enter category name: ");
        String currencyContent = scanner.nextLine();
        category.setName(currencyContent);
    }

    private void setCategoryParent(CategoryTransactionModel category) {
        System.out.println("Enter parent category number: ");
        final CategoryTransactionModel parent = requestCategory();
        category.setParent(parent);
    }
}