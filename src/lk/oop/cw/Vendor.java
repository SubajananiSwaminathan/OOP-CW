package lk.oop.cw;

public class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final String vendorId;
    private final int totalTickets;
    private final int releaseInterval;

    public Vendor(TicketPool ticketPool, String vendorId, int totalTickets, int releaseInterval) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.totalTickets = totalTickets;
        this.releaseInterval = releaseInterval;
    }

    @Override
    public void run() {
        while (!ticketPool.allTicketsAdded()) {
            if (ticketPool.addTickets(1)) {
                System.out.println(vendorId + " released a ticket.");
            }
            try {
                Thread.sleep(releaseInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println(vendorId + " has stopped releasing tickets.");
    }
}

