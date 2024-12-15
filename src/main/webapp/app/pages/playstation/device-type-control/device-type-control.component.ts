import { Component, OnInit } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { DeviceTypeDeleteDialogComponent } from './device-type-delete-dialog.component';

@Component({
  selector: 'jhi-device-type-control',
  templateUrl: './device-type-control.component.html',
  styleUrls: ['./device-type-control.component.scss']
})
export class DeviceTypeControlComponent implements OnInit {
  deviceTypes: string[] = [];
  devices: PsDeviceDTO[] = [];
  deviceTypeList?: PsDeviceType[];
  isLoading = false;

  constructor(
    private playstationResourceService: PlaystationResourceService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;
    Promise.all([
      this.loadDevices(),
      this.loadDeviceTypes()
    ]).finally(() => {
      this.isLoading = false;
    });
  }

  loadDevices(): Promise<void> {
    return new Promise((resolve) => {
      this.playstationResourceService.getDevices().subscribe(devices => {
        this.devices = devices;
        resolve();
      });
    });
  }

  loadDeviceTypes(): Promise<void> {
    return new Promise((resolve) => {
      this.playstationResourceService.getDevicesTypes().subscribe(types => {
        this.deviceTypeList = types;
        this.deviceTypes = types.map(type => type.name).filter((name): name is string => name !== undefined);
        resolve();
      });
    });
  }

  getDevicesByType(type: string): PsDeviceDTO[] {
    return this.devices.filter(device => device.type === type);
  }

  getDeviceTypeByType(type: string): PsDeviceType | undefined {
    return this.deviceTypeList?.find(dt => dt.name === type);
  }

  delete(deviceType: PsDeviceType): void {
    if (deviceType.id) {
      const modalRef = this.modalService.open(DeviceTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
      modalRef.componentInstance.deviceType = deviceType;
      modalRef.closed.subscribe(reason => {
        if (reason === ITEM_DELETED_EVENT) {
          this.loadAll();
        }
      });
    }
  }
} 