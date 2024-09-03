package PortfolioApiCall.service;

import PortfolioApiCall.Config.EnvVariableReader;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraphQlDataFetcher {
    private static final Logger logger = LoggerFactory.getLogger(GraphQlDataFetcher.class);
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    EnvVariableReader envVariableReader;
    @Autowired
    TokenService tokenService;

    /**
     *
     * @param id portfolioId
     * @param startDate starting Date (optional)
     * @param endDate ending Date (optional)
     * @return Response from the FA server
     */
    public JsonNode getTransactions(Integer id, String startDate, String endDate) {

        String graphqlEndpoint = envVariableReader.getFa_graphqlbaseUrl();

        Map<String, Object> variables = new HashMap<>();
        variables.put("ids", List.of(id));

        if (!startDate.equals("start")) {
            variables.put("startDate", startDate);
        }

        if (!endDate.equals("end")) {
            variables.put("endDate", endDate);
        }


        logger.info("Variables set:: " + variables);
        return fetchData(graphqlEndpoint, portfolioTransactionQuery(), variables);
    }

    /**
     *
     * @return Query
     */
    private String portfolioTransactionQuery() {

        return """
                query Transactions($ids: [Long], $startDate: String, $endDate: String) {
                   portfoliosByIds(ids: $ids) {
                     transactions(status: "OK", startDate: $startDate, endDate: $endDate) {
                       portfolio: parentPortfolio {
                         shortName
                       }
                       security {
                         name
                         isinCode
                       }
                       currency {
                         code: securityCode
                       }
                       quantity: amount
                       unitPrice: unitPriceView
                       tradeAmount
                       type {
                         name: typeName
                       }
                       transactionDate
                       settlementDate
                     }
                   }
                 }
                 
                """;
    }


    /**
     *
     * @param graphqlEndpoint endpoint
     * @param query queryUsed
     * @param variables variables that were provided
     * @return Response from Server
     */
    private JsonNode fetchData(String graphqlEndpoint, String query, Map<String, Object> variables) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Context-Type", "application/json");
        headers.setBearerAuth(tokenService.getAccessToken());
        Map<String, Object> body = new HashMap<>();
        body.put("query", query);
        body.put("variables", variables);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<JsonNode> response = restTemplate.exchange(
                graphqlEndpoint,
                HttpMethod.POST,
                entity,
                JsonNode.class
        );
        return response.getBody();
    }
}
