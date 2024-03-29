package hu.bme.vik.ambrustorok.common.configuration.apidocs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.AuthorizationCodeGrantBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Collections;

@Configuration
public class ApiDocsConfiguration {

    @Value("${cs.auth-sever-url}")
    private String authServerUrl;

    @Value("${cs.client-id}")
    private String CLIENT_ID;

    @Value("${cs.client-secret}")
    private String CLIENT_SECRET;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("hu.bme.vik.ambrustorok"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(Collections.singletonList(securityScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    @Bean
    public SecurityConfiguration security() {
        return SecurityConfigurationBuilder.builder()
                .clientId(CLIENT_ID)
                .clientSecret(CLIENT_SECRET)
                .scopeSeparator(" ")
                .useBasicAuthenticationWithAccessCodeGrant(true)
                .build();
    }

    private SecurityScheme securityScheme() {
        GrantType grantType = new AuthorizationCodeGrantBuilder()
                .tokenEndpoint(b -> b.url(authServerUrl + "/token").tokenName("oauthtoken"))
                .tokenRequestEndpoint(b -> b.url(authServerUrl + "/authorize").clientIdName(CLIENT_ID).clientSecretName(CLIENT_SECRET)).build();

        return new OAuthBuilder().name("spring_oauth")
                .grantTypes(Collections.singletonList(grantType))
                .build();
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Collections.singletonList(new SecurityReference("spring_oauth", new AuthorizationScope[]{})))
                .operationSelector(s -> !s.requestMappingPattern().matches(".*/public.*"))
                .build();
    }
}
