import { Component, OnInit } from '@angular/core';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { SharedModule } from 'app/shared/shared.module';
import { faSync, faEye, faEdit, faClock, faSpinner } from '@fortawesome/free-solid-svg-icons';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SessionInvoiceViewComponent } from './session-invoice-view.component';
import { Router, ActivatedRoute } from '@angular/router';
import { PaginationSearchModel, PlaystationContainer } from 'app/core/konsolApi';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'jhi-session-list',
  templateUrl: './session-list.component.html',
  styleUrls: ['./session-list.component.scss'],
})
export class SessionListComponent implements OnInit {
  sessions: PsSessionDTO[] = [];
  isLoading = false;
  container: PlaystationContainer | null | undefined;
  Math = Math;

  // Pagination
  page = 1;
  pageSize = 10;
  totalItems = 0;
  sortField = 'startTime';
  sortOrder = 'desc';

  // Icons
  faSync = faSync;
  faEye = faEye;
  faEdit = faEdit;
  faClock = faClock;
  faSpinner = faSpinner;

  constructor(
    private toastr: ToastrService,
    private translateService: TranslateService,
    private playstationResourceService: PlaystationResourceService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // First try to get container from resolver data
    this.route.data.subscribe(data => {
      if (data['container']) {
        // eslint-disable-next-line no-console
        this.container = data['container'];
        this.loadSessions();
      }
    });
  }

  loadSessions(): void {
    this.isLoading = true;
    if (!this.container || !this.container.id) {
      return;
    }

    const searchModel: PaginationSearchModel = {
      page: this.page - 1, // Backend expects 0-based page index
      size: this.pageSize,
      sortField: this.sortField,
      sortOrder: this.sortOrder,
    };
    this.playstationResourceService.getSessionsByContainerId(this.container.id, searchModel, 'response').subscribe({
      next: (response: HttpResponse<PsSessionDTO[]>) => {
        this.sessions = response.body ?? [];
        this.parsePaginationHeaders(response.headers);
        this.isLoading = false;
      },
      error: error => {
        console.error('Error loading sessions:', error);
        this.isLoading = false;
        this.toastr.error(this.translateService.instant('playstation.sessionHistory.error.loading'));
      },
    });
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

  onPageChange(page: number): void {
    this.page = page;
    this.loadSessions();
  }

  parsePaginationHeaders(headers: HttpHeaders): void {
    const totalCountHeader = headers.get('X-Total-Count');
    this.totalItems = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;
  }
  viewSession(session: PsSessionDTO): void {
    if (session.id) {
      this.router.navigate([session.id, 'view'], { relativeTo: this.route });
    }
  }

  trackById(index: number, session: PsSessionDTO): string | undefined {
    return session.id;
  }
}
