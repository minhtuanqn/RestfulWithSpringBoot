package com.controller;

import com.service.DefaultService;
import org.mockito.Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;

@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class)
public class AnotationWithProfile {

    @Configuration
    static class ContextConfiguration {

        @Mock
        DefaultService defaultService;

        @Bean
        public DefaultController defaultController() {
            return new DefaultController(defaultService);
        }
    }


}
