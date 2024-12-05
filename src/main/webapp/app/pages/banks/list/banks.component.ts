import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { BankDTO, BankBalanceDTO } from '../../../core/konsolApi';
import { BankResourceService } from '../../../core/konsolApi/api/bankResource.service';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { BankCreateModalComponent } from '../create/bank-create-modal.component';

@Component({
  selector: 'jhi-banks',
  templateUrl: './banks.component.html',
  styleUrls: ['./banks.component.scss'],
})
export class BanksComponent implements OnInit {
  banks?: BankDTO[];
  isLoading = false;
  searchText = '';
  filteredBanks?: BankDTO[];
  selectedBank?: BankDTO;
  bankAnalysis?: BankBalanceDTO;

  constructor(
    private bankService: BankResourceService,
    private router: Router,
    private modalService: NgbModal,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.isLoading = true;
    this.bankService
      .getAllBanks()
      .pipe(finalize(() => (this.isLoading = false)))
      .subscribe({
        next: (banks: BankDTO[]) => {
          this.banks = banks;
          this.filterBanks();
          if (banks.length > 0) {
            this.selectBank(banks[0]);
          }
        },
        error: () => {
          // Handle error
        },
      });
  }

  selectBank(bank: BankDTO): void {
    this.selectedBank = bank;
    if (bank.id) {
      this.isLoading = true;
      this.bankService
        .getBankAnalysis(bank.id)
        .pipe(finalize(() => (this.isLoading = false)))
        .subscribe({
          next: (analysis: BankBalanceDTO) => {
            this.bankAnalysis = analysis;
          },
          error: () => {
            console.error('Failed to load bank analysis');
          },
        });
    }
  }

  onSearch(query: string): void {
    this.searchText = query;
    this.filterBanks();
  }

  filterBanks(): void {
    if (!this.banks) {
      return;
    }
    if (!this.searchText) {
      this.filteredBanks = this.banks;
      return;
    }
    const searchLower = this.searchText.toLowerCase();
    this.filteredBanks = this.banks.filter(bank => bank.name?.toLowerCase().includes(searchLower));
  }

  delete(id: string | undefined): void {
    if (!id) {
      return;
    }

    if (confirm(this.translateService.instant('entity.delete.warning'))) {
      this.bankService.deleteBank(id).subscribe({
        next: () => {
          this.loadAll();
          if (this.selectedBank?.id === id) {
            this.selectedBank = undefined;
            this.bankAnalysis = undefined;
          }
        },
        error: () => {
          console.error('Failed to delete bank');
        },
      });
    }
  }

  createNew(): void {
    const modalRef = this.modalService.open(BankCreateModalComponent, { size: 'lg' });
    modalRef.closed.subscribe(result => {
      if (result) {
        this.loadAll();
      }
    });
  }
}
