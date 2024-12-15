import { Component, OnInit } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';
import { PlaystationDeviceTypeService } from 'app/entities/playstation-device-type/service/playstation-device-type.service';
import { IPlaystationDeviceType } from 'app/entities/playstation-device-type/playstation-device-type.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { DeviceTypeDeleteDialogComponent } from './device-type-delete-dialog.component';

@Component({
  selector: 'jhi-device-type-control',
  templateUrl: './device-type-control.component.html',
  styleUrls: ['./device-type-control.component.scss']
})
export class DeviceTypeControlComponent implements OnInit {
  deviceTypes: PsDeviceType[] = [];
  devices: PsDeviceDTO[] = [];
  deviceTypeList?: IPlaystationDeviceType[];
  isLoading = false;

  constructor(
    private playstationResourceService: PlaystationResourceService,
    private deviceTypeService: PlaystationDeviceTypeService,
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
      this.deviceTypeService.query().subscribe(res => {
        this.deviceTypeList = res.body ?? [];
        resolve();
      });
    });
  }

  getDevicesByType(type: PsDeviceType): PsDeviceDTO[] {
    return this.devices.filter(device => device.type === type);
  }

  getDeviceTypeByType(type: PsDeviceType): IPlaystationDeviceType | undefined {
    return this.deviceTypeList?.find(dt => dt.name === type);
  }

  delete(deviceType: IPlaystationDeviceType): void {
    const modalRef = this.modalService.open(DeviceTypeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.deviceType = deviceType;
    modalRef.closed.subscribe(reason => {
      if (reason === ITEM_DELETED_EVENT) {
        this.loadAll();
      }
    });
  }
} 