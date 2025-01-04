import { Component, OnInit } from '@angular/core';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { PlaystationContainer } from 'app/core/konsolApi';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { HttpResponse } from '@angular/common/http';
import { forkJoin } from 'rxjs';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';

export interface GroupedSession {
  container: PlaystationContainer | null;
  sessions: PsSessionDTO[];
}

@Component({
  selector: 'jhi-last-sessions',
  templateUrl: './last-sessions.component.html',
  styleUrls: ['./last-sessions.component.scss'],
})
export class LastSessionsComponent implements OnInit {
  sessions: PsSessionDTO[] = [];
  containers: PlaystationContainer[] = [];
  isLoading = false;
  groupedSessions: Map<string, GroupedSession> = new Map();
  container: PlaystationContainer | null | undefined;

  constructor(
    private sheftResourceService: SheftResourceService,
    private containerService: PlaystationContainerResourceService,
    private playstationResourceService: PlaystationResourceService,
    private router: Router,
    private route: ActivatedRoute,
    private translateService: TranslateService,
    private toastr: ToastrService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.isLoading = true;

    // Load both containers and sessions in parallel
    forkJoin({
      containers: this.containerService.getPlaystationContainers(),
      sessions: this.sheftResourceService.getActiveSheftSessions(),
    }).subscribe({
      next: result => {
        this.containers = result.containers;
        this.sessions = result.sessions;
        this.groupSessions();
        this.isLoading = false;
      },
      error: error => {
        console.error('Error loading data:', error);
        this.toastr.error(this.translateService.instant('playstation.lastSessions.error.loading'));
        this.isLoading = false;
      },
    });
  }

  loadLastSessions(): void {
    this.loadData(); // Reuse loadData to refresh both containers and sessions
  }

  private groupSessions(): void {
    this.groupedSessions = this.sessions.reduce((groups, session) => {
      const containerId = session.containerId || 'unassigned';
      if (!groups.has(containerId)) {
        // Find container from the loaded containers
        const container = this.containers.find(c => c.id === containerId) || (this.container?.id === containerId ? this.container : null);
        groups.set(containerId, {
          container,
          sessions: [],
        });
      }
      groups.get(containerId)?.sessions.push(session);
      return groups;
    }, new Map<string, GroupedSession>());

    // Sort sessions within each container by start time
    this.groupedSessions.forEach(group => {
      group.sessions.sort((a, b) => {
        return new Date(b.startTime ?? 0).getTime() - new Date(a.startTime ?? 0).getTime();
      });
    });
  }

  viewSession(session: PsSessionDTO): void {
    if (session.id) {
      this.printSession(session);
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

  printSession(session: PsSessionDTO): void {
    if (session.id) {
      this.playstationResourceService.printSession(session.id).subscribe({
        next: () => {
          // Success handling - you might want to show a success message
          this.toastr.success(this.translateService.instant('playstation.lastSessions.printSuccess'));
        },
        error: error => {
          console.error('Error printing session:', error);
          this.toastr.error(this.translateService.instant('playstation.lastSessions.printError'));
        },
      });
    }
  }
}
