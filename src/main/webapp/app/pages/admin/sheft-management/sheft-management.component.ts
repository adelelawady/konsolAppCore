import { Component, OnInit, OnDestroy } from '@angular/core';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';
import { ActiveSheftService } from '../sheft-component/active-sheft.service';
import { SheftDTO } from 'app/core/konsolApi/model/sheftDTO';
import { interval, Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-sheft-management',
  templateUrl: './sheft-management.component.html',
  styleUrls: ['./sheft-management.component.scss'],
})
export class SheftManagementComponent implements OnInit, OnDestroy {
  activeSheft: SheftDTO | null = null;
  loading = false;
  error = false;
  private refreshInterval?: Subscription;

  constructor(private sheftResource: SheftResourceService, private activeSheftService: ActiveSheftService, private router: Router) {}

  ngOnInit(): void {
    this.loadActiveSheft();
    // Refresh active shift data every 30 seconds
    this.refreshInterval = interval(30000).subscribe(() => {
      this.loadActiveSheft(true);
    });
  }

  ngOnDestroy(): void {
    if (this.refreshInterval) {
      this.refreshInterval.unsubscribe();
    }
  }

  loadActiveSheft(silent = false): void {
    if (!silent) {
      this.loading = true;
    }
    this.activeSheftService.getActiveSheft(true).subscribe({
      next: sheft => {
        this.activeSheft = sheft;
        this.loading = false;
        this.error = false;
      },
      error: error => {
        console.error('Error loading active sheft:', error);
        this.error = true;
        this.loading = false;
      },
    });
  }

  stopActiveSheft(): void {
    if (!confirm('Are you sure you want to stop the active shift?')) {
      return;
    }

    this.loading = true;
    this.sheftResource.stopActiveSheft().subscribe({
      next: stoppedSheft => {
        this.activeSheftService.clearCache();
        this.router.navigate(['/shefts', stoppedSheft.id]);
      },
      error: error => {
        console.error('Error stopping sheft:', error);
        this.error = true;
        this.loading = false;
      },
    });
  }

  viewSheftDetails(): void {
    if (this.activeSheft?.id) {
      this.router.navigate(['/shefts', this.activeSheft.id]);
    }
  }

  getDuration(startTime: string | undefined): string {
    if (!startTime) return '00:00:00';

    const start = new Date(startTime).getTime();
    const now = new Date().getTime();
    const diff = Math.floor((now - start) / 1000); // difference in seconds

    const hours = Math.floor(diff / 3600);
    const minutes = Math.floor((diff % 3600) / 60);
    const seconds = diff % 60;

    return `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
  }
}
