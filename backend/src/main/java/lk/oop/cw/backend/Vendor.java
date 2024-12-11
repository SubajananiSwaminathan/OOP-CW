package lk.oop.cw.backend;

/**
 * Represents a vendor that periodically releases tickets into the ticket pool.
 * <p>
 * This class implements {@link Runnable} to simulate a vendor's behavior in a multithreaded
 * environment. Vendors release tickets at a fixed interval until the ticket pool is sold out
 * or the thread is interrupted.
 * </p>
 */
public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int releaseInterval;
    private final int ticketsPerRelease;
    private final String vendorId;
    private final String eventId;
    private final String eventName;
    private final double price;
    private final String category;

    private static int vendorCounter = 1;

    /**
     * Constructs a Vendor instance with specified properties for simulation.
     * @param ticketPool       The shared ticket pool to add tickets to.
     * @param releaseInterval  The interval (in milliseconds) at which tickets are released.
     * @param ticketsPerRelease The number of tickets released at each interval.
     * @param eventId          The unique event ID associated with this vendor's tickets.
     * @param eventName        The name of the event.
     * @param price            The price of the tickets this vendor releases.
     * @param category        The category of tickets being released by this vendor.
     */
    public Vendor(TicketPool ticketPool, int releaseInterval, int ticketsPerRelease, String eventId, String eventName, double price, String category) {
        this.ticketPool = ticketPool;
        this.releaseInterval = releaseInterval;
        this.ticketsPerRelease = ticketsPerRelease;
        this.vendorId = "Vendor-" + vendorCounter++; // Generate vendorId like vendor-1, vendor-2, etc.
        this.eventId = eventId;
        this.eventName = eventName;
        this.price = price;
        this.category = category;
    }

    /**
     * Simulates the periodic release of tickets into the shared ticket pool until the system is sold out
     * or the thread is interrupted.
     */
    @Override
    public void run() {
        try {
            while (!ticketPool.isSoldOut()) {
                ticketPool.addTickets(vendorId, eventId, eventName, price, ticketsPerRelease, category);
                Thread.sleep(releaseInterval);
            }
        } catch (InterruptedException e) {
            ticketPool.addLog("Vendor interrupted");
        }
        ticketPool.addLog(vendorId + " stopped as all tickets are released.");
    }
}
