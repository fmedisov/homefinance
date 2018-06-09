package ru.medisov.home_finance.console_ui;

import java.util.Scanner;

public final class CommandRequester implements UiRequester<Command, Command.CommandType> {

    @Override
    public Command request(Command.CommandType commandType) {
        System.out.println("");
        Command.printCommands(commandType);
        Command command = null;
        Scanner in = new Scanner(System.in);
        while (command == null) {
            System.out.println(getMessage(commandType));
            try {
                String userChoice = in.nextLine();

                if (userChoice.length() == 0) {
                    break;  // return to query for command types
                }

                int operationNum = Integer.parseInt(userChoice);
                command = Command.find(operationNum, commandType);

                if (command == null) {
                    throw new RuntimeException();
                }

                return command;

            } catch (RuntimeException e) {
                System.out.println("Возникла ошибка при считывании номера команды");
                System.out.println("Выберите номер допустимой команды");
            }
        }

        return null;
    }

    private String getMessage(Command.CommandType commandType) {
        if (commandType == Command.CommandType.TYPE) {
            return "\nВведите номер типа команды: ";
        }
        else {
            return "\nВведите номер команды (Enter - для возврата к предыдущему пункту): ";
        }
    }
}
