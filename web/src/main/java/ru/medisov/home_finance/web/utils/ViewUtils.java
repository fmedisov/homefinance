package ru.medisov.home_finance.web.utils;

import org.springframework.core.convert.converter.Converter;
import ru.medisov.home_finance.service.Service;

import java.util.ArrayList;
import java.util.List;

public class ViewUtils {

    public static  <F, T> List<T> listViews(Service<F> service, Converter<F, T> converter) {
        List<T> viewList = new ArrayList<>();
        service.findAll().forEach(model -> viewList.add(converter.convert(model)));

        return viewList;
    }
}
