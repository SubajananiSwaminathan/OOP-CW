package lk.oop.cw;

public class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final String customerId;
    private final int retrievalInterval;

    public Customer(TicketPool ticketPool, String customerId, int retrievalInterval) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
        this.retrievalInterval = retrievalInterval;
    }

    @Override
    public void run() {
        while (!ticketPool.allTicketsSold()) {
            if (ticketPool.removeTicket()) {
                System.out.println(customerId + " purchased a ticket.");
            } else {
                System.out.println(customerId + " attempted to buy a ticket, but none were available.");
            }
            try {
                Thread.sleep(retrievalInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(customerId + " has stopped purchasing tickets.");
    }
}
