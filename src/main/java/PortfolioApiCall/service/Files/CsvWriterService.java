package PortfolioApiCall.service.Files;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvWriterService {

    private static final Logger logger = LoggerFactory.getLogger(CsvWriterService.class);

    /**
     *
     * @param response response from the fA server
     * @param filePath
     * @return
     * @throws IOException
     */
    public String writeToCsv(JsonNode response, Path filePath) throws IOException {

        logger.info("Transaction " + response);

        List<Map<String, String>> dataList = convertJsonNodeToListOfMaps(response);

        logger.info("Transaction converted to dataList " + dataList);


        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder()
                .addColumn("PortfolioShortName")
                .addColumn("SecurityName")
                .addColumn("SecurityISINCode")
                .addColumn("CurrencyCode")
                .addColumn("Amount")
                .addColumn("UnitPrice")
                .addColumn("TradeAmount")
                .addColumn("TypeName")
                .addColumn("TransactionDate")
                .addColumn("SettlementDate")
                .build().withHeader();

        // convert Path to file
        File file = filePath.toFile();

        csvMapper.writer(schema).writeValue(file, dataList);
        logger.info("Successfully written data to file");
        return "Successfully written data to file";
    }

    /**
     *
     * @return DummyData
     * @throws IOException
     */
    public static JsonNode DummyData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String dummy_transaction_response = """
                   [
                             {
                               "portfolio": {
                                 "shortName": "Anna-fonder"
                               },
                               "security": {
                                 "name": "SFL Corporation Ltd. 18/23 4,875% CONV",
                                 "isinCode": "US824689AG86"
                               },
                               "currency": {
                                 "code": "USD"
                               },
                               "quantity": 910000,
                               "unitPrice": 1.21875,
                               "tradeAmount": 11090.63,
                               "type": {
                                 "name": "Coupon"
                               },
                               "transactionDate": "2021-11-01",
                               "settlementDate": "2021-11-01"
                             },
                             {
                               "portfolio": {
                                 "shortName": "Anna-fonder"
                               },
                               "security": {
                                 "name": "FA Mixed+ B",
                                 "isinCode": "LU0012345678"
                               },
                               "currency": {
                                 "code": "EUR"
                               },
                               "quantity": 8094.83894162,
                               "unitPrice": 6.70347,
                               "tradeAmount": 54263.51,
                               "type": {
                                 "name": "Redemption"
                               },
                               "transactionDate": "2021-09-29",
                               "settlementDate": "2021-09-29"
                             }
                   ]
                """;

        return objectMapper.readTree(dummy_transaction_response);
    }


    /**
     *
     * @param response response from the FA server
     * @return only the  transactions
     */

    private static JsonNode getTransactionList (JsonNode response){
        return response.path("data").path("portfoliosByIds").get(0).path("transactions");
    }

    /**
     *
     * @param response response from the FA server for the query
     * @return only the transaction for the portfolio ready for writing to csv
     */
    private static  List<Map<String, String>> convertJsonNodeToListOfMaps(JsonNode response) {

        JsonNode transactionNode = getTransactionList(response);
        List<Map<String, String>> dataList = new ArrayList<>();
        for (JsonNode transaction : transactionNode) {
            Map<String, String> rowMap = new HashMap<>();
            rowMap.put("PortfolioShortName", transaction.path("portfolio").path("shortName").asText());
            rowMap.put("SecurityName", transaction.path("security").path("name").asText());
            rowMap.put("SecurityISINCode", transaction.path("security").path("isinCode").asText());
            rowMap.put("CurrencyCode", transaction.path("currency").path("code").asText());
            rowMap.put("Amount", transaction.path("quantity").asText());
            rowMap.put("UnitPrice", transaction.path("unitPrice").asText());
            rowMap.put("TradeAmount", transaction.path("tradeAmount").asText());
            rowMap.put("TypeName", transaction.path("type").path("name").asText());
            rowMap.put("TransactionDate", transaction.path("transactionDate").asText());
            rowMap.put("SettlementDate", transaction.path("settlementDate").asText());
            dataList.add(rowMap);
        }
        return dataList;
    }

}
