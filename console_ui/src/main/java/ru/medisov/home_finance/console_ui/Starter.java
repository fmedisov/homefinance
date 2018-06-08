package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.dao.DaoConfig;
import ru.medisov.home_finance.dao.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.CurrencyServiceImpl;

import java.util.Optional;
import java.util.Scanner;

import static ru.medisov.home_finance.console_ui.Command.EXIT;

public class Starter {
    public static void main(String[] args) {
        DaoConfig.initGlobalConfig();
        startUi();
    }

    private static void startUi() {
        boolean isEnable = true;

        while (isEnable) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Select the command \n" +
                    Command.SAVE_CURRENCY.name() + "\n" +
                    Command.GET_CURRENCY.name() + "\n" +
                    EXIT.name());

            String scannerText = scanner.nextLine();
            Command command = Command.valueOf(scannerText);

            CurrencyService currencyService = new CurrencyServiceImpl();

            if (EXIT.compareTo(command) == 0) {
                isEnable = false;
            }

            switch (command) {
                case GET_CURRENCY:
                    System.out.println("Enter your currency name. e. g. [Russian ruble]");
                    String name = scanner.nextLine();
                    Optional<CurrencyModel> byName = currencyService.findByName(name);
                    System.out.println(byName.get());

                    break;
                case SAVE_CURRENCY:
                    System.out.println("Enter your currency information. e. g. [Russian ruble:RUB:\u20BD]");
                    String next = scanner.nextLine();
                    String[] currencyContent = next.split(":");
                    CurrencyModel currencyModel = new CurrencyModel().setName(currencyContent[0])
                            .setCode(currencyContent[1]).setSymbol(currencyContent[2]);
                    currencyService.save(currencyModel);

                    break;
                default:
                    System.exit(0);
            }
        }
    }
}
