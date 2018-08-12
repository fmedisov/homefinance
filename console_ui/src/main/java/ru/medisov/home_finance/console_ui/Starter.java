package ru.medisov.home_finance.console_ui;

import org.xml.sax.SAXException;
import ru.medisov.home_finance.common.model.TransactionModel;
import ru.medisov.home_finance.common.parser.DOMParser;
import ru.medisov.home_finance.common.parser.Parser;
//import ru.medisov.home_finance.service.ServiceInit;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class Starter {
    public static void main(String[] args) {
        UiConfig.initConfig();

        //startParser();
        startUi();
    }

    private static void startUi() {
        //new ServiceInit().init();
        Command commandType;

        do {
            commandType = new CommandRequester().request(Command.CommandType.TYPE);

            if (commandType != Command.EXIT) {
                new CommandRunner().setCommandType(commandType).execute();
            }

        } while (commandType != Command.EXIT);
    }

    private static void startParser() {
        Parser<TransactionModel> domParser = new DOMParser();
        List<TransactionModel> transactionModels = null;
        try {
            transactionModels = domParser.parseEntity("transactions.xml");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        domParser.printEntityList(transactionModels);
    }
}
