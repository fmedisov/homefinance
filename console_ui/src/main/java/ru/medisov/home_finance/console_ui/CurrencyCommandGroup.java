package ru.medisov.home_finance.console_ui;

import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.service.CurrencyService;
import ru.medisov.home_finance.service.CurrencyServiceImpl;

import java.util.*;

public class CurrencyCommandGroup implements CommandGroup<CurrencyModel> {
    private Scanner scanner = new Scanner(System.in);
    private CurrencyService currencyService = new CurrencyServiceImpl();
    private List<CurrencyModel> currencies = new ArrayList<>(currencyService.findAll());

    @Override
    public CurrencyModel save() {
        System.out.println("New currency. Enter your currency information. e. g. [Russian ruble:RUB:\u20BD]");
        String currencyParams = scanner.nextLine();
        return currencyService.save(setCurrency(currencyParams));
    }

    @Override
    public CurrencyModel update() {
        CurrencyModel currency = requestCurrency();
        System.out.println("Enter your currency information. e. g. [Russian ruble:RUB:\u20BD]");
        String currencyParams = scanner.nextLine();
        return currencyService.update(setCurrency(currencyParams).setId(currency.getId()));
    }

    @Override
    public Optional<CurrencyModel> remove() {
        CurrencyModel currency = requestCurrency();
        Optional<CurrencyModel> optionalModel = Optional.empty();

        if (currencyService.remove(currency.getId())) {
            optionalModel = Optional.of(currency);
        }

        return optionalModel;
    }

    @Override
    public CurrencyModel find() {
        System.out.println("Find currency. Enter your currency name. e. g. [Russian ruble]");
        String name = scanner.nextLine();
        Optional<CurrencyModel> byName = currencyService.findByName(name);
        CurrencyModel found = byName.orElse(save()); //todo
        System.out.println("Выбрана валюта - " + found);

        return found;
    }

    public void getPairInfo() {
        //todo implement the method

    }

    public void findAll() {
        System.out.println("List of currencies");
        Collection<CurrencyModel> byName = currencyService.findAll();
        byName.forEach(System.out::println);
    }

    public CurrencyModel requestCurrency() {
        int size = currencies.size();

        System.out.println("enter number of currency: ");

        for (int i = 0; i < size; i++) {
            System.out.println(i + " - " + currencies.get(i).getName());
        }

        Scanner in = new Scanner(System.in);

        while (true) {
            String userChoice = in.nextLine();

            if (userChoice.length() == 0) {
                break;  // return to the top menu
            }

            try {
                int categoryNum = Integer.parseInt(userChoice);

                return currencies.get(categoryNum);
            } catch (Exception e) {
                System.out.println("Введите номер команды (Enter - для возврата к предыдущему пункту): ");
            }
        }

        return new CurrencyModel();
    }

    private CurrencyModel setCurrency(String currencyParams) {
        String[] currencyContent = currencyParams.split(":");
        return new CurrencyModel().setName(currencyContent[0]).setCode(currencyContent[1]).setSymbol(currencyContent[2]);
    }
}
