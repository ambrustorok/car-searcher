package hu.bme.vik.ambrustorok.common.configuration.apidocs;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class ResourceServerWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/swagger-resources/**", "/swagger-ui/", "/webjars/springfox-swagger-ui/**", "/v2/api-docs**", "/swagger**").permitAll()
                .anyRequest().authenticated()
                .and().oauth2ResourceServer().jwt();
    }
}
