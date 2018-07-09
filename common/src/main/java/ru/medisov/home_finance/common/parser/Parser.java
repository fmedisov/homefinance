package ru.medisov.home_finance.common.parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface Parser<E> {

    List<E> parseEntity(String xmlFile) throws ParserConfigurationException, IOException, SAXException;

    default void printEntityList(List<E> eList) {
        eList.forEach(System.out::println);
    }
}
