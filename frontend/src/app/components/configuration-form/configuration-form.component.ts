import { Component, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { lastValueFrom } from 'rxjs';
import {NgIf} from '@angular/common';
import {ControlPanelComponent} from '../control-panel/control-panel.component';

/**
 * A standalone Angular component for configuring ticket-related settings.
 * Handles user input for configuration settings and sends them to the backend server
 * for persistence. Emits data using EventEmitter and communicates with the server
 * via HTTP POST upon form submission.
 */
@Component({
  selector: 'app-configuration-form',
  templateUrl: './configuration-form.component.html',
  standalone: true,
  imports: [
    FormsModule,
    NgIf,
    ControlPanelComponent
  ],
  styleUrls: ['./configuration-form.component.css']
})

export class ConfigurationFormComponent {
  protected totalTickets: number = 0;
  protected ticketReleaseRate: number = 0;
  protected customerRetrievalRate: number = 0;
  protected maxTicketCapacity: number = 0;

  /** EventEmitter for notifying parent components upon form submission */
  @Output() protected formSubmit: EventEmitter<any> = new EventEmitter();

  /**
   * Constructor to inject the HttpClient for making HTTP requests.
   * @param http - HttpClient instance for server communication
   */
  public constructor(private http: HttpClient) {}

  /**
   * Handles form submission logic.
   * Performs validation of user input and sends configuration settings
   * to the server using an HTTP POST request. Emits the configuration data
   * using an EventEmitter to notify parent components of changes.
   */
  public async onSubmit(): Promise<void> {
    if (
      this.totalTickets <= 0 ||
      this.ticketReleaseRate <= 0 ||
      this.customerRetrievalRate <= 0 ||
      this.maxTicketCapacity <= 0
    ) {
      alert('All fields should have positive values.');
    } else {
      const params = {
        totalTickets: this.totalTickets,
        ticketReleaseRate: this.ticketReleaseRate,
        customerRetrievalRate: this.customerRetrievalRate,
        maxTicketCapacity: this.maxTicketCapacity
      };

      this.formSubmit.emit(params);

      try {
        const response = await lastValueFrom(
          this.http.post(`${environment.apiUrl}/api/tickets/configure`, null, { params })
        );
        console.log('Configuration successfully sent to backend!', response);
        alert('Configuration applied successfully!');
      } catch (error) {
        console.error('Error sending configuration to backend:', error);
        alert('An error occurred while configuring the ticket pool. Please try again.');
      }
    }
  }
}
