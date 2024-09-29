package org.ide.microserviciocliente.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Permitir todos los orígenes, métodos y encabezados
                registry.addMapping("/**") // Para todas las rutas
                        .allowedOrigins("*") // Permitir todas las URLs
                        .allowedMethods("*") // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE, etc.)
                        .allowedHeaders("*") // Permitir todos los encabezados
                        .allowCredentials(true); // Permitir que se envíen cookies o credenciales
            }
        };
    }
}
