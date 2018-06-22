package ru.medisov.home_finance.console_ui;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public final class CommandRunner {
    private Command command;
    private Command commandType;

    public void execute() {
        if (commandType == Command.ACCOUNT) {
            executeByAccount();
        } else if (commandType == Command.CURRENCY) {
            executeByCurrency();
        } else if (commandType == Command.TRANSACTION) {
            executeByTransaction();
        } else if (commandType == Command.ACCOUNT_TYPE) {
            executeByAccountType();
        }
    }

    private void executeByAccount() {
        command = new CommandRequester().request(Command.CommandType.ACCOUNT);

        if (command != null) {
            AccountCommandGroup accountCommands = new AccountCommandGroup();
            switch (command) {
                case ACCOUNT_CREATE:
                    accountCommands.save();
                    break;
                case ACCOUNT_DELETE:
                    accountCommands.remove();
                    break;
                case ACCOUNT_EDIT:
                    accountCommands.update();
                    break;
                case ACCOUNT_GET:
                    accountCommands.find();
                    break;
                case ACCOUNT_LIST:
                    accountCommands.findAll();
                    break;
            }
        }
    }

    private void executeByCurrency() {
        command = new CommandRequester().request(Command.CommandType.CURRENCY);

        if (command != null) {
            CurrencyCommandGroup currencyCommands = new CurrencyCommandGroup();
            switch (command) {
                case CURRENCY_CREATE:
                    currencyCommands.save();
                    break;
                case CURRENCY_EDIT:
                    currencyCommands.update();
                    break;
                case CURRENCY_DELETE:
                    currencyCommands.remove();
                    break;
                case CURRENCY_GET:
                    currencyCommands.find();
                    break;
                case CURRENCY_PAIR:
                    currencyCommands.getPairInfo();
                    break;
                case CURRENCY_LIST:
                    currencyCommands.findAll();
                    break;
            }
        }
    }

    private void executeByTransaction() {
        command = new CommandRequester().request(Command.CommandType.TRANSACTION);

        if (command != null) {
            TransactionCommandGroup transactionCommands = new TransactionCommandGroup();
            switch (command) {
                case TRANSACTION_CREATE:
                    transactionCommands.save();
                    break;
                case TRANSACTION_BY_PERIOD:
                    transactionCommands.getByPeriod();
                    break;
                case TRANSACTION_BY_CATEGORY:
                    transactionCommands.getByCategory();
                    break;
                case TRANSACTION_COMMON_SUM:
                    transactionCommands.getCommonSumByPeriod();
                    break;
            }
        }
    }

    private void executeByAccountType() {
        command = new CommandRequester().request(Command.CommandType.ACCOUNT_TYPE);

        if (command != null) {
            AccountTypeCommandGroup accountTypeCommands = new AccountTypeCommandGroup();
            switch (command) {
                case ACCOUNT_TYPE_DEBIT:
                    accountTypeCommands.debit();
                    break;
            }
        }
    }
}