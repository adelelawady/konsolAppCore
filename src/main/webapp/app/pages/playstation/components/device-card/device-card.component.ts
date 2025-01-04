import { Component, Input, ChangeDetectorRef, OnInit, OnDestroy, ElementRef, Output, EventEmitter } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { PlaystationContainer, PsDeviceDTO } from 'app/core/konsolApi';
import { interval, Subscription } from 'rxjs';
import { startWith, distinctUntilChanged } from 'rxjs/operators';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'app/shared/components/confirmation-modal/confirmation-modal.component';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-device-card',
  templateUrl: './device-card.component.html',
  styleUrls: ['./device-card.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class DeviceCardComponent implements OnInit, OnDestroy {
  @Input() device!: PsDeviceDTO;
  @Input() isSelected = false;
  sessionDuration = '0h 0m';
  container: PlaystationContainer | null | undefined;
  private durationSubscription?: Subscription;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  orderChangeAnimation = false;
  private subscription?: Subscription;
  // eslint-disable-next-line @typescript-eslint/member-ordering
  @Output() deviceMoved = new EventEmitter<void>();

  // eslint-disable-next-line @typescript-eslint/member-ordering
  availableDevices: any[] = [];

  constructor(
    private playstationService: PlaystationService,
    private cdr: ChangeDetectorRef,
    private elementRef: ElementRef,
    private playstationResourceService: PlaystationResourceService,
    private modalService: NgbModal,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      if (data['container']) {
        // eslint-disable-next-line no-console
        this.container = data['container'];
      }
    });

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
        if (this.device.id === this.playstationService.getSelectedDevice()?.id) {
          this.triggerOrderChangeAnimation();
          this.cdr.markForCheck(); // Use markForCheck instead of detectChanges
        }
      })
    );

    // this.loadAvailableDevices();
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
    if (this.device.active) {
      return 'border-green';
    } else {
      return 'border-red';
    }
  }

  getStatusClass(): string {
    return `status-${this.device.active ? 'active' : 'inactive'}`;
  }

  updateSessionDuration(): void {
    if (!this.device.timeManagement) {
      return;
    }

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
    return this.device.session?.startTime ? new Date(this.device.session.startTime).toLocaleTimeString() : '';
  }

  calculateSessionCost(): string {
    if (!this.device.session) {
      return '0';
    }

    // Calculate session time cost
    let totalCost = 0;

    if (this.device.session.type?.price && this.device.session.startTime && this.device.timeManagement) {
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
      totalCost += this.device.session.invoice.netPrice;
    }

    // Add deviceSessionsNetPrice if exists
    if (this.device.session.deviceSessionsNetPrice && this.device.timeManagement) {
      totalCost += this.device.session.deviceSessionsNetPrice;
    }

    return totalCost.toFixed(0);
  }

  // eslint-disable-next-line @typescript-eslint/explicit-function-return-type
  updateValue(newValue: any): void {
    this.device = newValue;
    this.cdr.detectChanges();
  }

  triggerOrderChangeAnimation(): void {
    this.orderChangeAnimation = true;
    this.cdr.detectChanges();

    setTimeout(() => {
      this.orderChangeAnimation = false;
      this.cdr.detectChanges();
    }, 3000);
  }

  loadAvailableDevices(): void {
    this.playstationResourceService.getDevicesByCategory({ name: this.container?.category }).subscribe(devices => {
      // Filter out the current device and active devices
      if (this.device.timeManagement) {
        this.availableDevices = devices.filter(d => d.id !== this.device.id && !d.active && d.timeManagement);
      } else {
        this.availableDevices = devices.filter(d => d.id !== this.device.id && !d.active && !d.timeManagement);
      }
    });
  }

  moveDevice(targetDeviceId: string): void {
    const confirmModal = this.modalService.open(ConfirmationModalComponent);
    confirmModal.componentInstance.message = `Are you sure you want to move this device?`;

    confirmModal.result.then(result => {
      if (result === 'confirm' && this.device.id) {
        this.playstationResourceService.moveDevice(this.device.id, targetDeviceId).subscribe({
          next: () => {
            this.playstationService.reloadDevices();
            this.deviceMoved.emit();
            // Show success message
          },
          error(error) {
            // Handle error
            console.error('Error moving device:', error);
          },
        });
      }
    });
  }

  getPreviousSessionsCount(): number {
    return this.device.session?.deviceSessions?.length || 0;
  }

  hasPreviousSessions(): boolean {
    return this.getPreviousSessionsCount() > 0;
  }
}
