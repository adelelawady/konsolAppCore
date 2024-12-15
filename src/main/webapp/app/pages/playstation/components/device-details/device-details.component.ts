import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { Subscription } from 'rxjs';
import { PsDeviceDTO } from 'app/core/konsolApi';

@Component({
  selector: 'jhi-device-details',
  templateUrl: './device-details.component.html',
  styleUrls: ['./device-details.component.scss']
})
export class DeviceDetailsComponent implements OnInit, OnDestroy {
  selectedDevice?: PsDeviceDTO;
  private subscription?: Subscription;
  
  constructor(private playstationService: PlaystationService) {}

  ngOnInit(): void {
    this.subscription = this.playstationService.selectedDevice$.subscribe(
      device => {
        this.selectedDevice = device || undefined;
      }
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  formatDuration(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    return `${hours}h ${remainingMinutes}m`;
  }

  formatCurrency(amount: number): string {
    return `EGP ${amount}`;
  }
}
