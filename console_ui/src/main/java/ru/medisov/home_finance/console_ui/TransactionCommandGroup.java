package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.dao.model.CategoryTransactionModel;
import ru.medisov.home_finance.dao.model.TransactionModel;
import ru.medisov.home_finance.service.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TransactionCommandGroup {
    private TransactionService transactionService = new TransactionServiceImpl();
    private List<CategoryTransactionModel> categories = new ArrayList<>(new CategoryServiceImpl().findAll());

    public void save() {

    }

    public void getByPeriod() {
        Command command = new CommandRequester().request(Command.CommandType.TRANSACTION_LIST);

        if (command != null) {
            switch (command) {
                case TRANSACTION_LIST_WEEK:
                    getByPeriod(ChronoUnit.WEEKS);
                    break;
                case TRANSACTION_LIST_MONTH:
                    getByPeriod(ChronoUnit.MONTHS);
                    break;
                case TRANSACTION_LIST_YEAR:
                    getByPeriod(ChronoUnit.YEARS);
                    break;
            }
        }
    }

    private void getByPeriod(ChronoUnit dateUnit) {
        long amountUnits = 1L;
        Collection<TransactionModel> transactions = transactionService.findByPeriod(LocalDateTime.now()
                                                        .minus(amountUnits, dateUnit), LocalDateTime.now());
        if (transactions.size() > 0) {
            System.out.println("Список транзакций за период:" + dateUnit.toString());
            transactions.forEach(System.out::println);
        }
    }

    public void getByCategory() {
        CategoryTransactionModel category = requestCategory();
        Collection<TransactionModel> transactions = transactionService.findByCategory(category);

        if (transactions.size() > 0) {
            System.out.println("Транзакции по категории '" + category.getName() + "': ");
            transactions.forEach(System.out::println);
        }

    }

    public void getCommonSumByPeriod() {
        ChronoUnit dateUnit = getDateUnit();
        Command variant = new CommandRequester().request(Command.CommandType.TRANSACTION_SUM_VARIANT);
        long amountUnits = 1L;
        LocalDateTime dateFrom = LocalDateTime.now().minus(amountUnits, dateUnit);
        LocalDateTime upToDate = LocalDateTime.now();

        if (variant != null) {
            switch (variant) {
                case TRANSACTION_SUM_CATEGORY:
                    Map<String, IncomeExpense> sumsByCategory = transactionService.sumByPeriod(dateFrom, upToDate, CategoryTransactionModel.class);
                    System.out.println(sumsByCategory);
                    break;
                case TRANSACTION_SUM_NO_CATEGORY:
                    Map<String, IncomeExpense> sumsNoCategory = transactionService.sumByPeriod(dateFrom, upToDate, null);
                    System.out.println(sumsNoCategory);
                    break;
            }
        }
    }

    private ChronoUnit getDateUnit() {
        Command period = new CommandRequester().request(Command.CommandType.TRANSACTION_SUM);

        if (period != null) {
            switch (period) {
                case TRANSACTION_SUM_DAY:
                    return ChronoUnit.DAYS;
                case TRANSACTION_SUM_WEEK:
                    return ChronoUnit.WEEKS;
                case TRANSACTION_SUM_MONTH:
                    return ChronoUnit.MONTHS;
                case TRANSACTION_SUM_YEAR:
                    return ChronoUnit.YEARS;
            }
        }

        return ChronoUnit.FOREVER;
    }

    private CategoryTransactionModel requestCategory() {
        int size = categories.size();

        System.out.println("Введите номер команды (Enter - для возврата к предыдущему пункту): ");

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
}
