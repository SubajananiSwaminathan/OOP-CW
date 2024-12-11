package lk.oop.CLI;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Represents the configuration settings for the ticketing system.
 * <p>
 * The {@code Configuration} class encapsulates the system's configuration parameters, such as:
 * <ul>
 *   <li>Total tickets available in the system.</li>
 *   <li>Rate at which tickets are released.</li>
 *   <li>Rate at which customers retrieve tickets.</li>
 *   <li>Maximum ticket capacity in the system.</li>
 * </ul>
 * </p>
 * <p>
 * This class provides default initialization, the ability to load and save configurations, and serves
 * as a central data structure for managing these settings.
 * </p>
 * <p>
 * Additionally, the class includes a Command-Line Interface (CLI) that allows users to input or modify
 * configuration values interactively. This CLI enables users to configure the ticketing system's parameters
 * directly by entering values through prompts during system startup or configuration sequences.
 * </p>
 */
public class Configuration {

    private static final String JSON_CONFIG_FILE = "configuration.json";
    private static final String TEXT_CONFIG_FILE = "configuration.txt";

    private static final String BASE_URL = "http://localhost:8080/api/tickets";

    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    /**
     * The entry point of the Ticketing System application.
     * <p>
     * This method initializes the system, sets up necessary components, and provides the user
     * with an interface to interact with the ticketing system through a command-line interface (CLI).
     * It manages the system's startup sequence and launches the menu for user interaction.
     * </p>
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Configuration configuration = new Configuration();

        if (configuration.loadConfigurationFromJSON() || configuration.loadConfigurationFromTextFile()) {
            System.out.println("Configuration file found.");
            System.out.print("Do you want to use the existing configuration? (Yes/No): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.startsWith("y")) {
                System.out.println("Using existing configuration...");
                configuration.sendConfigurationToBackend();
            } else if (choice.startsWith("n")) {
                System.out.println("Proceeding with new configuration initialization...");
                configuration.initializeSystem(scanner);
            } else {
                System.out.println("Invalid input. Please enter 'Y' or 'N'.");
            }
        } else {
            System.out.println("No configuration file found. Please initialize the system.");
            configuration.initializeSystem(scanner);
        }

        while (true) {
            configuration.showMenu(scanner);
        }
    }

    /**
     * Constructs a new {@code Configuration} object with default configuration settings.
     * <p>
     * This constructor initializes the configuration fields to their default values:
     * <ul>
     *   <li>{@code totalTickets = 0}</li>
     *   <li>{@code ticketReleaseRate = 0}</li>
     *   <li>{@code customerRetrievalRate = 0}</li>
     *   <li>{@code maxTicketCapacity = 0}</li>
     * </ul>
     * </p>
     * <p>
     * These defaults represent an unconfigured state, which can be updated later using setter methods
     * or by loading configuration settings from external sources.
     * </p>
     */
    public Configuration() {
        this.totalTickets = 0;
        this.ticketReleaseRate = 0;
        this.customerRetrievalRate = 0;
        this.maxTicketCapacity = 0;
    }

    /**
     * Displays the command-line interface (CLI) menu to the user and processes their selection.
     * <p>
     * This method presents a menu with the following options:
     * <ul>
     *   <li>Start Vendor Threads</li>
     *   <li>Start Customer Threads</li>
     *   <li>Get Ticket Pool Status</li>
     *   <li>Stop System</li>
     * </ul>
     * The user is prompted to input their choice, and the method processes it accordingly:
     * <ul>
     *   <li>Choice 1: Calls {@link #startVendorThreads(Scanner)}</li>
     *   <li>Choice 2: Calls {@link #startCustomerThreads(Scanner)}</li>
     *   <li>Choice 3: Calls {@link #getTicketPoolStatus()}</li>
     *   <li>Choice 4: Calls {@link #stopSystem()} and exits the system.</li>
     * </ul>
     * If an invalid option is entered, an error message is displayed, and the menu remains active.
     * </p>
     *
     * @param scanner A {@link Scanner} instance used to capture user input for menu selection.
     */
    private void showMenu(Scanner scanner) {
        System.out.println("\n--- Ticketing System CLI ---");
        System.out.println("1. Start Vendor Threads");
        System.out.println("2. Start Customer Threads");
        System.out.println("3. Get Ticket Pool Status");
        System.out.println("4. Stop System");
        System.out.print("Choose an option: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1":
                startVendorThreads(scanner);
                break;
            case "2":
                startCustomerThreads(scanner);
                break;
            case "3":
                getTicketPoolStatus();
                break;
            case "4":
                stopSystem();
                return;
            default:
                System.out.println("Invalid option. Try again.");
        }
    }

