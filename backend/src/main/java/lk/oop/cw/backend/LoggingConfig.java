package lk.oop.cw.backend;

import lombok.Getter;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Configures logging for the application.
 * <p>
 * This class sets up a logger with both console and file handlers. Logs are saved to the file
 * `logs/app.log` and are also output to the console. It initializes the logger with a simple
 * formatter and sets the logging level to {@link Level #INFO}.
 * </p>
 */
public class LoggingConfig {
    @Getter
    private static Logger logger;

    static {
        try {
            FileHandler fileHandler = new FileHandler("logs/app.log", false);
            fileHandler.setFormatter(new SimpleFormatter());

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());

            logger = Logger.getLogger(LoggingConfig.class.getName());
            logger.addHandler(fileHandler);
            logger.addHandler(consoleHandler);

            logger.setLevel(Level.INFO);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
