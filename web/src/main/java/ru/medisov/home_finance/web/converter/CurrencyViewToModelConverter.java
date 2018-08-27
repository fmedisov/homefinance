package ru.medisov.home_finance.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.web.view.CurrencyView;

public class CurrencyViewToModelConverter implements Converter<CurrencyView, CurrencyModel> {

    public CurrencyModel convert(CurrencyView currencyView) {
        CurrencyModel currencyModel = new CurrencyModel();
        currencyModel
                .setId(currencyView.getId())
                .setName(currencyView.getName())
                .setCode(currencyView.getCode())
                .setSymbol(currencyView.getSymbol());

        return currencyModel;
    }
}