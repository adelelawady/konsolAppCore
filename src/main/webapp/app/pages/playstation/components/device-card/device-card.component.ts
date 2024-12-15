import { Component, Input, OnInit } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { PsDeviceDTO } from 'app/core/konsolApi';



@Component({
  selector: 'jhi-device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss']
})
export class DeviceCardComponent implements OnInit {
  @Input() device!: PsDeviceDTO;

  constructor(private playstationService: PlaystationService) {}

  ngOnInit(): void {}

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

  formatCurrency(amount: number): string {
    return new Intl.NumberFormat('tr-TR', { style: 'currency', currency: 'TRY' }).format(amount);
  }

  formatDuration(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    return `${hours}h ${remainingMinutes}m`;
  }
}
