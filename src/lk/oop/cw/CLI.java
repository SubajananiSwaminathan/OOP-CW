package lk.oop.cw;

import java.util.Scanner;

public class CLI {
    public static Configuration setupConfiguration() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Total Number of Tickets:");
        int totalTickets = scanner.nextInt();

        System.out.println("Enter Ticket Release Rate (milliseconds):");
        int ticketReleaseRate = scanner.nextInt();

        System.out.println("Enter Customer Retrieval Rate (milliseconds):");
        int customerRetrievalRate = scanner.nextInt();

        System.out.println("Enter Maximum Ticket Capacity:");
        int maxTicketCapacity = scanner.nextInt();

        return new Configuration(totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }
}
