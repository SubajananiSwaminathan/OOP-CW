package lk.oop.cw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketPool {
    private final int maxCapacity;
    private final List<String> tickets = Collections.synchronizedList(new ArrayList<>());
    private int ticketsSold = 0;
    private int ticketsAdded = 0;

    public TicketPool(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean addTickets(int count) {
        synchronized (tickets) {
            if (ticketsAdded >= maxCapacity) {
                return false;
            }
            for (int i = 0; i < count && ticketsAdded < maxCapacity; i++) {
                tickets.add("Ticket-" + (ticketsAdded + 1));
                ticketsAdded++;
            }
            tickets.notifyAll();
            return true;
        }
    }

    public boolean removeTicket() {
        synchronized (tickets) {
            if (tickets.isEmpty()) {
                return false;
            }
            tickets.remove(0);
            ticketsSold++;
            tickets.notifyAll();
            return true;
        }
    }

    public boolean allTicketsSold() {
        synchronized (tickets) {
            return ticketsSold >= maxCapacity;
        }
    }

    public boolean allTicketsAdded() {
        synchronized (tickets) {
            return ticketsAdded >= maxCapacity;
        }
    }
}
