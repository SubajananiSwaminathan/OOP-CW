package lk.oop.cw.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The entry point of the Spring Boot application.
 * <p>
 * This class is annotated with {@link SpringBootApplication} to indicate a
 * Spring Boot application. The main method initializes and runs the application.
 * </p>
 */
@SpringBootApplication
public class BackendApplication {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args Command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
