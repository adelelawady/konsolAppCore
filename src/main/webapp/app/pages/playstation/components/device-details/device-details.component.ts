import { Component, OnInit } from '@angular/core';
import { Device } from '../device-card/device-card.component';

@Component({
  selector: 'jhi-device-details',
  templateUrl: './device-details.component.html',
  styleUrls: ['./device-details.component.scss']
})
export class DeviceDetailsComponent implements OnInit {
  selectedDevice?: Device;
  
  constructor() {}

  ngOnInit(): void {}

  onDeviceSelect(device: Device): void {
    this.selectedDevice = device;
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
