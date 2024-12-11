package lk.oop.cw.backend;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for managing ticket-related operations and simulating ticket pool interactions.
 * <p>
 * This service is responsible for initializing the ticket pool, managing vendor and customer threads,
 * adding/removing tickets, and retrieving system status or logs. It interacts with the underlying
 * {@link TicketPool} instance to perform core operations.
 * </p>
 */
@Service
public class TicketService {
    private TicketPool ticketPool;

    private final List<Thread> vendorThreads = new ArrayList<>();
    private final List<Thread> customerThreads = new ArrayList<>();

    /**
     * Constructs the service with a given TicketPool instance.
     * @param ticketPool The ticket pool to be managed by this service.
     */
    public TicketService(TicketPool ticketPool) {
        this.ticketPool = ticketPool;
    }

    /**
     * Adds tickets to the system by delegating to the {@link TicketPool}.
     * @param vendorId   The vendor's identifier.
     * @param eventId    The event's identifier.
     * @param eventName  The name of the event.
     * @param price      The price of the tickets.
     * @param ticketsToAdd The number of tickets to add.
     * @param category   The ticket category.
     */
    public void addTickets(String vendorId, String eventId, String eventName, double price, int ticketsToAdd, String category) {
        ticketPool.addTickets(vendorId, eventId, eventName, price, ticketsToAdd, category);
    }

    /**
     * Removes a ticket from the pool associated with a specific customer.
     * @param customerId The customer ID to remove the ticket for.
     */
    public void removeTicket(String customerId) {
        ticketPool.removeTicket(customerId);
    }

    /**
     * Initializes the ticket pool with a total number of tickets and a maximum ticket capacity.
     * @param totalTickets      The total number of tickets.
     * @param maxTicketCapacity The maximum ticket capacity allowed.
     */
    public void initializeTicketPool(int totalTickets, int maxTicketCapacity) {
        this.ticketPool = new TicketPool(totalTickets, maxTicketCapacity);
    }

    /**
     * Starts threads simulating vendors releasing tickets into the system.
     * @param vendorCount       The number of vendor threads to start.
     * @param ticketReleaseRate The rate at which each vendor releases tickets.
     * @param ticketsPerRelease The number of tickets each vendor will release per interval.
     */
    public void startVendorThreads(int vendorCount, int ticketReleaseRate, int ticketsPerRelease) {
        String[] eventNames = {"Music Concert", "Sports Event", "Theater Play", "Tech Expo", "Art Show"};
        String[] categories = {"VIP", "Regular", "Balcony"};
        double[] prices = {100, 150, 200, 250, 300};

        for (int i = 0; i < vendorCount; i++) {
            String eventId = "Event-" + ((i % eventNames.length) + 1);
            String eventName = eventNames[i % eventNames.length];
            String category = categories[i % categories.length];
            double price = prices[i % prices.length];

            Vendor vendor = new Vendor(ticketPool, ticketReleaseRate, ticketsPerRelease, eventId, eventName, price, category);
            Thread vendorThread = new Thread(vendor, "Vendor-" + (vendorThreads.size() + 1));
            vendorThreads.add(vendorThread);
            vendorThread.start();
        }
    }

    /**
     * Starts threads simulating customers attempting to purchase tickets.
     * @param customerCount        The number of customer threads to start.
     * @param customerRetrievalRate The rate at which customers try to purchase tickets.
     * @param ticketsPerPurchase  The number of tickets each customer tries to purchase at a time.
     */
    public void startCustomerThreads(int customerCount, int customerRetrievalRate, int ticketsPerPurchase) {
        for (int i = 0; i < customerCount; i++) {
            Customer customer = new Customer(ticketPool, customerRetrievalRate, ticketsPerPurchase);
            Thread customerThread = new Thread(customer, "Customer-" + (customerThreads.size() + 1));
            customerThreads.add(customerThread);
            customerThread.start();
        }
    }

    /**
     * Stops all currently active vendor threads.
     */
    public void stopVendorThreads() {
        for (Thread vendorThread : vendorThreads) {
            vendorThread.interrupt();
        }
        vendorThreads.clear();
    }

    /**
     * Stops all currently active customer threads.
     */
    public void stopCustomerThreads() {
        for (Thread customerThread : customerThreads) {
            customerThread.interrupt();
        }
        customerThreads.clear();
    }

    /**
     * Adds a new vendor thread with the given release rate and number of tickets per release.
     * @param ticketReleaseRate The rate at which tickets are released by this vendor.
     * @param ticketsPerRelease The number of tickets the vendor releases per interval.
     */
    public void addVendor(int ticketReleaseRate, int ticketsPerRelease) {
        try {
            startVendorThreads(1, ticketReleaseRate, ticketsPerRelease);
            System.out.println("Successfully added new vendor.");
        } catch (Exception e) {
            System.err.println("Failed to add vendor: " + e.getMessage());
        }
    }

    /**
     * Removes a single vendor thread from the system.
     */
    public void removeVendor() {
        if (!vendorThreads.isEmpty()) {
            Thread vendorThread = vendorThreads.remove(vendorThreads.size() - 1);
            vendorThread.interrupt();
        }
    }

    /**
     * Adds a new customer thread with specified retrieval interval and tickets per purchase.
     * @param retrievalInterval The interval at which the customer attempts ticket retrieval.
     * @param ticketsPerPurchase The number of tickets attempted per transaction.
     */
    public void addCustomer(int retrievalInterval, int ticketsPerPurchase) {
        Customer customer = new Customer(ticketPool, retrievalInterval, ticketsPerPurchase);
        Thread customerThread = new Thread(customer, "Customer-" + (customerThreads.size() + 1));
        customerThreads.add(customerThread);
        customerThread.start();
    }

    /**
     * Removes a single customer thread from the system.
     */
    public void removeCustomer() {
        if (!customerThreads.isEmpty()) {
            Thread customerThread = customerThreads.remove(customerThreads.size() - 1);
            customerThread.interrupt();
        }
    }

    /**
     * Retrieves the current status of tickets remaining in the system.
     * @return A string representing the number of tickets remaining.
     */
    public String getTicketStatus() {
        int remainingTickets = ticketPool.getRemainingTickets();
        return String.format("Tickets Remaining: %d", remainingTickets);
    }

    /**
     * Retrieves application logs related to ticket activities.
     * @return The logs as a single concatenated string.
     */
    public String getLogs() {
        return String.join("\n", ticketPool.getLogs());
    }
}
