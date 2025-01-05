import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PlaystationService } from '../../services/playstation.service';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { InvoiceUpdateDTO } from 'app/core/konsolApi/model/invoiceUpdateDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { finalize, debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { PlaystationEndSessionDTO } from 'app/core/konsolApi';

@Component({
  selector: 'jhi-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
})
export class CheckoutComponent implements OnInit {
  @Output() cancelCheckout = new EventEmitter<void>();
  printSessionReceipt = false;
  checkoutForm: FormGroup;
  selectedDevice: PsDeviceDTO | null = null;
  isProcessing = false;
  isUpdating = false;
  private lastUserNetPrice = 0;

  constructor(
    private fb: FormBuilder,
    private playstationService: PlaystationService,
    private playstationResourceService: PlaystationResourceService,
    private invoiceResourceService: InvoiceResourceService
  ) {
    this.checkoutForm = this.fb.group({
      discount: [0, [Validators.min(0)]],
      additions: [0, [Validators.min(0)]],
      notes: [''],
      userNetPrice: [0, [Validators.min(0)]],
    });
  }

  ngOnInit(): void {
    this.playstationService.selectedDevice$.subscribe(device => {
      this.selectedDevice = device;
      if (device?.session?.invoice) {
        let userNetPrice = 0;
        if (device.session.invoice.userNetPrice === 0) {
          userNetPrice = this.getFinalTotal();
        } else {
          userNetPrice = device.session.invoice.userNetPrice ?? 0;
        }

        this.lastUserNetPrice = userNetPrice;

        this.checkoutForm.patchValue({
          discount: device.session.invoice.discount ?? 0,
          additions: device.session.invoice.additions ?? 0,
          notes: device.session.invoice.additionsType ?? '',
          userNetPrice,
        });
      }
    });

    // Subscribe to discount and additions changes only
    this.checkoutForm
      .get('discount')
      ?.valueChanges.pipe(debounceTime(500), distinctUntilChanged())
      .subscribe(() => this.updateInvoice());

    this.checkoutForm
      .get('additions')
      ?.valueChanges.pipe(debounceTime(500), distinctUntilChanged())
      .subscribe(() => this.updateInvoice());

    // Handle notes changes
    this.checkoutForm
      .get('notes')
      ?.valueChanges.pipe(debounceTime(500), distinctUntilChanged())
      .subscribe(() => this.updateInvoice());

    // Handle userNetPrice changes separately
    this.checkoutForm
      .get('userNetPrice')
      ?.valueChanges.pipe(debounceTime(1000), distinctUntilChanged())
      .subscribe(value => {
        if (value !== this.lastUserNetPrice) {
          this.lastUserNetPrice = value;
          this.updateInvoice();
        }
      });
  }

  updateUserNetPriceValue(): void {
    const userNetPriceControl = this.checkoutForm.get('userNetPrice');
    if (userNetPriceControl) {
      userNetPriceControl.setValue(this.getFinalTotal());
    }
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
    return this.selectedDevice?.session?.invoice?.totalPrice ?? 0;
  }
  getNetPriceTotal(): number {
    return this.selectedDevice?.session?.invoice?.netPrice ?? 0;
  }

  getTotalBeforeDiscount(): number {
    const sessionCost = this.getSessionTimeCost();
    const ordersTotal = this.getOrdersTotal();
    const previousSessionsTotal = this.selectedDevice?.session?.deviceSessionsNetPrice ?? 0;

    if (!this.selectedDevice?.timeManagement) {
      return ordersTotal;
    }
    return sessionCost + ordersTotal + previousSessionsTotal;
  }

  getTotalAfterAdditionsAndDiscount(): number {
    let sessionCost = this.getSessionTimeCost();
    const ordersTotal = this.getNetPriceTotal();
    const previousSessionsTotal = this.selectedDevice?.session?.deviceSessionsNetPrice ?? 0;

    if (!this.selectedDevice?.timeManagement) {
      return ordersTotal;
    }
    if (
      ordersTotal === 0 &&
      this.selectedDevice.session?.invoice?.discount !== undefined &&
      this.selectedDevice.session.invoice.discount > 0
    ) {
      sessionCost = sessionCost - this.selectedDevice.session.invoice.discount;
    }
    return sessionCost + ordersTotal + previousSessionsTotal;
  }

