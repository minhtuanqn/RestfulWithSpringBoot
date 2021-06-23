package com.config;

import com.resolver.RequestPaginationResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Application config
 */
@Configuration
public class AppConfig implements WebMvcConfigurer {

    /**
     * Add Pagination resolver
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new RequestPaginationResolver());
    }
}
