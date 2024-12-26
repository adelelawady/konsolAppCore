import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { interval, Subscription, Subject, of } from 'rxjs';
import { startWith, switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { PlaystationService } from '../../services/playstation.service';
import { ActivatedRoute } from '@angular/router';
import { PlaystationContainer } from 'app/core/konsolApi';

@Component({
  selector: 'jhi-device-list',
  templateUrl: './device-list.component.html',
  styleUrls: ['./device-list.component.scss'],
})
export class DeviceListComponent implements OnInit, OnDestroy {
  container: PlaystationContainer | null | undefined;

  devices: PsDeviceDTO[] = [];
  isLoading = false;
  firstLoad = false;
  private refreshSubscription?: Subscription;
  private destroy$ = new Subject<void>();

  constructor(
    private playstationResourceService: PlaystationResourceService,
    private playstationService: PlaystationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // First try to get container from resolver data
    this.route.data.subscribe(data => {
      if (data['container']) {
        // eslint-disable-next-line no-console
        this.container = data['container'];
        // eslint-disable-next-line no-console
        console.log('container from resolver',this.container);
        this.loadDevices(true).subscribe();
      }
    });
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

  private loadDevices(showLoading = true): Observable<PsDeviceDTO[]> {
    if (showLoading) {
      this.isLoading = true;
    }

    if (!this.container) {
      this.isLoading = false;
      return of([]);
    }

    return this.playstationResourceService.getDevicesByCategory({ name: this.container.category }).pipe(
      tap(devices => {
        this.devices = devices;
        if (!this.firstLoad) {
          this.firstLoad = true;
        }
        if (showLoading) {
          this.isLoading = false;
        }
      })
    );
  }
}
