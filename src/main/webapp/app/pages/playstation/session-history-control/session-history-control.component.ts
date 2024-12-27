import { Component, OnInit } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { PaginationSearchModel } from 'app/core/konsolApi/model/paginationSearchModel';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { 
  faSync, 
  faEye, 
  faTrash, 
  faClock, 
  faGamepad, 
  faCalendarAlt, 
  faMoneyBillWave,
  faPercent,
  faPlus,
  faFileInvoiceDollar,
  faSortUp,
  faSortDown 
} from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmationModalComponent } from 'app/shared/components/confirmation-modal/confirmation-modal.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-session-history-control',
  templateUrl: './session-history-control.component.html',
  styleUrls: ['./session-history-control.component.scss']
})
export class SessionHistoryControlComponent implements OnInit {
  sessions: PsSessionDTO[] = [];
  isLoading = false;
  
  // Pagination
  page = 1;
  pageSize = 10;
  totalItems = 0;
  sortField = 'startTime';
  sortOrder = 'desc';
  
  // Icons
  faSync = faSync;
  faEye = faEye;
  faTrash = faTrash;
  faClock = faClock;
  faGamepad = faGamepad;
  faCalendarAlt = faCalendarAlt;
  faMoneyBillWave = faMoneyBillWave;
  faPercent = faPercent;
  faPlus = faPlus;
  faFileInvoiceDollar = faFileInvoiceDollar;
  faSortUp = faSortUp;
  faSortDown = faSortDown;

  // Add Math for template usage
  Math = Math;

  constructor(
    private playstationResourceService: PlaystationResourceService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private translateService: TranslateService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadSessions();
  }

  loadSessions(): void {
    this.isLoading = true;

    const searchModel: PaginationSearchModel = {
      page: this.page - 1, // Backend expects 0-based page index
      size: this.pageSize,
      sortField: this.sortField,
      sortOrder: this.sortOrder
    };
    

    this.playstationResourceService.getSessions(searchModel, 'response').subscribe({
      next: (response: HttpResponse<PsSessionDTO[]>) => {
        this.sessions = response.body ?? [];
        this.parsePaginationHeaders(response.headers);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading sessions:', error);
        this.isLoading = false;
        this.toastr.error(this.translateService.instant('playstation.sessionHistory.error.loading'));
      }
    });
  }

  private parsePaginationHeaders(headers: HttpHeaders): void {
    const totalCountHeader = headers.get('X-Total-Count');
    this.totalItems = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
  }

  onPageChange(page: number): void {
    this.page = page;
    this.loadSessions();
  }

  onPageSizeChange(newSize: number): void {
    this.pageSize = Number(newSize);
    this.page = 1; // Reset to first page when changing page size
    this.loadSessions();
  }

  onSort(sortField: string): void {
    if (this.sortField === sortField) {
      // Toggle sort order if clicking the same field
      this.sortOrder = this.sortOrder === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = sortField;
      this.sortOrder = 'asc';
    }
    this.loadSessions();
  }

  getDuration(session: PsSessionDTO): string {
    if (!session.startTime || !session.endTime) {
      return '0h 0m';
    }

    const start = new Date(session.startTime);
    const end = new Date(session.endTime);
    const durationMs = end.getTime() - start.getTime();
    const hours = Math.floor(durationMs / (1000 * 60 * 60));
    const minutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
  }

  viewSession(session: PsSessionDTO): void {
    if (session.id) {
      this.router.navigate(['/playstation/sessions', session.id, 'view']);
    }
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