import { Component, Input, OnInit } from '@angular/core';

export interface Device {
  id: number;
  name: string;
  roomName: string;
  deviceType: 'PS4' | 'PS5';
  status: 'available' | 'in-use' | 'maintenance';
  duration?: number;
  cost?: number;
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
export class DeviceCardComponent implements OnInit {
  @Input() device!: Device;

  constructor() {}

  ngOnInit(): void {}

  getStatusText(): string {
    switch (this.device.status) {
      case 'maintenance':
        return 'متوقف';
      case 'in-use':
        return 'قيد الاستخدام';
      default:
        return 'متاح';
    }
  }

  getBorderColor(): string {
    switch (this.device.status) {
      case 'maintenance':
        return 'border-red';
      case 'in-use':
        return 'border-blue';
      default:
        return 'border-green';
    }
  }

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
