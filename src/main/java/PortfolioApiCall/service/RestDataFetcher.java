package PortfolioApiCall.service;

import PortfolioApiCall.Config.EnvVariableReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
public class RestDataFetcher {
    @Autowired
    EnvVariableReader envVariableReader;
    private final RestTemplate restTemplate;

    @Autowired
    public RestDataFetcher(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    /**
     *
     * @return credentials for data access
     */
    public String getFACredentials(){
        String loginUrl = envVariableReader.getFa_loginUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        //form data
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("username", Collections.singletonList(envVariableReader.getFa_username()));
        formData.put("password", Collections.singletonList(envVariableReader.getFa_password()));
        formData.put("client_id", Collections.singletonList("external-api"));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(formData, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                loginUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        logger.info("Response from FA credentials "+ response);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to get credentials, status code: " + response.getStatusCode());
        }
    }
}
