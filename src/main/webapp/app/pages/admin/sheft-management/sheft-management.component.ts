import { Component, OnInit, OnDestroy } from '@angular/core';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';
import { ActiveSheftService } from '../sheft-component/active-sheft.service';
import { SheftDTO } from 'app/core/konsolApi/model/sheftDTO';
import { interval, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

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
  public showStopSheftModal = false;
  public printSheftOnStop = true;

  constructor(
    private sheftResource: SheftResourceService,
    private activeSheftService: ActiveSheftService,
    private router: Router,
    private toastr: ToastrService
  ) {}

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
        if (sheft == null) {
          this.activeSheft = null;
          this.loading = false;
          this.error = false;
          return;
        }
        if (sheft?.id) {
          this.sheftResource.getSheft(sheft.id).subscribe({
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
      },
      error: error => {
        console.error('Error loading active sheft:', error);
        this.error = true;
        this.loading = false;
      },
    });
  }

  stopActiveSheft(): void {
    this.loading = true;
    this.sheftResource.stopActiveSheft(this.printSheftOnStop).subscribe({
      next: stoppedSheft => {
        this.activeSheftService.clearCache();
        this.closeStopSheftModal();
        this.router.navigate(['/shefts', stoppedSheft.id]);
      },
      error: error => {
        console.error('Error stopping sheft:', error);
        this.error = true;
        this.loading = false;
        this.closeStopSheftModal();
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

  updateSheft(): void {
    if (!this.activeSheft || !this.activeSheft.id) {
      return;
    }

    this.sheftResource.partialUpdateSheft(this.activeSheft.id, this.activeSheft).subscribe({
      next: updatedSheft => {
        this.activeSheft = updatedSheft;
        this.toastr.success('Shift updated successfully');
      },
      error: error => {
        this.loadActiveSheft();
        this.toastr.error('Failed to update shift');
        console.error('Error updating shift:', error);
      },
    });
  }

  handleInputChange(field: string, event: Event): void {
    if (!this.activeSheft) return;

    const input = event.target as HTMLInputElement;
    const value = input.value;

    // Update the local activeSheft object
    if (field === 'additions' || field === 'sheftExpenses') {
      (this.activeSheft as any)[field] = parseFloat(value) || 0;
    } else {
      (this.activeSheft as any)[field] = value;
    }

    this.updateSheft();
  }

  handleTextAreaChange(field: string, event: Event): void {
    if (!this.activeSheft) return;

    const textarea = event.target as HTMLTextAreaElement;
    // Update the local activeSheft object
    (this.activeSheft as any)[field] = textarea.value;

    this.updateSheft();
  }

  public startSheft(): void {
    this.sheftResource.startSheft().subscribe({
      next: response => {
        // Reload the active shift after creation
        this.loadActiveSheft();
      },
      error: error => {
        console.error('Error starting shift:', error);
        // Handle error - you might want to show a notification to the user
      },
    });
  }

  public confirmStopSheft(): void {
    this.showStopSheftModal = true;
  }

  public closeStopSheftModal(): void {
    this.showStopSheftModal = false;
  }
}
