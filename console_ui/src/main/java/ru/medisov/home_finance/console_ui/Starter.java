package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.service.ServiceInit;

public class Starter {
    public static void main(String[] args) {
        UiConfig.initConfig();
        startUi();
    }

    private static void startUi() {
        new ServiceInit().init();
        Command commandType;

        do {
            commandType = new CommandRequester().request(Command.CommandType.TYPE);

            if (commandType != Command.EXIT) {
                new CommandRunner().setCommandType(commandType).execute();
            }

        } while (commandType != Command.EXIT);
    }
}
