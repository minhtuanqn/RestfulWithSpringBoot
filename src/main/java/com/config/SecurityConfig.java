package com.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    Environment environment;

    /**
     * Config security
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/swagger").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic().authenticationEntryPoint(authenticationEntryPoint());
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
    }

    /**
     * Register in-memony user details for authentication
     * @param auth
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String username = environment.getProperty("spring.security.user.name");
        String password = environment.getProperty("spring.security.user.password");
        String role = environment.getProperty("spring.security.user.roles");
        if (username != null && password != null) {
            auth.inMemoryAuthentication()
                    .withUser(username)
                    .password(passwordEncoder().encode(password))
                    .roles(role);
        }
    }

    /**
     * for handle exception when wrong username or password
     * @return AuthenticaionEntryPoin object
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            Map<String, Object> data = new HashMap<>();
            data.put("status", HttpStatus.FORBIDDEN.value());
            data.put("message", "Access denied");
            data.put("path", request.getRequestURL().toString());

            OutputStream out = response.getOutputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(out, data);
            out.flush();
        };
    }

    /**
     * Register bean for encoding
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
