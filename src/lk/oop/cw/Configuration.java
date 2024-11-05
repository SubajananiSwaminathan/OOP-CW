package lk.oop.cw;

//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;

public class Configuration {
    private int totalTickets;
    private int ticketReleaseRate;
    private int customerRetrievalRate;
    private int maxTicketCapacity;

    public Configuration(int totalTickets, int ticketReleaseRate, int customerRetrievalRate, int maxTicketCapacity) {
        setTotalTickets(totalTickets);
        setTicketReleaseRate(ticketReleaseRate);
        setCustomerRetrievalRate(customerRetrievalRate);
        setMaxTicketCapacity(maxTicketCapacity);
    }

    public void setTotalTickets(int totalTickets) {
        if (totalTickets < 0) throw new IllegalArgumentException("Total tickets must be non-negative.");
        this.totalTickets = totalTickets;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        if (ticketReleaseRate <= 0) throw new IllegalArgumentException("Ticket release rate must be positive.");
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        if (customerRetrievalRate <= 0) throw new IllegalArgumentException("Customer retrieval rate must be positive.");
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        if (maxTicketCapacity <= 0) throw new IllegalArgumentException("Max ticket capacity must be positive.");
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    @Override
    public String toString() {
        return String.format("Configuration [totalTickets=%d, ticketReleaseRate=%d, customerRetrievalRate=%d, maxTicketCapacity=%d]",
                totalTickets, ticketReleaseRate, customerRetrievalRate, maxTicketCapacity);
    }
}


//    public void saveToJSON(String filePath) throws IOException {
//        Gson gson = new GsonBuilder().setPrettyPrinting().create();
//        try (FileWriter writer = new FileWriter(filePath)) {
//            gson.toJson(this, writer);
//        }
//    }

//    public static Configuration loadFromJSON(String filePath) throws IOException {
//        Gson gson = new Gson();
//        try (FileReader reader = new FileReader(filePath)) {
//            return gson.fromJson(reader, Configuration.class);
//        }
//    }