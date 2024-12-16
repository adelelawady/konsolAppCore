import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PlaystationService } from '../../services/playstation.service';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { InvoiceUpdateDTO } from 'app/core/konsolApi/model/invoiceUpdateDTO';
import { HttpErrorResponse } from '@angular/common/http';

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

  constructor(
    private fb: FormBuilder,
    private playstationService: PlaystationService,
    private playstationResourceService: PlaystationResourceService
  ) {
    this.checkoutForm = this.fb.group({
      discount: [0, [Validators.min(0)]],
      additions: [0, [Validators.min(0)]],
      notes: ['']
    });
  }

  ngOnInit(): void {
    this.playstationService.selectedDevice$.subscribe(device => {
      this.selectedDevice = device;
      if (device?.session?.invoice) {
        this.checkoutForm.patchValue({
          discount: device.session.invoice.discount || 0,
          additions: device.session.invoice.additions || 0,
          notes: device.session.invoice.additionsType || ''
        });
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
    if (!this.selectedDevice?.session?.invoice?.id) return;

    const invoiceUpdate: InvoiceUpdateDTO = {
      id: this.selectedDevice.session.invoice.id,
      discount: this.checkoutForm.get('discount')?.value || 0,
      additions: this.checkoutForm.get('additions')?.value || 0,
      additionsType : this.checkoutForm.get('notes')?.value,
     // netPrice: this.getOrdersTotal(),
      //totalPrice: this.getFinalTotal()
    };

    /*
    this.playstationResourceService.saveInvoice(invoiceUpdate.id)
      .subscribe({
        next: () => {
          this.playstationService.reloadDevices();
        },
        error: (error: HttpErrorResponse) => {
          console.error('Error updating invoice:', error);
        }
      });
      */
  }

  endSession(): void {
    if (!this.selectedDevice?.id || this.isProcessing) return;

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