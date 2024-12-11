package lk.oop.cw.backend;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a ticket with details about the event, vendor, and pricing.
 * <p>
 * This class serves as a data model for a ticket, encapsulating information such as the vendor ID,
 * event ID, event name, price, and category. It provides standard getter and setter methods for each
 * property to allow controlled access and modification.
 * </p>
 */
@Setter
@Getter
public class Ticket {
    private String vendorId;
    private String eventId;
    private String eventName;
    private double price;
    private String category;

    /**
     * Constructs a new Ticket instance with specified details.
     *
     * @param vendorId   The identifier of the vendor associated with this ticket.
     * @param eventId    The identifier of the event for which this ticket is issued.
     * @param eventName  The name of the event.
     * @param price      The price of the ticket.
     * @param category   The category of the ticket (e.g., VIP, General Admission).
     */
    public Ticket(String vendorId, String eventId, String eventName, double price, String category) {
        this.vendorId = vendorId;
        this.eventId = eventId;
        this.eventName = eventName;
        this.price = price;
        this.category = category;
    }

}
