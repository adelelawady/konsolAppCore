import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';

@Injectable({
  providedIn: 'root'
})
export class PlaystationService {
  private selectedDeviceSubject = new BehaviorSubject<PsDeviceDTO | null>(null);
  private reloadDevicesSubject = new BehaviorSubject<void>(undefined);
  private showOrdersSubject = new BehaviorSubject<boolean>(false);

  selectedDevice$ = this.selectedDeviceSubject.asObservable();
  reloadDevices$ = this.reloadDevicesSubject.asObservable();
  showOrders$ = this.showOrdersSubject.asObservable();

  selectDevice(device: PsDeviceDTO | null): void {
    this.selectedDeviceSubject.next(device);
  }

  reloadDevices(): void {
    this.reloadDevicesSubject.next();
  }

  showOrdersList(): void {
    this.showOrdersSubject.next(true);
  }

  hideOrders(): void {
    this.showOrdersSubject.next(false);
  }
}
