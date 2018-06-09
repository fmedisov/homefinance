package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.dao.DaoConfig;

public class Starter {
    public static void main(String[] args) {
        DaoConfig.initGlobalConfig();
        UiConfig.initGlobalConfig();
        startUi();
    }

    private static void startUi() {
        Command commandType;

        do {
            commandType = new CommandRequester().request(Command.CommandType.TYPE);

            if (commandType != Command.EXIT) {
                new CommandRunner().setCommandType(commandType).execute();
            }

        } while (commandType != Command.EXIT);
    }
}
