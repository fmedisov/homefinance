package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.dao.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.CurrencyServiceImpl;

import java.util.Optional;
import java.util.Scanner;

public class CurrencyCommandGroup implements CommandGroup {
    private Scanner scanner = new Scanner(System.in);
    private CurrencyService currencyService = new CurrencyServiceImpl();

    public void getPairInfo() {

    }

    @Override
    public void save() {
        System.out.println("Enter your currency information. e. g. [Russian ruble:RUB:\u20BD]");
        String next = scanner.nextLine();
        String[] currencyContent = next.split(":");
        CurrencyModel currencyModel = new CurrencyModel().setName(currencyContent[0])
                .setCode(currencyContent[1]).setSymbol(currencyContent[2]);
        currencyService.save(currencyModel);
    }

    @Override
    public void update() {

    }

    @Override
    public void remove() {

    }

    @Override
    public void find() {
        System.out.println("Enter your currency name. e. g. [Russian ruble]");
        String name = scanner.nextLine();
        Optional<CurrencyModel> byName = currencyService.findByName(name);
        System.out.println(byName.get());
    }
}
