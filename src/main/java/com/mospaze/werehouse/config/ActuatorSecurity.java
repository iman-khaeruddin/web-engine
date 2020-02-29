package com.mospaze.werehouse.config;

/*import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;*/

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 */
//@Configuration
public class ActuatorSecurity /*extends WebSecurityConfigurerAdapter*/ {

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .requestMatchers(EndpointRequest.to(HealthEndpoint.class))
                .hasRole("ACTUATOR_ADMIN")
                .requestMatchers(EndpointRequest.toAnyEndpoint())
                .permitAll()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .permitAll()
                .antMatchers("/")
                .permitAll()
                .antMatchers("/**")
                .authenticated()
                .and()
                .httpBasic();
    }*/

}