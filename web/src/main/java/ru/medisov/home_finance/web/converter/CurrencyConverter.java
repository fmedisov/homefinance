package ru.medisov.home_finance.web.converter;

import org.springframework.stereotype.Component;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.web.view.CurrencyView;

@Component
public class CurrencyConverter {

    public CurrencyView toCurrencyView(CurrencyModel currencyModel) {
        CurrencyView currencyView = new CurrencyView();
        currencyView
                .setId(currencyModel.getId())
                .setName(currencyModel.getName())
                .setCode(currencyModel.getCode())
                .setSymbol(currencyModel.getSymbol());

        return currencyView;
    }

    public CurrencyModel toCurrencyModel(CurrencyView currencyView) {
        CurrencyModel currencyModel = new CurrencyModel();
        currencyModel
                .setId(currencyView.getId())
                .setName(currencyView.getName())
                .setCode(currencyView.getCode())
                .setSymbol(currencyView.getSymbol());

        return currencyModel;
    }
}
