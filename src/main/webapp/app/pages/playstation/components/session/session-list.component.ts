import { Component, OnInit } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { HttpResponse } from '@angular/common/http';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SharedModule } from 'app/shared/shared.module';
import { 
  faSync, 
  faEye, 
  faEdit, 
  faClock,
  faSpinner
} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-session-list',
  templateUrl: './session-list.component.html',
  styleUrls: ['./session-list.component.scss'],
})
export class SessionListComponent implements OnInit {
  sessions: PsSessionDTO[] = [];
  isLoading = false;

  // Icons
  faSync = faSync;
  faEye = faEye;
  faEdit = faEdit;
  faClock = faClock;
  faSpinner = faSpinner;

  constructor(private playstationResourceService: PlaystationResourceService) {}

  ngOnInit(): void {
    this.loadSessions();
  }

  loadSessions(): void {
    this.isLoading = true;
    this.playstationResourceService.getSessions()
      .subscribe(
        (res: PsSessionDTO[]) => {
          this.sessions = res;
          this.isLoading = false;
        },
        () => {
          this.isLoading = false;
        }
    );
  }

  viewSession(session: PsSessionDTO): void {
    // Implement view logic
    console.log('View session:', session);
  }

  editSession(session: PsSessionDTO): void {
    // Implement edit logic
    console.log('Edit session:', session);
  }

  trackById(index: number, session: PsSessionDTO): string | undefined {
    return session.id;
  }
} 