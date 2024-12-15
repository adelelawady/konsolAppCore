import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { PsDeviceDTO } from 'app/core/konsolApi';
import { interval, Subscription } from 'rxjs';
import { startWith } from 'rxjs/operators';

@Component({
  selector: 'jhi-device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss']
})
export class DeviceCardComponent implements OnInit, OnDestroy {
  @Input() device!: PsDeviceDTO;
  sessionDuration: string = '0h 0m';
  private durationSubscription?: Subscription;

  constructor(private playstationService: PlaystationService) {}

  ngOnInit(): void {
    if (this.device.session) {
      // Update duration every second
      this.durationSubscription = interval(1000)
        .pipe(startWith(0))
        .subscribe(() => {
          this.updateSessionDuration();
        });
    }
  }

  ngOnDestroy(): void {
    if (this.durationSubscription) {
      this.durationSubscription.unsubscribe();
    }
  }

  onSelect(): void {
    this.playstationService.selectDevice(this.device);
  }

  onDoubleClick(event: MouseEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.playstationService.selectDevice(this.device);
    this.playstationService.showOrdersList();
  }

  getBorderColor(): string {
    if (this.device?.active) {
      return 'border-green';
    } else {
      return 'border-red';
    }
  }

  getStatusClass(): string {
    return `status-${this.device.active ? 'active' : 'inactive'}`;
  }

  private updateSessionDuration(): void {
    if (!this.device.session?.startTime) {
      this.sessionDuration = '0h 0m';
      return;
    }

    const startTime = new Date(this.device.session.startTime).getTime();
    const now = new Date().getTime();
    const duration = now - startTime;

    const hours = Math.floor(duration / (1000 * 60 * 60));
    const minutes = Math.floor((duration % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((duration % (1000 * 60)) / 1000);

   // this.sessionDuration = `${hours}h ${minutes}m ${seconds}s`;

    this.sessionDuration = `${hours}h ${minutes}m`;
  }

  isDeviceActive(): boolean {
    return !!this.device.session;
  }

  getSessionStartTime(): string {
    return this.device.session?.startTime ? 
      new Date(this.device.session.startTime).toLocaleTimeString() : '';
  }

  calculateSessionCost(): string {
    if (!this.device?.session || !this.device?.session?.type?.price || !this.device.session?.startTime) {
      return '0';
    }

    const startTime = new Date(this.device.session.startTime).getTime();
    const now = new Date().getTime();
    const durationInMs = now - startTime;

    // Convert duration to hours (as a decimal)
    const durationInHours = durationInMs / (1000 * 60 * 60);
    
    // Calculate cost (hourly rate * duration in hours)
    const hourlyRate = Number(this.device.session.type.price);
    const cost = hourlyRate * durationInHours;
    
    // Return formatted number with 2 decimal places
    return cost.toFixed(0);
  }
}
