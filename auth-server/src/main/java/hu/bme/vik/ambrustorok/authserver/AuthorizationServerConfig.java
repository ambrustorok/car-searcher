package hu.bme.vik.ambrustorok.authserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keys.KeyManager;
import org.springframework.security.crypto.keys.StaticKeyGeneratingKeyManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;

@EnableWebSecurity
public class AuthorizationServerConfig {

    @Value("${spring.datasource.driverClassName}")
    private String jdbcDriverClassName;
    @Value("${spring.datasource.url}")
    private String jdbcURl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Bean
    public WebSecurityConfigurer<WebSecurity> defaultOAuth2AuthorizationServerSecurity() {
        return new AuthorizationServerWebSecurityConfiguration();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    /**
     * Itt most beégetjük milyen klienseink (alkalmazás komponenseink) vannak a rendszerben.
     *
     * @return
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        // A frontend
        RegisteredClient frontendClient = RegisteredClient.withId("1138c5af-fd56-4a58-8c8f-769c2e327433")
                .clientId("car-searcher-frontend")
                .clientSecret("ca8831eb-e504-46f1-a514-8be84ba892ed") // Ezt elvileg public clientnél nem kellene megadni
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("http://localhost:4200/*")
                .redirectUri("http://localhost:8080/swagger-ui/oauth2-redirect.html")
                .build();

        // Az API-Gateway
        RegisteredClient apiGateway = RegisteredClient.withId("c681c3d5-fa87-4cd7-b8fc-650862c0cc6e")
                .clientId("api-gateway")
                .clientSecret("546c5e7b-bd66-4df3-a601-8a31a4bee482")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();

        // vehicle service
        RegisteredClient vehicleService = RegisteredClient.withId("02032352-50dd-4c5f-92c8-2b53fb2b764c")
                .clientId("vehicle-service")
                .clientSecret("f3c2557d-ab7c-4b9f-a287-9e5b9b5eb599")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();

        // vehicle service
        RegisteredClient searchService = RegisteredClient.withId("67126549-50dd-4c5f-92c8-2b53fb2b764c")
                .clientId("search-service")
                .clientSecret("f3c2557d-ab7c-4c9f-a287-9e5b9b5eb599")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .build();


        return new InMemoryRegisteredClientRepository(
                frontendClient,
                apiGateway,
                searchService,
                vehicleService
        );
    }

    @Bean
    public KeyManager keyManager() {
        return new StaticKeyGeneratingKeyManager();
    }

    @Bean
    public UserDetailsService users() {
        return userDetailsService();
    }

    @Bean
    public JdbcDaoImpl userDetailsService() {
        JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
        jdbcDaoImpl.setDataSource(dataSource());
        jdbcDaoImpl.setEnableGroups(false);
        jdbcDaoImpl.setEnableAuthorities(true);
        jdbcDaoImpl.setMessageSource(messageSource());
        return jdbcDaoImpl;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriverClassName);
        dataSource.setUrl(jdbcURl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        return dataSource;
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource())
                .usersByUsernameQuery("select username, password, enabled"
                        + " from users where username=?")
                .authoritiesByUsernameQuery("select username, authority "
                        + "from authorities where username=?")
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}