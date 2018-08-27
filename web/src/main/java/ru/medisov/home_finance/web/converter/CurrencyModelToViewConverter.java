package ru.medisov.home_finance.web.converter;

import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.common.model.CurrencyModel;
import ru.medisov.home_finance.web.view.CurrencyView;

public class CurrencyModelToViewConverter implements Converter<CurrencyModel, CurrencyView> {

    public CurrencyView convert(CurrencyModel currencyModel) {
        CurrencyView currencyView = new CurrencyView();
        currencyView
                .setId(currencyModel.getId())
                .setName(currencyModel.getName())
                .setCode(currencyModel.getCode())
                .setSymbol(currencyModel.getSymbol());

        return currencyView;
    }

}