import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { interval, Subscription } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Component({
  selector: 'jhi-device-list',
  templateUrl: './device-list.component.html',
  styleUrls: ['./device-list.component.scss']
})
export class DeviceListComponent implements OnInit, OnDestroy {
  devices: PsDeviceDTO[] = [];
  isLoading = false;
  private refreshSubscription?: Subscription;

  constructor(private playstationResourceService: PlaystationResourceService) {}

  ngOnInit(): void {
    // Refresh devices every 30 seconds
    this.refreshSubscription = interval(30000)
      .pipe(
        startWith(0), // Start immediately
        switchMap(() => this.loadDevices())
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
  }

  private loadDevices(): Observable<PsDeviceDTO[]> {
    this.isLoading = true;
    return this.playstationResourceService.getDevices().pipe(
      tap(devices => {
        this.devices = devices;
        this.isLoading = false;
      })
    );
  }
}
