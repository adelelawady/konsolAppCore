import { Component, OnInit } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';

@Component({
  selector: 'jhi-device-details',
  templateUrl: './device-details.component.html',
  styleUrls: ['./device-details.component.scss']
})
export class DeviceDetailsComponent implements OnInit {
  selectedDevice: PsDeviceDTO | null = null;
  isStartingSession = false;

  constructor(
    private playstationService: PlaystationService,
    private playstationResourceService: PlaystationResourceService
  ) {}

  ngOnInit(): void {
    this.playstationService.selectedDevice$.subscribe(device => {
      this.selectedDevice = device;
    });
  }

  startSession(): void {
    if (this.selectedDevice?.id && !this.isStartingSession) {
      this.isStartingSession = true;
      this.playstationResourceService.startDeviceSession(this.selectedDevice.id)
        .subscribe({
          next: (updatedDevice) => {
            this.playstationService.selectDevice(updatedDevice);
            this.playstationService.reloadDevices();
          },
          error: (error) => {
            console.error('Error starting session:', error);
          },
          complete: () => {
            this.isStartingSession = false;
          }
        });
    }
  }

  getSessionDuration(): string {
    if (!this.selectedDevice?.session?.startTime) {
      return '0h 0m';
    }

    const startTime = new Date(this.selectedDevice.session.startTime).getTime();
    const now = new Date().getTime();
    const duration = now - startTime;

    const hours = Math.floor(duration / (1000 * 60 * 60));
    const minutes = Math.floor((duration % (1000 * 60 * 60)) / (1000 * 60));

    return `${hours}h ${minutes}m`;
  }

  isDeviceActive(): boolean {
    return this.selectedDevice?.session !== null && this.selectedDevice?.session !== undefined;
  }

  getDeviceStatus(): boolean {
    return this.selectedDevice?.active || false;
  }
}

