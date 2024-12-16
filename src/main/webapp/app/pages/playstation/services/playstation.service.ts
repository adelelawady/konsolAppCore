import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { PsDeviceDTO } from 'app/core/konsolApi';

@Injectable({
  providedIn: 'root'
})
export class PlaystationService {
  private showOrdersSubject = new BehaviorSubject<boolean>(false);
  private selectedDeviceSubject = new BehaviorSubject<PsDeviceDTO | null>(null);
  private reloadDevicesSubject = new Subject<void>();
  private orderChangeSubject = new Subject<void>();

  showOrders$ = this.showOrdersSubject.asObservable();
  selectedDevice$ = this.selectedDeviceSubject.asObservable();
  reloadDevices$ = this.reloadDevicesSubject.asObservable();
  orderChange$ = this.orderChangeSubject.asObservable();

  showOrdersList(): void {
    this.showOrdersSubject.next(true);
  }

  hideOrders(): void {
    this.showOrdersSubject.next(false);
  }

  selectDevice(device: PsDeviceDTO | null): void {
    this.selectedDeviceSubject.next(device);
  }

  getSelectedDevice(): PsDeviceDTO | null {
    return this.selectedDeviceSubject.value;
  }

  reloadDevices(): void {
    this.reloadDevicesSubject.next();
  }

  notifyOrderChange(): void {
    console.log('PlaystationService: Notifying order change');
    this.orderChangeSubject.next();
  }
}