    /**
     * Initializes the system by prompting the user for configuration parameters,
     * validating their input, and setting up system configuration accordingly.
     * <p>
     * This method interacts with the user through the provided {@link Scanner} instance
     * to gather the following input values:
     * <ul>
     *   <li>Total number of tickets</li>
     *   <li>Ticket release rate (in milliseconds)</li>
     *   <li>Customer retrieval rate (in milliseconds)</li>
     *   <li>Maximum ticket capacity</li>
     * </ul>
     * These inputs are validated via the {@link #getValidInput(Scanner, String)} method.
     * After collecting the configuration, the method sends the configuration settings
     * to the backend system and persists them by saving to both JSON and text file formats.
     * </p>
     *
     * @param scanner A {@link Scanner} instance used to capture user input for configuration
     */
    private void initializeSystem(Scanner scanner) {
        System.out.println("Please enter the configuration parameters:");

        this.totalTickets = getValidInput(scanner, "Enter total number of tickets: ");
        this.ticketReleaseRate = getValidInput(scanner, "Enter ticket release rate (milliseconds): ");
        this.customerRetrievalRate = getValidInput(scanner, "Enter customer retrieval rate (milliseconds): ");
        this.maxTicketCapacity = getValidInput(scanner, "Enter maximum ticket capacity: ");

        System.out.println("System initialized");

        sendConfigurationToBackend();
        saveConfigurationToJSON();
        saveConfigurationToTextFile();
    }

