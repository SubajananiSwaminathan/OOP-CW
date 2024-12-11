package lk.oop.cw.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for setting up Cross-Origin Resource Sharing (CORS) settings.
 * <p>
 * This configuration ensures that requests from a specific frontend application (running on
 * <a href="http://localhost:4200">http://localhost:4200</a>) are allowed to access the backend APIs. It allows common HTTP methods
 * and all headers to facilitate interaction with the frontend.
 * </p>
 */
@Configuration
public class WebConfig {

    /**
     * Defines a {@link WebMvcConfigurer} bean to configure CORS mappings.
     * @return A configured instance of {@link WebMvcConfigurer} with the necessary CORS settings.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
