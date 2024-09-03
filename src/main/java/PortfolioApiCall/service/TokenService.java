package PortfolioApiCall.service;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
    The usage of this class is to minimize the api call for the authentication to the FA server.
    As the expiry time for the token is known, thus the token is made available in memory and re-used at-most.
 */
@Service
public class TokenService {
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);
    private final RestDataFetcher restDataFetcher;
    private final ObjectMapper objectMapper;
    private String accessToken;
    private Long tokenExpiresIn;

    @Autowired
    public TokenService(RestDataFetcher restDataFetcher) {
        this.restDataFetcher = restDataFetcher;
        this.objectMapper = new ObjectMapper();
        this.accessToken = null;
        this.tokenExpiresIn = null;
    }

    /**
     *
     * @return  AccessToken from server if the token has expired; if not loads the memory
     */
    public synchronized String getAccessToken() {
        if (isTokenExpired()) {
            logger.info("Token has expired / about to expire / empty, fetching fresh token");

            try {
                String body = restDataFetcher.getFACredentials();
                JsonNode tokenResponse = objectMapper.readTree(body);
                this.accessToken = tokenResponse.get("access_token").asText();
                this.tokenExpiresIn = System.currentTimeMillis() + tokenResponse.get("expires_in").asLong() * 1000;

                return this.accessToken;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse the body", e);
            }
        }

        logger.info("Token has not expired; using the previous token");
        return this.accessToken;
    }

    private boolean isTokenExpired() {
        return this.tokenExpiresIn == null || System.currentTimeMillis() >= tokenExpiresIn - 20 * 1000;
    }

}

