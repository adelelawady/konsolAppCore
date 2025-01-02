import { Component, OnInit } from '@angular/core';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { PlaystationContainer } from 'app/core/konsolApi';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';

@Component({
  selector: 'jhi-last-sessions',
  templateUrl: './last-sessions.component.html',
  styleUrls: ['./last-sessions.component.scss'],
})
export class LastSessionsComponent implements OnInit {
  sessions: PsSessionDTO[] = [];
  isLoading = false;
  container: PlaystationContainer | null | undefined;

  constructor(
    private sheftResourceService: SheftResourceService,
    private router: Router,
    private route: ActivatedRoute,
    private translateService: TranslateService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadLastSessions();
  }

  loadLastSessions(): void {
    this.isLoading = true;

    this.sheftResourceService.getActiveSheftSessions().subscribe({
      next: (sessions: PsSessionDTO[]) => {
        this.sessions = sessions;
        this.isLoading = false;
      },
      error: error => {
        console.error('Error loading sessions:', error);
        this.isLoading = false;
        this.toastr.error(this.translateService.instant('playstation.lastSessions.error.loading'));
      },
    });
  }

  viewSession(session: PsSessionDTO): void {
    if (session.id) {
      this.router.navigate([session.id, 'view'], { relativeTo: this.route });
    }
  }

  trackById(index: number, session: PsSessionDTO): string | undefined {
    return session.id;
  }

  calculateDuration(startTime?: string, endTime?: string): string {
    if (!startTime || !endTime) {
      return '-';
    }
    const start = new Date(startTime);
    const end = new Date(endTime);
    const durationMs = end.getTime() - start.getTime();
    const hours = Math.floor(durationMs / (1000 * 60 * 60));
    const minutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
  }
}
