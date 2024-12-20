import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PlaystationContainerStateService } from '../services/playstation-container.service';
import { of } from 'rxjs';

@Component({
  selector: 'jhi-devices-control',
  templateUrl: './devices-control.component.html',
  styleUrls: ['./devices-control.component.scss']
})
export class DevicesControlComponent implements OnInit {
  devices?: PsDeviceDTO[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page = 1;

  protected readonly playstationResourceService = inject(PlaystationResourceService);
  protected modalService = inject(NgbModal);
  private containerStateService: PlaystationContainerStateService = inject(PlaystationContainerStateService);
  ngOnInit(): void {
    this.loadDevices();
  }

  loadDevices(): void {
    this.isLoading = true;
    
    const container = this.containerStateService.getCurrentContainer();
    if (!container) {
     return;
    }
    this.playstationResourceService.getDevicesByCategory({ name: container.category }, 'response', true).subscribe({
      next: (res: HttpResponse<PsDeviceDTO[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers);
      },
      error: () => {
        this.isLoading = false;
        this.onError();
      }
    });
  }

  delete(device: PsDeviceDTO): void {
    if (device.id && confirm('Are you sure you want to delete this device?')) {
      this.playstationResourceService.deleteDevice(device.id).subscribe(() => {
        this.loadDevices();
      });
    }
  }

  protected onSuccess(data: PsDeviceDTO[] | null, headers: any): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.devices = data || [];
  }

  protected onError(): void {
    // Handle error
  }

  trackId(index: number, device: PsDeviceDTO): string {
    return device.id || '';
  }
} 