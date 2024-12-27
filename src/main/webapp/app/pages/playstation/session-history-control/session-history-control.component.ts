import { Component, OnInit } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { faSync, faEye, faTrash } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'app/shared/components/confirmation-modal/confirmation-modal.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-session-history-control',
  templateUrl: './session-history-control.component.html',
  styleUrls: ['./session-history-control.component.scss']
})
export class SessionHistoryControlComponent implements OnInit {
  sessions: PsSessionDTO[] = [];
  isLoading = false;
  
  // Icons
  faSync = faSync;
  faEye = faEye;
  faTrash = faTrash;

  constructor(
    private playstationResourceService: PlaystationResourceService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadSessions();
  }

  loadSessions(): void {
    this.isLoading = true;
    this.playstationResourceService.getSessions().subscribe({
      next: (sessions) => {
        this.sessions = sessions;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading sessions:', error);
        this.isLoading = false;
        this.toastr.error(this.translateService.instant('playstation.sessionHistory.error.loading'));
      }
    });
  }

  viewSession(session: PsSessionDTO): void {
    // Implement view logic
  }

  deleteSession(session: PsSessionDTO): void {
    const modal = this.modalService.open(ConfirmationModalComponent);
    modal.componentInstance.message = this.translateService.instant('playstation.sessionHistory.confirm.delete');

    modal.result.then((result) => {
      if (result === 'confirm' && session.id) {
        this.playstationResourceService.deleteSession(session.id).subscribe({
          next: () => {
            this.loadSessions();
            this.toastr.success(this.translateService.instant('playstation.sessionHistory.success.delete'));
          },
          error: (error) => {
            console.error('Error deleting session:', error);
            this.toastr.error(this.translateService.instant('playstation.sessionHistory.error.delete'));
          }
        });
      }
    });
  }

  calculateTotalCost(session: PsSessionDTO): number {
    let total = 0;
    if (session.invoice?.netPrice) {
      total += session.invoice.netPrice;
    }
    return total;
  }

  trackById(index: number, session: PsSessionDTO): string | undefined {
    return session.id;
  }
} 