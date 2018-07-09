package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.common.model.AccountModel;
import ru.medisov.home_finance.common.model.AccountType;
import ru.medisov.home_finance.common.utils.MoneyUtils;
import ru.medisov.home_finance.console_ui.exception.HomeFinanceUIException;
import ru.medisov.home_finance.service.AccountService;
import ru.medisov.home_finance.service.AccountServiceImpl;

import java.util.*;

public class AccountCommandGroup implements CommandGroup<AccountModel> {
    private Scanner scanner = new Scanner(System.in);
    private AccountService accountService = new AccountServiceImpl();

    @Override
    public AccountModel save() {
        System.out.println("New account. Enter your account information. ");
        final AccountModel newAccount = new AccountModel();
        setParameters(newAccount);
        AccountModel savedModel = accountService.save(newAccount);
        System.out.println("Saved model: " + savedModel);
        return savedModel;
    }

    @Override
    public AccountModel update() {
        final AccountModel account = requestAccount();
        System.out.println("Edit account information.");
        setParameters(account);
        AccountModel updatedModel = accountService.update(account);
        System.out.println("Updated model: " + updatedModel);
        return updatedModel;
    }

    @Override
    public Optional<AccountModel> remove() {
        AccountModel account = requestAccount();
        Optional<AccountModel> optionalModel = Optional.empty();

        if (accountService.remove(account.getId())) {
            optionalModel = Optional.of(account);
        }

        return optionalModel;
    }

    @Override
    public AccountModel find() {
        System.out.println("Find account. Enter your account id");
        String userChoice = scanner.nextLine();
        Long id = Long.parseLong(userChoice);
        Optional<AccountModel> byId = accountService.findById(id);
        final AccountModel found = byId.orElseThrow(HomeFinanceUIException::new);
        System.out.println("Выбран счет - " + found);
        return found;
    }

    public void findAll() {
        System.out.println("List of accounts");
        Collection<AccountModel> byName = accountService.findAll();
        byName.forEach(System.out::println);
    }

    private void setParameters(AccountModel account) {
        setAccountName(account);
        setCurrency(account);
        setAmount(account);
        setAccountType(account);
    }

    private void setAccountName(AccountModel account) {
        if (isNotChange(account.getName(), "Name")) {
            return;
        }

        System.out.println("Enter account name: ");
        String name = scanner.nextLine();
        account.setName(name);
    }

    private void setCurrency(AccountModel account) {
        if (isNotChange(account.getCurrencyModel(), "Currency")) {
            return;
        }

        System.out.println("Enter account currency: ");
        account.setCurrencyModel(new CurrencyCommandGroup().requestCurrency());
    }

    private void setAmount(AccountModel account) {
        if (isNotChange(account.getAmount(), "Amount")) {
            return;
        }

        System.out.println("Enter account start balance: ");
        String amountString = scanner.nextLine();

        account.setAmount(MoneyUtils.inBigDecimal(amountString));
    }

    private void setAccountType(AccountModel account) {
        if (isNotChange(account.getAccountType(), "AccountType")) {
            return;
        }

        System.out.println("Enter account type: ");
        AccountType accountType = requestAccountType();
        account.setAccountType(accountType);
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
        final List<AccountModel> accounts = new ArrayList<>(accountService.findAll());
        int size = accounts.size();

        System.out.println("enter number of account: ");

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
}