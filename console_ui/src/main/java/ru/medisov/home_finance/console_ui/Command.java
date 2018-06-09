package ru.medisov.home_finance.console_ui;

public enum Command {
    ACCOUNT_CREATE(1, UiConfig.getProperty("command.ACCOUNT_CREATE.name"), CommandType.ACCOUNT),
    ACCOUNT_DELETE(2, UiConfig.getProperty("command.ACCOUNT_DELETE.name"), CommandType.ACCOUNT),
    ACCOUNT_EDIT(3, UiConfig.getProperty("command.ACCOUNT_EDIT.name"), CommandType.ACCOUNT),
    ACCOUNT_GET(4, UiConfig.getProperty("command.ACCOUNT_GET.name"), CommandType.ACCOUNT),

    CURRENCY_CREATE(1, UiConfig.getProperty("command.CURRENCY_CREATE.name"), CommandType.CURRENCY),
    CURRENCY_EDIT(2, UiConfig.getProperty("command.CURRENCY_EDIT.name"), CommandType.CURRENCY),
    CURRENCY_DELETE(3, UiConfig.getProperty("command.CURRENCY_DELETE.name"), CommandType.CURRENCY),
    CURRENCY_GET(4, UiConfig.getProperty("command.CURRENCY_GET.name"), CommandType.CURRENCY),
    CURRENCY_PAIR(5, UiConfig.getProperty("command.CURRENCY_PAIR.name"), CommandType.CURRENCY),

    TRANSACTION_CREATE(1, UiConfig.getProperty("command.TRANSACTION_CREATE.name"), CommandType.TRANSACTION),
    TRANSACTION_BY_PERIOD(2, UiConfig.getProperty("command.TRANSACTION_BY_PERIOD.name"), CommandType.TRANSACTION),
    TRANSACTION_BY_CATEGORY(3, UiConfig.getProperty("command.TRANSACTION_BY_CATEGORY.name"), CommandType.TRANSACTION),
    TRANSACTION_COMMON_SUM(4, UiConfig.getProperty("command.TRANSACTION_COMMON_SUM.name"), CommandType.TRANSACTION),

    ACCOUNT_TYPE_DEBIT(1, UiConfig.getProperty("command.ACCOUNT_TYPE_DEBIT.name"), CommandType.ACCOUNT_TYPE),

    EXIT(0, UiConfig.getProperty("command.EXIT.name"), CommandType.TYPE),
    ACCOUNT(1, UiConfig.getProperty("command.ACCOUNT.name"), CommandType.TYPE),
    CURRENCY(2, UiConfig.getProperty("command.CURRENCY.name"), CommandType.TYPE),
    TRANSACTION(3, UiConfig.getProperty("command.TRANSACTION.name"), CommandType.TYPE),
    ACCOUNT_TYPE(4, UiConfig.getProperty("command.ACCOUNT_TYPE.name"), CommandType.TYPE);

    public enum CommandType { ACCOUNT, CURRENCY, TRANSACTION, ACCOUNT_TYPE, TYPE }

    private final int number;
    private final String name;
    private final CommandType commandType;

    Command(int number, String name, CommandType commandType) {

        this.number = number;
        this.name = name;
        this.commandType = commandType;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    @Override
    public String toString() {
        return number + " - " + name;
    }

    public static Command find(int number, CommandType commandType) {
        for (Command command : values()) {
            if (command.getNumber() == number && command.getCommandType() == commandType) {
                return command;
            }
        }
        return null;
    }

    public static void printCommands(CommandType commandType) {
        for (Command command : values()) {
            if (command.getCommandType() == commandType) {
                System.out.println(command.toString());
            }
        }
    }
}