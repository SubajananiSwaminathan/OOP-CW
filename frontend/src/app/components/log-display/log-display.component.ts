import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {lastValueFrom} from 'rxjs';

/**
 * This component is responsible for displaying real-time logs fetched from the backend.
 * It polls the server at a regular interval to fetch and display log data.
 */
@Component({
  selector: 'app-log-display',
  standalone: true,
  imports: [
    NgIf,
    NgForOf
  ],
  templateUrl: './log-display.component.html',
  styleUrl: './log-display.component.css'
})

export class LogDisplayComponent implements OnInit {
  protected logs: string[] = [];
  private pollingInterval = 500;

  /**
   * Creates an instance of LogDisplayComponent.
   * @param http - The HttpClient instance used to perform HTTP requests.
   */
  public constructor(private http: HttpClient) {}

  /**
   * Lifecycle hook that runs after component initialization.
   * Sets up the initial log fetch and starts polling the server at a regular interval.
   */
  ngOnInit(): void {
    this.fetchLogs();
    setInterval(() => this.fetchLogs(), this.pollingInterval);
  }

  /**
   * Fetches log data from the backend server via HTTP GET.
   * Logs are split by newline (`\n`) into an array for rendering purposes.
   * Handles server errors gracefully by logging them to the console.
   */
  fetchLogs(): void {
    this.http
      .get(`${environment.apiUrl}/api/tickets/logs`, { responseType: 'text' })
      .subscribe(
        (data: string) => {
          this.logs = data.split('\n');
        },
        (error) => {
          console.error('Error fetching logs:', error);
        }
      );
  }
}
