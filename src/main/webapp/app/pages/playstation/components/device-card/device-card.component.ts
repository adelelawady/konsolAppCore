import { Component, Input, ChangeDetectorRef, OnInit, OnDestroy, ElementRef } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { PsDeviceDTO } from 'app/core/konsolApi';
import { interval, Subscription } from 'rxjs';
import { startWith, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'jhi-device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DeviceCardComponent implements OnInit, OnDestroy {
  @Input() device!: PsDeviceDTO;
  @Input() isSelected = false;
  sessionDuration: string = '0h 0m';
  private durationSubscription?: Subscription;
  orderChangeAnimation = false;
  private subscription?: Subscription;

  constructor(private playstationService: PlaystationService, private cdr: ChangeDetectorRef, private elementRef: ElementRef) {}

  ngOnInit(): void {
    if (this.device.session) {
      this.durationSubscription = interval(500)
        .pipe(startWith(0))
        .subscribe(() => {
          this.updateSessionDuration();
        });
    }

    // Initialize subscription if not already set
    if (!this.subscription) {
      this.subscription = new Subscription();
    }

    // Add device selection subscription
    this.subscription.add(
      this.playstationService.selectedDevice$
        .pipe(distinctUntilChanged((prev, curr) => prev?.id === curr?.id))
        .subscribe(selectedDevice => {
          this.isSelected = selectedDevice?.id === this.device.id;
          this.cdr.detectChanges();
        })
    );

    // Add order change subscription
    this.subscription.add(
      this.playstationService.orderChange$.subscribe(() => {
        console.log('Order change event received in device card', this.device?.id);
        console.log('Selected device:', this.playstationService.getSelectedDevice()?.id);
        if (this.device?.id === this.playstationService.getSelectedDevice()?.id) {
          console.log('Triggering animation for device', this.device?.id);
          this.triggerOrderChangeAnimation();
          this.cdr.markForCheck(); // Use markForCheck instead of detectChanges
        }
      })
    );
  }

  ngOnDestroy(): void {
    if (this.durationSubscription) {
      this.durationSubscription.unsubscribe();
    }
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  onSelect(): void {
    if (this.isSelected) {
      //this.playstationService.selectDevice(null);
    } else {
      this.playstationService.selectDevice(this.device);
    }
  }

  onDoubleClick(event: MouseEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.playstationService.selectDevice(this.device);
    this.playstationService.showOrdersList();
  }

  getBorderColor(): string {
    if (this.orderChangeAnimation) {
      return 'border-animation';
    }
    if (this.device?.active) {
      return 'border-green';
    } else {
      return 'border-red';
    }
  }

  getStatusClass(): string {
    return `status-${this.device.active ? 'active' : 'inactive'}`;
  }

  private updateSessionDuration(): void {
    if (!this.device.session?.startTime) {
      this.sessionDuration = '0h 0m';
      return;
    }

    const startTime = new Date(this.device.session.startTime).getTime();
    const now = new Date().getTime();
    const duration = now - startTime;

    const hours = Math.floor(duration / (1000 * 60 * 60));
    const minutes = Math.floor((duration % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((duration % (1000 * 60)) / 1000);

   // this.sessionDuration = `${hours}h ${minutes}m ${seconds}s`;

    this.sessionDuration = `${hours}h ${minutes}m`;
  }

  isDeviceActive(): boolean {
    return !!this.device.session;
  }

  getSessionStartTime(): string {
    return this.device.session?.startTime ? 
      new Date(this.device.session.startTime).toLocaleTimeString() : '';
  }

  calculateSessionCost(): string {
    if (!this.device?.session) {
      return '0';
    }

    // Calculate session time cost
    let totalCost = 0;
    
    if (this.device.session.type?.price && this.device.session.startTime) {
      const startTime = new Date(this.device.session.startTime).getTime();
      const now = new Date().getTime();
      const durationInMs = now - startTime;
      const durationInHours = durationInMs / (1000 * 60 * 60);
      const hourlyRate = Number(this.device.session.type.price);
      const sessionCost = hourlyRate * durationInHours;
      totalCost += sessionCost;
    }

    // Add orders cost if there are any
    if (this.device.session.invoice?.netPrice) {
      totalCost += this.device.session?.invoice?.netPrice;
    }

    return totalCost.toFixed(0);
  }

  updateValue(newValue: any) {
    this.device = newValue;
    this.cdr.detectChanges();
  }

  private triggerOrderChangeAnimation(): void {
    this.orderChangeAnimation = true;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.orderChangeAnimation = false;
      this.cdr.detectChanges();
    }, 3000);
  }
}