    /**
     * Prompts the user for input, validates the entered value, and ensures it is a
     * positive integer greater than zero.
     * <p>
     * This method repeatedly displays the provided prompt until the user enters
     * a valid positive integer. If the user provides invalid input (non-integer or
     * integer less than or equal to zero), appropriate error messages are shown,
     * and the user is re-prompted until valid input is received.
     * </p>
     *
     * @param scanner A {@link Scanner} instance used to read user input.
     * @param prompt  A {@link String} that specifies the message to display to the user
     *                when prompting for input.
     * @return A validated positive integer entered by the user.
     */
    private int getValidInput(Scanner scanner, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine());
                if (input <= 0) {
                    System.out.println("Please enter a positive integer greater than zero.");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    /**
     * Sends the current system configuration settings to the backend server via an HTTP POST request.
     * <p>
     * This method constructs a URL with the system's configuration parameters, including:
     * total tickets,
     * ticket release rate,
     * customer retrieval rate, and
     * maximum ticket capacity.
     * The URL is used to send the configuration to the backend using the {@link #sendPostRequest} method.
     * </p>
     * <p>
     * If the operation is successful, a success message is printed. Otherwise, an error message is logged
     * to indicate the failure to send the configuration.
     * </p>
     */
    private void sendConfigurationToBackend() {
        try {
            String urlString = BASE_URL + "/configure?totalTickets=" + this.totalTickets +
                    "&ticketReleaseRate=" + this.ticketReleaseRate +
                    "&customerRetrievalRate=" + this.customerRetrievalRate +
                    "&maxTicketCapacity=" + this.maxTicketCapacity;

            sendPostRequest(urlString, null);

            System.out.println("Configuration successfully sent to the backend.");
        } catch (Exception e) {
            System.err.println("Error sending configuration to backend: " + e.getMessage());
        }
    }

    /**
     * Initiates vendor threads by sending configuration details to the backend server.
     * <p>
     * This method prompts the user to input the number of vendors and the number of tickets each vendor
     * should release per cycle. These inputs are combined with the system's ticket release rate and sent
     * to the backend server using an HTTP POST request via the {@link #sendPostRequest} method.
     * </p>
     * <p>
     * If successful, the method prints confirmation that the vendor threads have started and outputs
     * the URL sent to the backend server. If an error occurs during the request, it logs the error message.
     * </p>
     * @param scanner A {@link Scanner} instance used to read user input for the vendor configuration.
     */
    private void startVendorThreads(Scanner scanner) {
        System.out.print("Enter vendor count: ");
        int vendorCount = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter tickets per release: ");
        int ticketsPerRelease = Integer.parseInt(scanner.nextLine());

        try {
            String urlString = BASE_URL + "/startVendorThreads?vendorCount=" + vendorCount +
                    "&ticketReleaseRate=" + this.ticketReleaseRate +
                    "&ticketsPerRelease=" + ticketsPerRelease;

            sendPostRequest(urlString, null);
            System.out.println("Vendor threads started.");
        } catch (Exception e) {
            System.err.println("Error starting vendor threads: " + e.getMessage());
        }
    }

    /**
     * Initiates customer threads by sending configuration details to the backend server.
     * <p>
     * This method prompts the user to input the number of customers and the number of tickets each customer
     * should attempt to purchase per cycle. These inputs are combined with the system's customer retrieval rate
     * and sent to the backend server using an HTTP POST request via the {@link #sendPostRequest} method.
     * </p>
     * <p>
     * If successful, the method prints confirmation that the customer threads have started and outputs
     * the URL sent to the backend server. If an error occurs during the request, it logs the error message.
     * </p>
     * @param scanner A {@link Scanner} instance used to read user input for the customer configuration.
     */
    private void startCustomerThreads(Scanner scanner) {
        System.out.print("Enter customer count: ");
        int customerCount = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter tickets per purchase: ");
        int ticketsPerPurchase = Integer.parseInt(scanner.nextLine());

        try {
            String urlString = BASE_URL + "/startCustomerThreads?customerCount=" + customerCount +
                    "&customerRetrievalRate=" + this.customerRetrievalRate +
                    "&ticketsPerPurchase=" + ticketsPerPurchase;

            sendPostRequest(urlString, null);
            System.out.println("Customer threads started.");
        } catch (Exception e) {
            System.err.println("Error starting customer threads: " + e.getMessage());
        }
    }

    /**
     * Retrieves and displays the current status of the ticket pool from the backend server.
     * <p>
     * This method sends an HTTP GET request to the backend server's status endpoint and prints
     * the response to the console. The status includes relevant information about the ticket pool's
     * current state as received from the server.
     * </p>
     * <p>
     * If the request is successful, the response is displayed with the prefix "Ticket Pool Status".
     * If an error occurs during the request, an error message is logged.
     * </p>
     */
    private void getTicketPoolStatus() {
        try {
            String urlString = BASE_URL + "/status";
            String response = sendGetRequest(urlString);
            System.out.println("Ticket Pool Status: " + response);
        } catch (Exception e) {
            System.err.println("Error getting ticket pool status: " + e.getMessage());
        }
    }

    /**
     * Stops the system by terminating the application.
     * <p>
     * This method prints a message indicating that the system is stopping and then shuts down
     * the application using {@link System#exit(int)} with a status code of 0.
     * </p>
     */
    private void stopSystem() {
        System.out.println("Stopping the system...");
        System.exit(0);
    }

    /**
     * Sends an HTTP GET request to the specified URL and returns the server's response as a string.
     * <p>
     * This method establishes a connection to the given URL, sends a GET request, and reads the
     * response from the server. The response is read line by line and combined into a single string,
     * which is then returned.
     * </p>
     *
     * @param urlString The URL to which the GET request is sent, as a string.
     * @return The server's response as a string.
     * @throws Exception If an error occurs while opening the connection, sending the request,
     *                   or reading the server's response.
     */
    private String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    /**
     * Sends an HTTP POST request to the specified URL with an optional JSON payload.
     * <p>
     * This method establishes a connection to the given URL, sets up the request method as POST,
     * and sends the provided JSON payload if it is not null. The method also sets the appropriate
     * request header for JSON content when a payload is provided.
     * </p>
     *
     * @param urlString   The URL to which the POST request is sent, as a string.
     * @param jsonPayload The JSON payload to send with the POST request. If null, no payload is sent.
     * @throws Exception If an error occurs during the connection setup, sending the request,
     *                   or while reading the response.
     */
    private void sendPostRequest(String urlString, String jsonPayload) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");

        connection.setDoOutput(true);
        if (jsonPayload != null) {
            connection.setRequestProperty("Content-Type", "application/json");
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
        }

        connection.getResponseCode();
    }

