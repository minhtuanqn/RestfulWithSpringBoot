package com.config;

import com.RestfullWithSpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = {RestfullWithSpringBootApplication.class})
@Profile("test")
public class TestConfig {


}
