package lk.oop.cw.backend;

/**
 * Represents a customer that simulates ticket purchasing by interacting with a {@link TicketPool}.
 * <p>
 * This class implements {@link Runnable} to simulate customer behavior in a multithreaded environment,
 * attempting to purchase tickets at a regular interval until tickets are sold out.
 * </p>
 */
public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;
    private final int retrievalInterval;
    private final int ticketsPerPurchase;

    private static int customerCounter = 1;

    /**
     * Constructs a new Customer instance.
     * @param ticketPool         The shared {@link TicketPool} instance this customer will interact with.
     * @param retrievalInterval  The time (in milliseconds) the customer waits between attempts to retrieve tickets.
     * @param ticketsPerPurchase The number of tickets this customer attempts to purchase in each attempt.
     */
    public Customer(TicketPool ticketPool, int retrievalInterval, int ticketsPerPurchase) {
        this.ticketPool = ticketPool;
        this.retrievalInterval = retrievalInterval;
        this.ticketsPerPurchase = ticketsPerPurchase;
        this.customerId = "Customer-" + customerCounter++; // Generate customerId like customer-1, customer-2, etc.
    }

    /**
     * Simulates the customer's ticket purchasing behavior.
     * <p>
     * The customer continuously attempts to remove tickets from the ticket pool until the tickets are sold out.
     * After attempting to purchase the specified number of tickets, the customer waits for the configured interval
     * before retrying. If interrupted during this simulation, it logs the interruption and exits the loop.
     * </p>
     */
    @Override
    public void run() {
        try {
            while (!ticketPool.isSoldOut()) {
                for (int i = 0; i < ticketsPerPurchase; i++) {
                    ticketPool.removeTicket(customerId);
                    if (ticketPool.isSoldOut()) break;
                }
                Thread.sleep(retrievalInterval);
            }
        } catch (InterruptedException e) {
            ticketPool.addLog("Customer interrupted");
        }
        ticketPool.addLog(customerId + " stopped as all tickets are sold.");
    }
}