    /**
     * Saves the current system configuration to a JSON file.
     * <p>
     * This method uses the Gson library to serialize the current object (representing the system's state)
     * into JSON format and writes it to a file specified by {@code JSON_CONFIG_FILE}. The JSON is saved
     * with pretty-printing enabled for better human readability.
     * </p>
     * <p>
     * If the operation is successful, a confirmation message is printed indicating the file path.
     * If an I/O error occurs during the write operation, an error message is logged to the console.
     * </p>
     *
     */
    private void saveConfigurationToJSON() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(JSON_CONFIG_FILE)) {
            gson.toJson(this, writer);
            System.out.println("Configuration saved to " + JSON_CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    /**
     * Loads the system configuration from a JSON file and applies it to the current instance.
     * <p>
     * This method uses the Gson library to deserialize configuration settings from a JSON file specified
     * by {@code JSON_CONFIG_FILE}. The following fields are loaded:
     * <ul>
     *   <li>Total tickets</li>
     *   <li>Ticket release rate</li>
     *   <li>Customer retrieval rate</li>
     *   <li>Maximum ticket capacity</li>
     * </ul>
     * If successful, the method updates the corresponding fields in the current instance and returns {@code true}.
     * If the JSON file is not found or an error occurs during the deserialization, appropriate error messages are logged,
     * and the method returns {@code false}.
     * </p>
     *
     * @return {@code true} if the configuration was successfully loaded; {@code false} otherwise.
     */
    private boolean loadConfigurationFromJSON() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(JSON_CONFIG_FILE)) {
            Configuration loadedConfig = gson.fromJson(reader, Configuration.class);
            this.totalTickets = loadedConfig.totalTickets;
            this.ticketReleaseRate = loadedConfig.ticketReleaseRate;
            this.customerRetrievalRate = loadedConfig.customerRetrievalRate;
            this.maxTicketCapacity = loadedConfig.maxTicketCapacity;
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("JSON configuration file not found.");
        } catch (IOException e) {
            System.err.println("Error reading JSON configuration file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Saves the current system configuration to a text file in a simple key-value format.
     * <p>
     * This method writes the following configuration settings to a text file specified by {@code TEXT_CONFIG_FILE}:
     * <ul>
     *   <li>Total tickets</li>
     *   <li>Ticket release rate</li>
     *   <li>Customer retrieval rate</li>
     *   <li>Maximum ticket capacity</li>
     * </ul>
     * Each configuration setting is saved on a new line in the format: {@code key=value}.
     * If the operation is successful, a confirmation message is printed. If an I/O error occurs during the write operation,
     * an error message is logged to the console.
     * </p>
     */
    private void saveConfigurationToTextFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEXT_CONFIG_FILE, false))) {
            writer.write("totalTickets=" + totalTickets + "\n");
            writer.write("ticketReleaseRate=" + ticketReleaseRate + "\n");
            writer.write("customerRetrievalRate=" + customerRetrievalRate + "\n");
            writer.write("maxTicketCapacity=" + maxTicketCapacity + "\n");
            System.out.println("Configuration saved to " + TEXT_CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("Error saving configuration to text file: " + e.getMessage());
        }
    }

    /**
     * Loads the system configuration from a text file and applies it to the current instance.
     * <p>
     * This method reads a text file specified by {@code TEXT_CONFIG_FILE} to load configuration settings.
     * It expects the file to be formatted with each configuration setting on a separate line in the format:
     * {@code key:value}. The following settings are loaded:
     * <ul>
     *   <li>Total tickets</li>
     *   <li>Ticket release rate</li>
     *   <li>Customer retrieval rate</li>
     *   <li>Maximum ticket capacity</li>
     * </ul>
     * If successful, the method parses these values, updates the system's fields, and returns {@code true}.
     * If an error occurs during the file reading, parsing, or I/O operation, an error message is logged,
     * and the method returns {@code false}.
     * </p>
     * @return {@code true} if the configuration was successfully loaded; {@code false} otherwise.
     */
    private boolean loadConfigurationFromTextFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEXT_CONFIG_FILE))) {
            this.totalTickets = Integer.parseInt(reader.readLine().split(":")[1].trim());
            this.ticketReleaseRate = Integer.parseInt(reader.readLine().split(":")[1].trim());
            this.customerRetrievalRate = Integer.parseInt(reader.readLine().split(":")[1].trim());
            this.maxTicketCapacity = Integer.parseInt(reader.readLine().split(":")[1].trim());
            return true;
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading configuration from text file: " + e.getMessage());
        }
        return false;
    }
}
