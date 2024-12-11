README.md

# Ticket Management System

The Ticket Management System is a simulation based multi-threaded application designed to manage ticket sales for events. It features a backend for handling vendors and ticket releases, a Command-Line Interface (CLI) for configuration, and a Graphical User Interface (GUI) for user-friendly interactions. The system supports adding vendors at runtime, assigning event-specific details, and managing ticket sales efficiently.


## Setup Instructions

#### Prerequisites

Before setting up the system, ensure the following tools are installed on your machine:

1. Java 17 or above
2. Node.js 16 or above
3. Spring Boot for Backend
4. Angular CLI for Frontend

#### Backend Setup (Spring Boot)

1. Navigate to the backend folder
```
cd backend
```
2. Build the backend application
```
mvn clean install
```
3. Run the Spring Boot application
```
mvn spring-boot:run
```
The backend server should now be running at http://localhost:8080.

Or you can use the built-in "run" function in your IDE to start BackendApplication.java

#### Frontend Setup (Angular)

1. Navigate to the frontend folder
```
cd frontend
```
2. Install the Angular dependencies:
```
npm install
```
3. Run the Angular development server
```
ng serve
```

The Angular frontend should now be accessible at http://localhost:4200.

Or you can use the built-in "run" function in your IDE to start Angular CLI.


## Usage

### Configuring and Starting the System

#### CLI Usage:

1. Start the backend server (BackendApplication.java).
2. Run and start the CLI (Configuration.java)
3. Enter values to CLI prompts to configure the ticketpool. Prompts include:
```
totalTickets: Total number of tickets to be sold during the session
ticketReleaseRate: The time interval between 2 releases by vendors (in milliseconds)
customerRetrievalRate: The time interval between 2 purchases by customers (in milliseconds)
maxTicketCapacity: Maximum number of tickets that the ticketpool can hold at a point of time
```
Once you finish configuring the ticketpool, you can simulate the process. The following prompts will be displayed:

1. Start Customer Threads
- This action will prompt the system to initiate the simulation of ticket releases. You will be asked to enter 2 values
```
Enter number of Vendors: This will initiate the process with the specified number of vendors
Enter Tickets per Release: The vendors will release the specified number of tickets in each release
```
2. Start Customer Threads
- This action will prompt the system to initiate the simulation of ticket purchases. You will be asked to enter 2 values
```
Enter number of Customers: This will initiate the process with the specified number of customers
Enter Tickets per Release: The customers will buy the specified number of tickets during each purchase
```
3. Show Ticketpool Status
- This will return the status of the ticketpool at that given point of time
4. Stop System
- Gracefully exits the system

#### GUI Usage:

