package lk.oop.cw;

public class Test {
    public static void main(String[] args) {
        Configuration config = CLI.setupConfiguration();

        TicketPool ticketPool = new TicketPool(config.getMaxTicketCapacity());

        Thread vendor1 = new Thread(new Vendor(ticketPool, "Vendor1", config.getTotalTickets(), config.getTicketReleaseRate()));
        Thread vendor2 = new Thread(new Vendor(ticketPool, "Vendor2", config.getTotalTickets(), config.getTicketReleaseRate()));

        Thread customer1 = new Thread(new Customer(ticketPool, "Customer1", config.getCustomerRetrievalRate()));
        Thread customer2 = new Thread(new Customer(ticketPool, "Customer2", config.getCustomerRetrievalRate()));
        Thread customer3 = new Thread(new Customer(ticketPool, "Customer3", config.getCustomerRetrievalRate()));

        vendor1.start();
        vendor2.start();

        customer1.start();
        customer2.start();
        customer3.start();
    }
}