  getFinalTotal(): number {
    const total = this.getTotalAfterAdditionsAndDiscount();
    // eslint-disable-next-line @typescript-eslint/restrict-plus-operands
    return total;
  }

  updateInvoice(): void {
    if (!this.selectedDevice || !this.selectedDevice.id || !this.selectedDevice.session?.invoice?.id || this.isUpdating) {
      return;
    }

    this.isUpdating = true;
    // const totalBeforeDiscount = this.getTotalBeforeDiscount();

    const invoiceUpdate: InvoiceUpdateDTO = {
      id: this.selectedDevice.session.invoice.id,
      discount: this.checkoutForm.get('discount')?.value || 0,
      additions: this.checkoutForm.get('additions')?.value || 0,
      additionsType: this.checkoutForm.get('notes')?.value,
      userNetPrice: this.checkoutForm.get('userNetPrice')?.value,
    };

    this.playstationResourceService
      .updateSessionInvoice(this.selectedDevice.id, invoiceUpdate)
      .pipe(
        finalize(() => {
          this.isUpdating = false;
        })
      )
      .subscribe({
        next: (device: PsDeviceDTO) => {
          this.selectedDevice = device;
          this.playstationService.selectDevice(device);
        },
        error(error: HttpErrorResponse) {
          console.error('Error updating invoice:', error);
        },
      });
  }

  reloadDevice(): void {
    if (!this.selectedDevice?.id) {
      return;
    }

    this.playstationResourceService.getDevice(this.selectedDevice.id).subscribe({
      next: device => {
        this.selectedDevice = device;
        this.playstationService.selectDevice(device);
      },
      error(error: HttpErrorResponse) {
        console.error('Error reloading device:', error);
      },
    });
  }
  endSessionWithFinalPrice(): void {
    // this.updateUserNetPriceValue();
    this.endSession(true);
  }

  endSession(matchFinalUserPrice: boolean = false): void {
    if (!this.selectedDevice?.id || this.isProcessing || this.isUpdating) {
      return;
    }

    this.isProcessing = true;

    const endSessionDTo: PlaystationEndSessionDTO = {
      matchFinalUserPrice: matchFinalUserPrice,
      printSessionRecipt: this.printSessionReceipt,
    };

    this.playstationResourceService.stopDeviceSession(this.selectedDevice.id, endSessionDTo).subscribe({
      next: () => {
        this.playstationService.reloadDevices();
        this.playstationService.selectDevice(null);
        this.cancel();
      },
      error(error: HttpErrorResponse) {
        console.error('Error ending session:', error);
      },
      complete: () => {
        this.isProcessing = false;
      },
    });
  }

  cancel(): void {
    this.cancelCheckout.emit();
  }

  getDuration(startTime: string | undefined, endTime: string | undefined): string {
    if (!startTime || !endTime) {
      return '0h 0m';
    }

    const start = new Date(startTime);
    const end = new Date(endTime);
    const durationMs = end.getTime() - start.getTime();
    const hours = Math.floor(durationMs / (1000 * 60 * 60));
    const minutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
  }

  calculateSessionCost(startTime: string | undefined, endTime: string | undefined, price: string | number | undefined): number {
    if (!startTime || !endTime || !price) {
      return 0;
    }

    const start = new Date(startTime).getTime();
    const end = new Date(endTime).getTime();
    const durationInMs = end - start;
    const durationInHours = durationInMs / (1000 * 60 * 60);
    const hourlyRate = Number(price);
    return Number((hourlyRate * durationInHours).toFixed(0));
  }

  getSessionTotalCost(prevSession: any): number {
    const timeCost = this.calculateSessionCost(prevSession.startTime, prevSession.endTime, prevSession.type?.price);
    const ordersCost = prevSession.invoice?.netPrice || 0;
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return, @typescript-eslint/restrict-plus-operands
    return timeCost + ordersCost;
  }
}
