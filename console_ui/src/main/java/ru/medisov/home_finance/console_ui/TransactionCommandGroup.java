package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.common.model.CategoryTransactionModel;
import ru.medisov.home_finance.common.model.TagModel;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.common.model.TransactionType;
import ru.medisov.home_finance.common.utils.MoneyUtils;
import ru.medisov.home_finance.service.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TransactionCommandGroup {
    private Scanner scanner = new Scanner(System.in);
    private TransactionService transactionService = new TransactionServiceImpl();
    private List<CategoryTransactionModel> categories = new ArrayList<>(new CategoryServiceImpl().findAll());

    public TransactionModel createTransaction() {
        System.out.println("New transaction. Enter your transaction information. ");
        final TransactionModel newTransaction = new TransactionModel();
        setParameters(newTransaction);
        TransactionModel savedModel = transactionService.save(newTransaction);
        System.out.println("Saved model: " + savedModel);
        return savedModel;
    }

    public TransactionModel editTransaction() {
        final TransactionModel transaction = requestTransaction();
        System.out.println("Edit transaction information.");
        setParameters(transaction);
        TransactionModel updatedModel = transactionService.update(transaction);
        System.out.println("Updated model: " + updatedModel);
        return updatedModel;
    }

    public Optional<TransactionModel> removeTransaction() {
        TransactionModel transaction = requestTransaction();
        Optional<TransactionModel> optionalModel = Optional.empty();

        if (transactionService.remove(transaction.getId())) {
            optionalModel = Optional.of(transaction);
        }

        return optionalModel;
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
                case TRANSACTION_LIST:
                    getByPeriod(ChronoUnit.MILLENNIA);
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
                    Map<CategoryTransactionModel, IncomeExpense> sumsByCategory = transactionService.sumByPeriodByCategories(dateFrom, upToDate);
                    System.out.println(sumsByCategory);
                    break;
                case TRANSACTION_SUM_NO_CATEGORY:
                    Map<String, IncomeExpense> sumsNoCategory = transactionService.sumByPeriodNoCategories(dateFrom, upToDate);
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

    private TransactionModel requestTransaction() {
        final List<TransactionModel> transactions = new ArrayList<>(transactionService.findAll());
        int size = transactions.size();

        System.out.println("enter number of transaction: ");

        for (int i = 0; i < size; i++) {
            System.out.println(i + " - " + transactions.get(i));
        }

        Scanner in = new Scanner(System.in);

        while (true) {
            String userChoice = in.nextLine();

            if (userChoice.length() == 0) {
                break;  // return to the top menu
            }

            try {
                int transactionNum = Integer.parseInt(userChoice);

                return transactions.get(transactionNum);
            } catch (Exception e) {
                System.out.println("Введите номер команды (Enter - для возврата к предыдущему пункту): ");
            }
        }

        return new TransactionModel();
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

    // todo add the necessary logic
    private TransactionType requestTransactionType() {
        System.out.println("enter transaction type: ");
        TransactionType.print();
        Scanner in = new Scanner(System.in);

        while (true) {
            String userChoice = in.nextLine();

            if (userChoice.length() == 0) {
                break;  // return to the top menu
            }

            try {
                int categoryNum = Integer.parseInt(userChoice);

                return TransactionType.findByNum(categoryNum).orElse(TransactionType.INCOME); //todo
            } catch (Exception e) {
                System.out.println("Введите номер команды (Enter - для возврата к предыдущему пункту): ");
            }
        }

        return TransactionType.INCOME;
    }

    private void setParameters(TransactionModel transaction) {
        setTransactionName(transaction);
        setAmount(transaction);
        setDateTime(transaction);
        setAccount(transaction);
        setCategory(transaction);
        setTransactionType(transaction);
        setTags(transaction);
    }

    // todo remove duplicate tags in tag_tbl
    private void setTags(TransactionModel transaction) {
        if (isNotChange(transaction.getTags(), "Tags")) {
            return;
        }

        System.out.println("Enter tags separated by colon (transport:car:gasoline) ");
        String tagsString = scanner.nextLine();
        transaction.setTags(parseTags(tagsString));
    }

    private void setTransactionType(TransactionModel transaction) {
        if (isNotChange(transaction.getTransactionType(), "Transaction type")) {
            return;
        }

        System.out.println("Enter transaction type: ");
        transaction.setTransactionType(requestTransactionType());
    }

    private void setCategory(TransactionModel transaction) {
        if (isNotChange(transaction.getCategory(), "Category")) {
            return;
        }

        System.out.println("Enter transaction category: ");
        transaction.setCategory(requestCategory());
    }

    private void setAccount(TransactionModel transaction) {
        if (isNotChange(transaction.getAccount(), "Account")) {
            return;
        }

        System.out.println("Enter transaction account: ");
        transaction.setAccount(new AccountCommandGroup().requestAccount());
    }

    private void setDateTime(TransactionModel transaction) {
        if (isNotChange(transaction.getDateTime(), "DateTime")) {
            return;
        }

        System.out.println("Enter transaction date (2017-12-31): ");
        String dateString = scanner.nextLine();
        transaction.setDateTime(parseDateTime(dateString));
    }

    private void setAmount(TransactionModel transaction) {
        if (isNotChange(transaction.getAmount(), "Amount")) {
            return;
        }

        System.out.println("Enter transaction amount: ");
        String amountString = scanner.nextLine();

        transaction.setAmount(MoneyUtils.inBigDecimal(amountString));
    }

    private void setTransactionName(TransactionModel transaction) {
        if (isNotChange(transaction.getName(), "Name")) {
            return;
        }

        System.out.println("Enter transaction name: ");
        String name = scanner.nextLine();
        transaction.setName(name);
    }

    private boolean isNotChange(Object parameter, String parameterName) {
        boolean isNotChange = false;

        if (parameter != null) {
            System.out.println(parameterName + " must be changed. y / n (yes/no)");
            String userChoice = scanner.nextLine();

            if ("n".equals(userChoice)) {
                isNotChange = true;
            }
        }

        return isNotChange;
    }

    private List<TagModel> parseTags(String tagsString) {
        List<TagModel> result = new ArrayList<>();

        if (tagsString == null || tagsString.length() < 2) {
            return result;
        }

        List<String> listOfTagStrings = Arrays.asList(tagsString.split(":"));
        listOfTagStrings.forEach(s -> result.add(new TagModel().setName(s)));

        return result;
    }

    // from format '2017-12-31'
    private LocalDateTime parseDateTime(String dateString) {
        return LocalDateTime.parse(dateString + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
