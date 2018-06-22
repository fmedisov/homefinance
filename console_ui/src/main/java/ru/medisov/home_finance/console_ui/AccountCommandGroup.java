package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.AccountServiceImpl;

import java.math.BigDecimal;
import java.util.*;

public class AccountCommandGroup implements CommandGroup<AccountModel> {
    private Scanner scanner = new Scanner(System.in);
    private AccountService accountService = new AccountServiceImpl();
    private List<AccountModel> accounts = new ArrayList<>(accountService.findAll());
    private Map<Integer, String> accountParameters = getAccountParameters();

    @Override
    public AccountModel save() {
        return null;
//        AccountModel account = new AccountModel();
//        account.setName(editName(account));
//        String name = requestName();
//        AccountType type = requestAccountType();
//        BigDecimal amount = requestAmount();
//        CurrencyModel currency = requestCurrency();
//
//        AccountModel account = new AccountModel().setName(name).setAccountType(type)
//                                                .setAmount(amount).setCurrencyModel(currency);
//
//        return accountService.save(account);
    }

    @Override
    public AccountModel update() {
        return null;
//        AccountModel account = requestAccount();
//        System.out.println("Enter your currency information. e. g. [Russian ruble:RUB:\u20BD]");
//        String currencyParams = scanner.nextLine();
//
//        AccountModel newAccount = new AccountModel().setName(name).setAccountType(type)
//                .setAmount(amount).setCurrencyModel(currency);
    }

    @Override
    public Optional<AccountModel> remove() {
        return null;
    }

    @Override
    public AccountModel find() {
        return null;
    }

    public void findAll() {
        System.out.println("List of accounts");
        Collection<AccountModel> byName = accountService.findAll();
        byName.forEach(System.out::println);
    }

    private String editName(AccountModel account) {
        if (account.getName() != null) {

        }
        System.out.println("Enter account name");
        return scanner.nextLine();
    }

    private CurrencyModel requestCurrency() {
        return new CurrencyCommandGroup().requestCurrency();
    }

    private BigDecimal requestAmount() {
        System.out.println("Enter account start balance: ");
        BigDecimal amount = scanner.nextBigDecimal();
        return getBaseAmount().add(amount);
    }

    public BigDecimal getBaseAmount() {
        return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_CEILING);
    }

    private AccountType requestAccountType() {
        System.out.println("enter account type: ");
        AccountType.print();
        Scanner in = new Scanner(System.in);

        while (true) {
            String userChoice = in.nextLine();

            if (userChoice.length() == 0) {
                break;  // return to the top menu
            }

            try {
                int categoryNum = Integer.parseInt(userChoice);

                return AccountType.findByNum(categoryNum).orElse(AccountType.CASH); //todo
            } catch (Exception e) {
                System.out.println("Введите номер команды (Enter - для возврата к предыдущему пункту): ");
            }
        }

        return AccountType.CASH;
    }

    public AccountModel requestAccount() {
        int size = accounts.size();

        System.out.println("enter number of currency: ");

        for (int i = 0; i < size; i++) {
            System.out.println(i + " - " + accounts.get(i).getName());
        }

        Scanner in = new Scanner(System.in);

        while (true) {
            String userChoice = in.nextLine();

            if (userChoice.length() == 0) {
                break;  // return to the top menu
            }

            try {
                int categoryNum = Integer.parseInt(userChoice);

                return accounts.get(categoryNum);
            } catch (Exception e) {
                System.out.println("Введите номер команды (Enter - для возврата к предыдущему пункту): ");
            }
        }

        return new AccountModel();
    }

    public Map<Integer, String> getAccountParameters() {
        Map<Integer, String> accountParameters = new HashMap<>();
        accountParameters.put(1, "Name");
        accountParameters.put(2, "Account type");
        accountParameters.put(3, "Currency");
        accountParameters.put(4, "Amount");

        return accountParameters;
    }

    public int requestParameter() {
        int paramNum = 0;
        int size = accountParameters.size();

        System.out.println("enter number of parameter: ");

        for (int i = 0; i < size; i++) {
            System.out.println(i + " - " + accountParameters.get(i));
        }

        Scanner in = new Scanner(System.in);

        while (true) {
            String userChoice = in.nextLine();

            if (userChoice.length() == 0) {
                break;  // return to the top menu
            }

            try {
                paramNum = Integer.parseInt(userChoice);

                return paramNum;
            } catch (Exception e) {
                System.out.println("enter number of parameter): ");
            }
        }

        return paramNum;
    }
}