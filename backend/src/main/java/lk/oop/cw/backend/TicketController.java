package lk.oop.cw.backend;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing ticket-related operations.
 * <p>
 * This controller provides endpoints for operations such as adding or removing tickets, configuring
 * the ticket pool, starting or stopping vendor and customer threads, and retrieving system status or logs.
 * It serves as the communication layer between the frontend and the ticket service layer.
 * </p>
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    private final TicketService ticketService;

    /**
     * Constructs the TicketController with the provided {@link TicketService}.
     * @param ticketService The service responsible for handling business logic related to tickets.
     */
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Adds a batch of tickets to the system.
     * @param vendorId   The ID of the vendor providing the tickets.
     * @param eventId    The ID of the event for which tickets are created.
     * @param eventName  The name of the event.
     * @param price      The price of the tickets.
     * @param tickets    The number of tickets to add.
     * @param category   The category of the tickets.
     */
    @PostMapping("/add/{tickets}/{vendorId}/{eventId}/{eventName}/{price}/{category}")
    public void addTickets(@PathVariable String vendorId, @PathVariable String eventId, @PathVariable String eventName,
                           @PathVariable double price, @PathVariable int tickets, @PathVariable String category) {
        ticketService.addTickets(vendorId, eventId, eventName, price, tickets, category);
    }

    /**
     * Removes a specific ticket associated with a customer ID.
     * @param customerId The ID of the customer whose ticket should be removed.
     */
    @PostMapping("/remove/{customerId}")
    public void removeTicket(@PathVariable String customerId) {
        ticketService.removeTicket(customerId);
    }

    /**
     * Configures the initial state of the ticket pool with a total number of tickets and maximum ticket capacity.
     * @param totalTickets      The total number of tickets to initialize the pool with.
     * @param maxTicketCapacity The maximum capacity allowed in the ticket pool.
     */
    @PostMapping("/configure")
    public void initializeTicketPool(@RequestParam int totalTickets, @RequestParam int maxTicketCapacity) {
        ticketService.initializeTicketPool(totalTickets, maxTicketCapacity);
    }

    /**
     * Starts vendor threads that simulate the periodic release of tickets.
     * @param vendorCount       The number of vendor threads to start.
     * @param ticketReleaseRate The rate at which tickets are released by vendors.
     * @param ticketsPerRelease The number of tickets each vendor will release per interval.
     */
    @PostMapping("/startVendorThreads")
    public void startVendorThreads(@RequestParam int vendorCount, @RequestParam int ticketReleaseRate, @RequestParam int ticketsPerRelease) {
        ticketService.startVendorThreads(vendorCount, ticketReleaseRate, ticketsPerRelease);
    }

    /**
     * Starts customer threads simulating ticket purchasing activity.
     * @param customerCount        The number of customer threads to start.
     * @param customerRetrievalRate The rate at which customers attempt to purchase tickets.
     * @param ticketsPerPurchase  The number of tickets each customer attempts to purchase at a time.
     */
    @PostMapping("/startCustomerThreads")
    public void startCustomerThreads(@RequestParam int customerCount, @RequestParam int customerRetrievalRate, @RequestParam int ticketsPerPurchase) {
        ticketService.startCustomerThreads(customerCount, customerRetrievalRate, ticketsPerPurchase);
    }

    /**
     * Stops all currently active vendor threads.
     */
    @PostMapping("/stopVendorThreads")
    public void stopVendorThreads() {
        ticketService.stopVendorThreads();
    }

    /**
     * Stops all currently active customer threads.
     */
    @PostMapping("/stopCustomerThreads")
    public void stopCustomerThreads() {
        ticketService.stopCustomerThreads();
    }

    /**
     * Adds a new vendor thread to simulate ticket release behavior.
     * @param ticketReleaseRate The rate at which tickets are released by the vendor.
     * @param ticketsPerRelease The number of tickets released per interval by the vendor.
     */
    @PostMapping("/addVendor")
    public void addVendor(@RequestParam int ticketReleaseRate, @RequestParam int ticketsPerRelease) {
        ticketService.addVendor(ticketReleaseRate, ticketsPerRelease);
    }

    /**
     * Removes an existing vendor thread from simulation.
     */
    @PostMapping("/removeVendor")
    public void removeVendor() {
        ticketService.removeVendor();
    }

    /**
     * Adds a new customer thread to simulate ticket purchasing behavior.
     * @param retrievalInterval The interval at which the customer tries to purchase tickets.
     * @param ticketsPerPurchase The number of tickets each customer attempts to purchase in a single transaction.
     */
    @PostMapping("/addCustomer")
    public void addCustomer(@RequestParam int retrievalInterval, @RequestParam int ticketsPerPurchase) {
        ticketService.addCustomer(retrievalInterval, ticketsPerPurchase);
    }

    /**
     * Removes an existing customer thread from simulation.
     */
    @PostMapping("/removeCustomer")
    public void removeCustomer() {
        ticketService.removeCustomer();
    }

    /**
     * Retrieves the current status of the ticket system.
     * @return A string representing the ticket system's status.
     */
    @GetMapping("/status")
    public String getTicketStatus() {
        return ticketService.getTicketStatus();
    }

    /**
     * Retrieves the application logs.
     * @return The logs as plain text.
     */
    @GetMapping("/logs")
    public ResponseEntity<String> getLogs() {
        String logs = ticketService.getLogs();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(logs);
    }
}
