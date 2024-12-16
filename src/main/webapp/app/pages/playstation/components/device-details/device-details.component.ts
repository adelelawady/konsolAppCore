import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { Subscription } from 'rxjs';
import { InvoiceItemDTO } from 'app/core/konsolApi/model/invoiceItemDTO';
import { InvoiceDTO } from 'app/core/konsolApi/model/invoiceDTO';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { CreateInvoiceItemDTO } from 'app/core/konsolApi/model/createInvoiceItemDTO';
import { InvoiceItemUpdateDTO } from 'app/core/konsolApi';

@Component({
  selector: 'jhi-device-details',
  templateUrl: './device-details.component.html',
  styleUrls: ['./device-details.component.scss']
})
export class DeviceDetailsComponent implements OnInit, OnDestroy {
  selectedDevice: PsDeviceDTO | null = null;
  isStartingSession = false;
  private subscription?: Subscription;
  orderItems: InvoiceItemDTO[] = [];

  constructor(
    private playstationService: PlaystationService,
    private playstationResourceService: PlaystationResourceService
  ) {}

  ngOnInit(): void {
    this.subscription = this.playstationService.selectedDevice$.subscribe(device => {
      this.selectedDevice = device;
      this.updateOrderItems(device?.session);
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private updateOrderItems(session?: PsSessionDTO): void {
    if (session?.invoice?.invoiceItems) {
      this.orderItems = Array.from(session.invoice.invoiceItems);
    } else {
      this.orderItems = [];
    }
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

  getTotalPrice(): number {
    return this.selectedDevice?.session?.invoice?.totalPrice || 0;
  }

  getNetPrice(): number {
    return this.selectedDevice?.session?.invoice?.netPrice || 0;
  }

  getDiscount(): number {
    return this.selectedDevice?.session?.invoice?.discount || 0;
  }

  updateDeviceOrder(item: InvoiceItemDTO, increment: boolean): void {
    if (!this.selectedDevice?.id || !item || !item.id) return;


    const newQty = increment ? (item.userQty || 0) + 1 : Math.max((item.userQty || 0) - 1, 0);
    const updateItemDTO: InvoiceItemUpdateDTO = {
      id: item.id,
      qty: newQty
    };

    if (newQty === 0) {
      this.deleteDeviceOrder(item);
      return;
    }


    this.playstationResourceService.updateDeviceOrder(this.selectedDevice?.id, item?.id, updateItemDTO)
      .subscribe({
        next: (updatedDevice) => {
          this.playstationService.selectDevice(updatedDevice);
          this.playstationService.reloadDevices();
          this.playstationService.notifyOrderChange();
        },
        error: (error) => {
          console.error('Error updating order:', error);
        }
      });
  }


  deleteDeviceOrder(item: InvoiceItemDTO): void {
    if (!this.selectedDevice?.id || !item || !item.id) return;


    this.playstationResourceService.deleteDeviceOrder(this.selectedDevice?.id, item?.id)
      .subscribe({
        next: () => {
          if (this.selectedDevice?.id && item?.id) {
          this.playstationResourceService.getDevice(this.selectedDevice?.id)
            .subscribe({
              next: (updatedDevice) => {
                this.selectedDevice = updatedDevice;
                this.playstationService.selectDevice(updatedDevice);
                this.playstationService.reloadDevices();
                this.playstationService.notifyOrderChange();
              },
              error: (error) => {
                console.error('Error getting device:', error);
              }
            });
          }
        },
        error: (error) => {
          console.error('Error deleting order:', error);
        }
      });
  }


  getSessionTimeCost(): number {
    if (!this.selectedDevice?.session?.type?.price || !this.selectedDevice.session?.startTime) {
      return 0;
    }

    const startTime = new Date(this.selectedDevice.session.startTime).getTime();
    const now = new Date().getTime();
    const durationInMs = now - startTime;
    const durationInHours = durationInMs / (1000 * 60 * 60);
    const hourlyRate = Number(this.selectedDevice.session.type.price);
    return Number((hourlyRate * durationInHours).toFixed(0));
  }

  getOrdersTotal(): number {
    return this.selectedDevice?.session?.invoice?.netPrice || 0;
  }

  getTotalCost(): number {
    return this.getSessionTimeCost() + this.getOrdersTotal();
  }
}
