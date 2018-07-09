package ru.medisov.home_finance.common.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.medisov.home_finance.common.model.TransactionModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DOMParser implements Parser<TransactionModel> {
    @Override
    public List<TransactionModel> parseEntity(String xmlFile) throws ParserConfigurationException, IOException, SAXException {
        List<TransactionModel> transactionModels = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(ClassLoader.getSystemResourceAsStream(xmlFile));
        NodeList nodeList = document.getDocumentElement().getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node instanceof Element) {
                TransactionModel model = new TransactionModel();
                model.setId(Long.valueOf(node.getAttributes().getNamedItem("id").getNodeValue()));

                NodeList childNodes = node.getChildNodes();

                for (int j = 0; j < childNodes.getLength(); j++) {
                    Node cNode = childNodes.item(j);

                    String content = cNode.getLastChild().getTextContent().trim();

                    switch (cNode.getNodeName()) {
                        case "transaction_type":
                            break;
                        case "category":
                            break;
                        case "amount":
                            model.setAmount(fillAmount(content));
                            break;
                        case "name":
                            model.setName(content);
                            break;
                    }
                }


                transactionModels.add(model);
            }
        }

        return transactionModels;
    }

    private BigDecimal fillAmount(String content) {
       // BigDecimal.
        return BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_CEILING).add(BigDecimal.ZERO);
    }
}
