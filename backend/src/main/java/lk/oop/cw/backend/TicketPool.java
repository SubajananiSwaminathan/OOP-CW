package lk.oop.cw.backend;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.logging.Logger;

/**
 * Manages the shared ticket pool, handling ticket operations and concurrency with proper locking mechanisms.
 * <p>
 * This class serves as a thread-safe resource for managing ticket inventory, supporting operations such as
 * adding tickets by vendors, allowing customers to remove tickets (purchase), and logging activities for monitoring purposes.
 * It implements synchronization to ensure safe concurrent access.
 * </p>
 */
@Configuration
@Component
public class TicketPool {
    private final List<Ticket> tickets;
    private final int maxTicketCapacity;
    private int totalTicketsReleased = 0;
    private final int totalTickets;

    private final List<String> logs;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    private static final Logger logger = LoggingConfig.getLogger();

    /**
     * Default constructor to initialize the ticket pool with default values.
     */
    public TicketPool() {
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.logs = Collections.synchronizedList(new ArrayList<>());
        this.maxTicketCapacity = 50;
        this.totalTickets = 500;
    }

    /**
     * Parameterized constructor for initializing the ticket pool with custom limits.
     * @param totalTickets     The total number of tickets allowed in the system.
     * @param maxTicketCapacity The maximum number of tickets that can be in the pool at a time.
     */
    public TicketPool(int totalTickets, int maxTicketCapacity) {
        this.tickets = Collections.synchronizedList(new ArrayList<>());
        this.logs = Collections.synchronizedList(new ArrayList<>());
        this.totalTickets = totalTickets;
        this.maxTicketCapacity = maxTicketCapacity;
    }

    /**
     * Waits if the ticket pool is either full or empty depending on the operation being performed.
     * @param isAdding Indicates whether the method is trying to add tickets or remove tickets.
     */
    private void waitIfNeeded(boolean isAdding) {
        lock.lock();
        try {
            while ((isAdding && tickets.size() >= maxTicketCapacity) || (!isAdding && tickets.isEmpty())) {
                if (isAdding) {
                    notFull.await();
                } else {
                    notEmpty.await();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Adds tickets to the ticket pool from a specific vendor while respecting the limits of ticket availability.
     * @param vendorId   ID of the vendor releasing tickets.
     * @param eventId    ID of the event the tickets are associated with.
     * @param eventName  Name of the event.
     * @param price      Price of the tickets being added.
     * @param ticketsToAdd Number of tickets to add.
     * @param category  Category of the tickets being added.
     */
    public void addTickets(String vendorId, String eventId, String eventName, double price, int ticketsToAdd, String category) {
        lock.lock();
        try {
            if (totalTicketsReleased >= totalTickets) {
                addLog("Total ticket limit reached. No more tickets can be added.");
                return;
            }

            waitIfNeeded(true);

            int ticketsRemaining = totalTickets - totalTicketsReleased;
            int ticketsToActuallyAdd = Math.min(ticketsToAdd, Math.min(ticketsRemaining, maxTicketCapacity - tickets.size()));

            for (int i = 0; i < ticketsToActuallyAdd; i++) {
                Ticket ticket = new Ticket(vendorId, eventId, eventName, price, category);
                tickets.add(ticket);
            }

            totalTicketsReleased += ticketsToActuallyAdd;
            addLog(vendorId + " added " + ticketsToActuallyAdd + " tickets for " + eventName + ". Total in pool: " + tickets.size());
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Allows a customer to remove (purchase) a ticket from the pool.
     * @param customerId ID of the customer purchasing the ticket.
     */
    public void removeTicket(String customerId) {
        lock.lock();
        try {
            waitIfNeeded(false);

            if (!tickets.isEmpty()) {
                Ticket ticket = tickets.remove(0);
                addLog(customerId + " purchased a ticket for event '" + ticket.getEventName() + "' (Vendor: " + ticket.getVendorId() +
                        ", Price: $" + ticket.getPrice() + ", Category: " + ticket.getCategory() + "). " +
                        "Tickets remaining in pool: " + tickets.size());
                notFull.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Checks if the ticket pool has run out of tickets.
     * @return true if the pool is sold out; false otherwise.
     */
    public boolean isSoldOut() {
        lock.lock();
        try {
            return totalTicketsReleased >= totalTickets && tickets.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Retrieves the number of remaining tickets currently in the pool.
     * @return Number of remaining tickets.
     */
    public int getRemainingTickets() {
        lock.lock();
        try {
            return tickets.size();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Logs an activity message to the system logs and application logger.
     * @param message Message to log.
     */
    public void addLog(String message) {
        logs.add(message);
        logger.info(message);
    }

    /**
     * Retrieves the list of logged messages for monitoring purposes.
     * @return A list of logged messages.
     */
    public List<String> getLogs() {
        return new ArrayList<>(logs);
    }
}
