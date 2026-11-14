package com.cuda.gym.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

        @Override
    public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/login").setViewName("auth/login");

                registry.addViewController("/403").setViewName("error/403");
        registry.addViewController("/404").setViewName("error/404");

                registry.addViewController("/").setViewName("index");
    }

        @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
               registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/");

               registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

      @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }
}