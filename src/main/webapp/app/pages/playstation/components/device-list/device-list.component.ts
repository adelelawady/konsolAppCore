import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { interval, Subscription, Subject } from 'rxjs';
import { startWith, switchMap, takeUntil } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PlaystationService } from '../../services/playstation.service';

@Component({
  selector: 'jhi-device-list',
  templateUrl: './device-list.component.html',
  styleUrls: ['./device-list.component.scss']
})
export class DeviceListComponent implements OnInit, OnDestroy {
  devices: PsDeviceDTO[] = [];
  isLoading = false;
  firstLoad = false;
  private refreshSubscription?: Subscription;
  private destroy$ = new Subject<void>();

  constructor(private playstationResourceService: PlaystationResourceService,
    private playstationService: PlaystationService) {}

  ngOnInit(): void {
    this.playstationService.reloadDevices$.subscribe(() => {
      this.loadDevices(true).subscribe();
    });

    // Refresh devices every 30 seconds without showing loading
    this.refreshSubscription = interval(30000)
      .pipe(
        startWith(0),
        switchMap(() => this.loadDevices(false))
      )
      .subscribe();
  }

  ngOnDestroy(): void {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
    this.destroy$.next();
    this.destroy$.complete();
  }

  private loadDevices(showLoading: boolean = true): Observable<PsDeviceDTO[]> {
    if (showLoading) {
      this.isLoading = true;
    }
    return this.playstationResourceService.getDevices().pipe(
      tap(devices => {
        this.devices = devices;
        if (!this.firstLoad){
          this.firstLoad = true;
        }
        if (showLoading) {
          this.isLoading = false;
        }
      })
    );
  }
}
