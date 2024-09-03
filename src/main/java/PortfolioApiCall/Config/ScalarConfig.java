package PortfolioApiCall.Config;

// https://www.danvega.dev/blog/graphql-custom-scalars
// https://github.com/graphql-java/graphql-java-extended-scalars

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class ScalarConfig {
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.Json)
                .scalar(ExtendedScalars.GraphQLLong);
    }
}
