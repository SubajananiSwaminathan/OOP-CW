import {Component, Input} from '@angular/core';
import {lastValueFrom} from 'rxjs';
import {environment} from '../../../environments/environment';
import {HttpClient} from '@angular/common/http';
import {FormsModule} from '@angular/forms';
import {NgIf} from '@angular/common';

/**
 * This component provides control options for managing vendor and customer threads,
 * as well as their associated configurations. It communicates with backend API endpoints
 * for actions like starting/stopping threads, adding/removing vendors and customers, and
 * handles potential error messages related to these operations.
 */
@Component({
  selector: 'app-control-panel',
  standalone: true,
  imports: [
    FormsModule,
    NgIf
  ],
  templateUrl: './control-panel.component.html',
  styleUrl: './control-panel.component.css'
})

export class ControlPanelComponent {
  /** Input value to control ticket release rate from parent component */
  @Input() public ticketReleaseRate: number = 0;

  /** Input value to control customer retrieval rate from parent component */
  @Input() public customerRetrievalRate: number = 0;

  protected vendorRunning: boolean = false;
  protected customerRunning: boolean = false;
  protected errorMessage: string = '';

  protected vendorCount: number = 0;
  protected customerCount: number = 0;
  protected ticketsPerRelease: number = 0;
  protected ticketsPerPurchase: number = 0;

  /**
   * Creates an instance of ControlPanelComponent.
   * Injects HttpClient for server communication.
   *
   * @param http - The HttpClient used for HTTP operations
   */
  public constructor(private http: HttpClient) {}

  /**
   * Starts vendor threads if valid data is provided.
   * Sends a request to the backend API endpoint to initiate vendor threads.
   */
  public startVendor() {
    if (this.vendorCount <= 0 || this.ticketReleaseRate <= 0 || this.ticketsPerRelease <= 0) {
      this.errorMessage = 'Please specify valid values for vendors and ticket release rate.';
      return;
    }

    this.errorMessage = '';

    const url = `${environment.apiUrl}/api/tickets/startVendorThreads?vendorCount=${this.vendorCount}&ticketReleaseRate=${this.ticketReleaseRate}&ticketsPerRelease=${this.ticketsPerRelease}`;
    lastValueFrom(this.http.post(url, {}))
      .then(response => {
        console.log('Vendor threads started successfully', response);
        this.vendorRunning = true;
      })
      .catch(error => {
        console.error('Error starting vendor threads', error);
        this.errorMessage = 'Failed to start vendor threads. Please try again.';
      });
  }

  /**
   * Stops vendor threads.
   * Sends a request to the backend to terminate vendor threads.
   */
  public stopVendor() {
    this.errorMessage = '';
    lastValueFrom(this.http.post(`${environment.apiUrl}/api/tickets/stopVendorThreads`, {}))
      .then(response => {
        console.log('Vendor threads stopped successfully', response);
        this.vendorRunning = false;
      })
      .catch(error => {
        console.error('Error stopping vendor threads', error);
        this.errorMessage = 'Failed to stop vendor threads. Please try again.';
      });
  }

  /**
   * Starts customer threads if valid data is provided.
   * Sends a request to the backend to initiate customer threads.
   */
  public startCustomer() {
    if (this.customerCount <= 0 || this.customerRetrievalRate <= 0 || this.ticketsPerPurchase <= 0) {
      this.errorMessage = 'Please specify valid values for customers and retrieval rate.';
      return;
    }

    this.errorMessage = '';

    const url = `${environment.apiUrl}/api/tickets/startCustomerThreads` +
      `?customerCount=${this.customerCount}&customerRetrievalRate=${this.customerRetrievalRate}&ticketsPerPurchase=${this.ticketsPerPurchase}`;

    lastValueFrom(this.http.post(url, {}))
      .then(response => {
        console.log('Customer threads started successfully', response);
        this.customerRunning = true;
      })
      .catch(error => {
        console.error('Error starting customer threads', error);
        this.errorMessage = 'Failed to start customer threads. Please try again.';
      });
  }

  /**
   * Stops customer threads.
   * Sends a request to the backend to terminate customer threads.
   */
  public stopCustomer() {
    this.errorMessage = '';
    lastValueFrom(this.http.post(`${environment.apiUrl}/api/tickets/stopCustomerThreads`, {}))
      .then(response => {
        console.log('Customer threads stopped successfully', response);
        this.customerRunning = false;
      })
      .catch(error => {
        console.error('Error stopping customer threads', error);
        this.errorMessage = 'Failed to stop customer threads. Please try again.';
      });
  }

  /**
   * Adds a vendor thread via the backend.
   * Sends ticket release rate and tickets per release to the server.
   */
  public addVendor() {
    if (this.ticketReleaseRate <= 0 || this.ticketsPerRelease <= 0) {
      this.errorMessage = 'Please specify valid values for ticket release rate and tickets per release.';
      return;
    }

    this.errorMessage = '';

    const url = `${environment.apiUrl}/api/tickets/addVendor?ticketReleaseRate=${this.ticketReleaseRate}&ticketsPerRelease=${this.ticketsPerRelease}`;

    lastValueFrom(this.http.post(url, {}))
      .then(response => {
        console.log('Vendor added successfully', response);
      })
      .catch(error => {
        console.error('Error adding vendor', error);
        this.errorMessage = 'Failed to add vendor. Please try again.';
      });
  }

  /**
   * Removes a vendor thread.
   * Sends a request to remove a vendor from the backend system.
   */
  public removeVendor() {
    this.errorMessage = '';
    lastValueFrom(this.http.post(`${environment.apiUrl}/api/tickets/removeVendor`, {}))
      .then(response => {
        console.log('Vendor removed successfully', response);
      })
      .catch(error => {
        console.error('Error removing vendor', error);
        this.errorMessage = 'Failed to remove vendor. Please try again.';
      });
  }

  /**
   * Adds a customer thread via the backend.
   * Sends customer retrieval rate and tickets per purchase to the server.
   */
  public addCustomer() {
    this.errorMessage = '';
    lastValueFrom(this.http.post(`${environment.apiUrl}/api/tickets/addCustomer?customerRetrievalRate=${this.customerRetrievalRate}&ticketsPerPurchase=${this.ticketsPerPurchase}`, {}))
      .then(response => {
        console.log('Customer added successfully', response);
      })
      .catch(error => {
        console.error('Error adding customer', error);
        this.errorMessage = 'Failed to add customer. Please try again.';
      });
  }

  /**
   * Removes a customer thread.
   * Sends a request to remove a customer from the backend system.
   */
  public removeCustomer() {
    this.errorMessage = '';
    lastValueFrom(this.http.post(`${environment.apiUrl}/api/tickets/removeCustomer`, {}))
      .then(response => {
        console.log('Customer removed successfully', response);
      })
      .catch(error => {
        console.error('Error removing customer', error);
        this.errorMessage = 'Failed to remove customer. Please try again.';
      });
  }
}
