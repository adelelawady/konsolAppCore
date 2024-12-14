import { Component, Input } from '@angular/core';

export interface Device {
  id: number;
  name: string;
  status: 'available' | 'in-use' | 'maintenance';
  hourlyRate: number;
  currentSession?: {
    startTime: string;
    duration: number;
    cost: number;
  };
  imageUrl: string;
}

@Component({
  selector: 'jhi-device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss']
})
export class DeviceCardComponent {
  @Input() device!: Device;

  getStatusClass(): string {
    return `status-${this.device.status}`;
  }

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('tr-TR', { style: 'currency', currency: 'TRY' }).format(amount);
  }

  formatDuration(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    return `${hours}h ${remainingMinutes}m`;
  }
}
