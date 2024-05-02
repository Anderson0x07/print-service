package com.impresion.impresion.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String url = "http://localhost:4200"; //SE CAMBIA EN PRODUCCION

        registry.addMapping("/print").allowedOrigins(url);

    }

}
