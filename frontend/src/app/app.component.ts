import { Component } from '@angular/core';
import { ConfigurationFormComponent } from './components/configuration-form/configuration-form.component';
import { TicketDisplayComponent } from './components/ticket-display/ticket-display.component';
import {LogDisplayComponent} from './components/log-display/log-display.component';
import {HttpClientModule} from '@angular/common/http';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: true,
  imports: [
    HttpClientModule,
    ConfigurationFormComponent,
    TicketDisplayComponent,
    LogDisplayComponent
  ],
  styleUrls: ['./app.component.css']
})
export class AppComponent {}