1. Start the backend server (BackendApplication.java).
2. Run the below command from the frontend folder to render the GUI.
```
ng serve
```
3. Access the GUI in your browser (http://localhost:4200).

The GUI includes 4 components:
```
1. Configuration Form: Used to configure the ticketpool. Same as the configuration in CLI.
2. Ticket Display: Displays the amount of tickets in the ticketpool at the moment.
3. Control Panel: Includes input fields to enter number of Vendors and Customers and to enter tickets per release and purchase.
Additionally, it includes buttons to start/stop vendor/customer threads and gives the additional option to add/remove a vendor/customer during runtime.
4. Log Display: Displays the logs of the actions happening in the simulation. (Ticket releases / purchases) 
```


## API Reference

#### Base URL
All API endpoints are prefixed with ```/api/tickets```

- Base URL: ```http://localhost:8080/api/tickets```

#### CORS Configuration
- The backend allows cross-origin requests from the frontend running on ```http://localhost:4200```

### API endpoints

**1. Add Tickets via Vendor**

This endpoint allows vendors to add a certain number of tickets for their specific event.
```
POST /api/tickets/add/{tickets}/{vendorId}/{eventId}/{eventName}/{price}/{category}
```

| Parameter | Type | Description |
| -------- | ------- | ---------- |
| tickets | Integer | Number of tickets to be added. |
| vendorId | String | ID of the vendor adding the tickets. |
| eventId | String | ID of the event. |
| eventName | String | Name of the event. |
| price | Double | Price of each ticket. | 
| category | String | Category of the event. |

Example request
```
POST http://localhost:8080/api/tickets/add/10/Vendor-1/Event-1/Concert/50.0/VIP
```

**2. Purchase Ticket via Customer**

This endpoint allows a customer to purchase a ticket. It will remove a ticket from the system when a customer buys it.
```
POST /api/tickets/remove/{customerId}
```

| Parameter | Type | Description |
| -------- | ------- | ---------- |
| customerId | String | ID of the customer purchasing the ticket |


Example request
```
POST http://localhost:8080/api/tickets/remove/Customer-1
```

**3. Configure Ticket Pool**

This endpoint initializes the ticket pool with a total number of tickets and a maximum capacity per event.
```
POST /api/tickets/configure
```

| Parameter | Type | Description |
| -------- | ------- | ---------- |
| totalTickets | Integer | Total number of tickets available. |
| maxTicketCapacity | Integer | Maximum number of tickets that the ticketpool can hold |


Example request
```
POST http://localhost:8080/api/tickets/configure?totalTickets=1000&maxTicketCapacity=500
```

**4. Start Vendor Threads**

This endpoint starts vendor threads with specified parameters for ticket release.
```
POST /api/tickets/startVendorThreads
```

| Parameter | Type | Description |
| -------- | ------- | ---------- |
| vendorCount | Integer | Number of vendor threads to start. |
| ticketReleaseRate | Integer | Rate at which tickets are released by vendors. |
| ticketsPerRelease | Integer | Number of tickets each vendor releases in each cycle. |

Example request
```
POST http://localhost:8080/api/tickets/startVendorThreads?vendorCount=5&ticketReleaseRate=10&ticketsPerRelease=20
```

**5. Start Customer Threads**

This endpoint starts customer threads with specified parameters for ticket purchase.
```
POST /api/tickets/startCustomerThreads
```

| Parameter | Type | Description |
| -------- | ------- | ---------- |
| customerCount | Integer | Number of customer threads to start. |
| customerRetrievalRate | Integer | Rate at which tickets are purchased by customers. |
| ticketsPerPurchase | Integer | Number of tickets each customer purchases in each cycle. |

Example request
```
POST http://localhost:8080/api/tickets/startCustomerThreads?customerCount=10&customerRetrievalRate=5&ticketsPerPurchase=2
```

**6. Stop Vendor Threads**

This endpoint stops all vendor threads.
```
POST /api/tickets/stopVendorThreads
```
Example request
```
POST http://localhost:8080/api/tickets/stopVendorThreads
```

**7. Stop Customer Threads**

This endpoint stops all customer threads.
```
POST /api/tickets/stopCustomerThreads
```
Example request
```
POST http://localhost:8080/api/tickets/stopCustomerThreads
```

**8. Add Vendor**

This endpoint allows dynamic addition of a vendor.
```
POST /api/tickets/addVendor
```

| Parameter | Type | Description |
| -------- | ------- | ---------- |
| customerRetrievalRate | Integer | Rate at which tickets are purchased by customers. |
| ticketsPerPurchase | Integer | Number of tickets each customer purchases in each cycle. |

Example request
```
POST http://localhost:8080/api/tickets/addVendor?ticketReleaseRate=1500&ticketsPerRelease=30
```

**9. Remove Vendor**

This endpoint allows dynamic removal of a vendor.
```
POST /api/tickets/removeVendor
```
Example request
```
POST http://localhost:8080/api/tickets/removeVendor
```

**10. Add Customer**

This endpoint allows dynamic addition of a customer.
```
POST /api/tickets/addCustomer
```

| Parameter | Type | Description |
| -------- | ------- | ---------- |
| retrievalInterval | Integer | Rate at which tickets are purchased by customers. |
| ticketsPerPurchase | Integer | Number of tickets each customer purchases in each cycle. |

Example request
```
POST http://localhost:8080/api/tickets/addCustomer?retrievalInterval=2000&ticketsPerPurchase=2
```

**11. Remove Customer**

This endpoint allows dynamic removal of a customer.
```
POST /api/tickets/removeCustomer
```
Example request
```
POST http://localhost:8080/api/tickets/removeCustomer
```

**12. Get Ticket Status**

This endpoint retrieves the current status of the ticket pool.
```
GET /api/tickets/status
```
Example request
```
GET http://localhost:8080/api/tickets/status
```

**13. Get Logs**

This endpoint retrieves the logs for ticket operations.
```
GET /api/tickets/logs
```
Example request
```
GET http://localhost:8080/api/tickets/logs
```
