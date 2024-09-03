package PortfolioApiCall.Config;

import lombok.Getter;
import lombok.Lombok;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// explicitly INCLUDE the component name, if @Qualifier annotation needs to be used
@Component("envVariableReader")
@Configuration
public class EnvVariableReader {
    Lombok lombok;

    @Getter
    @Value("${spring.application.name}")
    private String applicationName;

    @Getter
    @Value("${credentials.username}")
    private String username;

    @Getter
    @Value("${credentials.password}")
    private String password;

    @Getter
    @Value("${credentials.username1}")
    private String username1;

    @Getter
    @Value("${credentials.password1}")
    private String password1;

    @Getter
    @Value("${rootDirectory}")
    private String rootDir;

    @Getter
    @Value("${fa.graphqlbaseUrl}")
    private String fa_graphqlbaseUrl;

    @Getter
    @Value("${fa.loginUrl}")
    private String fa_loginUrl;

    @Getter
    @Value("${fa.username}")
    private String fa_username;

    @Getter
    @Value("${fa.password}")
    private String fa_password;

}


