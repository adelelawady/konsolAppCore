import { Component, OnInit } from '@angular/core';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { PlaystationContainer } from 'app/core/konsolApi';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-last-sessions',
  templateUrl: './last-sessions.component.html',
  styleUrls: ['./last-sessions.component.scss'],
})
export class LastSessionsComponent implements OnInit {
  sessions: PsSessionDTO[] = [];
  isLoading = false;
  groupedSessions: Map<string, PsSessionDTO[]> = new Map();
  container: PlaystationContainer | null | undefined;
  constructor(
    private sheftResourceService: SheftResourceService,
    private router: Router,
    private route: ActivatedRoute,
    private translateService: TranslateService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    // First try to get container from resolver data
    this.route.data.subscribe(data => {
      if (data['container']) {
        // eslint-disable-next-line no-console
        this.container = data['container'];
        // eslint-disable-next-line no-console
        this.loadLastSessions();
      }
    });
  }

  loadLastSessions(): void {
    this.isLoading = true;
    this.sheftResourceService.getActiveSheftSessions().subscribe(
      (sessions: PsSessionDTO[]) => {
        this.isLoading = false;
        this.sessions = sessions;
        this.groupSessions();
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  private groupSessions(): void {
    this.groupedSessions = this.sessions.reduce((groups, session) => {
      const containerId = session.containerId || 'unassigned';
      if (!groups.has(containerId)) {
        groups.set(containerId, []);
      }
      groups.get(containerId)?.push(session);
      return groups;
    }, new Map<string, PsSessionDTO[]>());

    // Sort sessions within each container by start time
    this.groupedSessions.forEach(sessions => {
      sessions.sort((a, b) => {
        return new Date(b.startTime ?? 0).getTime() - new Date(a.startTime ?? 0).getTime();
      });
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
