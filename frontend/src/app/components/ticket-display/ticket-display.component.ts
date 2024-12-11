import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import { NgIf } from '@angular/common';
import { environment } from '../../../environments/environment';
import { Chart, LinearScale, CategoryScale, LineElement, LineController, PointElement, Title, Tooltip, Legend } from 'chart.js';  // Import necessary modules

Chart.register(LinearScale, CategoryScale, LineElement, LineController, PointElement, Title, Tooltip, Legend);

/**
 * This component displays real-time ticket status using a chart visualization
 * and fetches the ticket status periodically from the backend server.
 */
@Component({
  selector: 'app-ticket-display',
  templateUrl: './ticket-display.component.html',
  standalone: true,
  imports: [
    NgIf
  ],
  styleUrls: ['./ticket-display.component.css']
})

export class TicketDisplayComponent implements OnInit, OnDestroy, AfterViewInit {
  protected ticketsRemaining: number = 0;
  protected soldOut: boolean = false;
  private intervalId: any;
  private readonly ticketHistory: number[] = Array(10).fill(0);

  private chart: any;

  /**
   * Constructor
   * @constructor - Initializes the TicketDisplayComponent instance.
   */
  public constructor() {}

  /**
   * Lifecycle hook: Called when the component is initialized.
   * Sets up a timer to fetch ticket status every 500 milliseconds.
   */
  public ngOnInit(): void {
    this.intervalId = setInterval(() => {
      this.fetchTicketStatus();
    }, 500);
  }

  /**
   * Lifecycle hook: Called when the component is destroyed.
   * Clears the interval to prevent memory leaks.
   */
  public ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  /**
   * Fetches ticket status from the backend server and updates the ticket history and chart.
   * Handles any errors during the fetch and updates the internal state for visualization.
   */
  public fetchTicketStatus(): void {
    fetch(`${environment.apiUrl}/api/tickets/status`)
      .then(response => response.text())
      .then(data => {
        const remaining = parseInt(data.split(':')[1].trim(), 10);
        if (!isNaN(remaining)) {
          this.ticketsRemaining = remaining;
          this.soldOut = remaining === 0;

          this.ticketHistory.push(remaining);
          if (this.ticketHistory.length > 10) {
            this.ticketHistory.shift();
          }

          this.updateChart();
        } else {
          console.error('Failed to parse remaining tickets:', data);
        }
      })
      .catch(error => {
        console.error('Error fetching ticket status:', error);
      });
  }

  /**
   * Updates the chart with the latest ticket history.
   * Redraws the visualization with the latest data.
   */
  public updateChart() {
    if (this.chart) {
      console.log('Updating chart with ticket history:', this.ticketHistory);
      this.chart.data.datasets[0].data = this.ticketHistory;
      this.chart.update('none');
    }
  }

  /**
   * Lifecycle hook: Called after the view has been initialized.
   * Sets up the initial chart visualization using Chart.js with default options.
   */
  public ngAfterViewInit() {
    this.chart = new Chart('ticketChart', {
      type: 'line',
      data: {
        labels: Array.from({ length: 10 }, (_, i) => i + 1),
        datasets: [{
          label: 'Tickets Remaining',
          data: this.ticketHistory,
          fill: false,
          borderColor: 'rgb(75, 192, 192)',
          tension: 0.1
        }]
      },
      options: {
        responsive: true,
        scales: {
          y: {
            type: 'linear',
            beginAtZero: true,
            title: {
              display: true,
              text: 'Tickets Remaining'
            },
            min: 0,
            max: 50,
            ticks: {
              stepSize: 5,
              precision: 0
            },
          },
          x: {
            title: {
              display: true,
              text: 'Time'
            }
          }
        }
      }
    });
  }
}
