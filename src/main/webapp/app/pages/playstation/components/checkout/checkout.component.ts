import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PlaystationService } from '../../services/playstation.service';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { InvoiceUpdateDTO } from 'app/core/konsolApi/model/invoiceUpdateDTO';
import { HttpErrorResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { InvoiceResourceService } from 'app/core/konsolApi/api/invoiceResource.service';
import { debounceTime } from 'rxjs/operators';

@Component({
  selector: 'jhi-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {
  @Output() cancelCheckout = new EventEmitter<void>();
  
  checkoutForm: FormGroup;
  selectedDevice: PsDeviceDTO | null = null;
  isProcessing = false;
  isUpdating = false;

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
      userNetCost: [{ value: 0, disabled: true }, [Validators.min(0)]],
      userNetPrice: [0, [Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.playstationService.selectedDevice$.subscribe(device => {
      this.selectedDevice = device;
      if (device?.session?.invoice) {
        this.checkoutForm.patchValue({
          discount: device.session.invoice.discount || 0,
          additions: device.session.invoice.additions || 0,
          notes: device.session.invoice.additionsType || '',
          userNetCost: this.getTotalBeforeDiscount(),
          userNetPrice: device.session.invoice.userNetPrice || this.getFinalTotal()
        });
      }
    });

    // Subscribe to form changes with debounce
    this.checkoutForm.valueChanges
      .pipe(
        debounceTime(500)
      )
      .subscribe(() => {
        this.updateInvoice();
      });

    // Update userNetCost whenever relevant values change
    this.checkoutForm.get('userNetPrice')?.valueChanges.subscribe(value => {
      if (value !== this.getFinalTotal()) {
        this.updateInvoice();
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

  getTotalBeforeDiscount(): number {
    return this.getSessionTimeCost() + this.getOrdersTotal();
  }

  getFinalTotal(): number {
    const total = this.getTotalBeforeDiscount();
    const discount = this.checkoutForm.get('discount')?.value || 0;
    const additions = this.checkoutForm.get('additions')?.value || 0;
    return total - discount + additions;
  }

  updateInvoice(): void {
    if (!this.selectedDevice?.session?.invoice?.id || this.isUpdating) return;

    this.isUpdating = true;
    const totalBeforeDiscount = this.getTotalBeforeDiscount();
    
    // Update the userNetCost field
    this.checkoutForm.patchValue({
      userNetCost: totalBeforeDiscount
    }, { emitEvent: false });

    const invoiceUpdate: InvoiceUpdateDTO = {
      id: this.selectedDevice.session.invoice.id,
      discount: this.checkoutForm.get('discount')?.value || 0,
      additions: this.checkoutForm.get('additions')?.value || 0,
      additionsType: this.checkoutForm.get('notes')?.value,
      userNetCost: totalBeforeDiscount,
      userNetPrice: this.checkoutForm.get('userNetPrice')?.value || this.getFinalTotal()
    };

    this.invoiceResourceService.updateInvoice(this.selectedDevice?.session?.invoice?.id , invoiceUpdate)
      .pipe(
        finalize(() => {
          this.isUpdating = false;
        })
      )
      .subscribe({
        next: () => {
          this.reloadDevice();
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error updating invoice:', error);
        }
      });
  }

  private reloadDevice(): void {
    if (!this.selectedDevice?.id) return;

    this.playstationResourceService.getDevice(this.selectedDevice.id)
      .subscribe({
        next: (device) => {
            this.selectedDevice = device;
          this.playstationService.selectDevice(device);
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error reloading device:', error);
        }
      });
  }

  endSession(): void {
    if (!this.selectedDevice?.id || this.isProcessing || this.isUpdating) return;

    this.isProcessing = true;
    this.playstationResourceService.stopDeviceSession(this.selectedDevice.id)
      .subscribe({
        next: () => {
          this.playstationService.reloadDevices();
          this.playstationService.selectDevice(null);
          this.cancelCheckout.emit();
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error ending session:', error);
        },
        complete: () => {
          this.isProcessing = false;
        }
      });
  }

  cancel(): void {
    this.cancelCheckout.emit();
  }
} 