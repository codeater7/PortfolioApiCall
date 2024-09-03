
package PortfolioApiCall.utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.io.IOException;

/**
 * This is used for tracing ip of the users who make a http call to the services.
 */
@Component
@Order(0)
public class RequestLogger implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestLogger.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String method = httpRequest.getMethod();
        String queryString = httpRequest.getQueryString();
        String clientIp = httpRequest.getRemoteAddr();
        String requestUri = httpRequest.getRequestURI();

        logger.info(" {}:::{} ~> {}: queryString: {}", method ,clientIp , requestUri, queryString);

        chain.doFilter(request, response);
    }
}

