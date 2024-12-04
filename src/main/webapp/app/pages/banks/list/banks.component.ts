import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';
import { BankDTO } from '../../../core/konsolApi';
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
  bankStats: { [key: string]: any } = {};

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
          this.generateFakeStats();
        },
        error: () => {
          // Handle error
        },
      });
  }

  generateFakeStats(): void {
    this.banks?.forEach(bank => {
      this.bankStats[bank.id!] = {
        balance: this.getRandomNumber(10000, 1000000),
        transactions: {
          today: this.getRandomNumber(10, 100),
          thisWeek: this.getRandomNumber(50, 500),
          thisMonth: this.getRandomNumber(200, 2000),
        },
        growth: {
          daily: this.getRandomNumber(-5, 15),
          weekly: this.getRandomNumber(-10, 30),
          monthly: this.getRandomNumber(-20, 50),
        },
        topTransactions: [
          {
            type: 'deposit',
            amount: this.getRandomNumber(1000, 50000),
            date: new Date(Date.now() - this.getRandomNumber(0, 7) * 24 * 60 * 60 * 1000),
          },
          {
            type: 'withdrawal',
            amount: this.getRandomNumber(1000, 50000),
            date: new Date(Date.now() - this.getRandomNumber(0, 7) * 24 * 60 * 60 * 1000),
          },
          {
            type: 'transfer',
            amount: this.getRandomNumber(1000, 50000),
            date: new Date(Date.now() - this.getRandomNumber(0, 7) * 24 * 60 * 60 * 1000),
          },
        ],
        accountTypes: {
          savings: this.getRandomNumber(100, 1000),
          checking: this.getRandomNumber(50, 500),
          business: this.getRandomNumber(10, 100),
        },
      };
    });
  }

  getRandomNumber(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }

  filterBanks(): void {
    if (!this.banks) {
      this.filteredBanks = [];
      return;
    }

    const search = this.searchText.toLowerCase();
    this.filteredBanks = this.banks.filter(
      bank => !search || (bank.name && bank.name.toLowerCase().includes(search)) || (bank.id && bank.id.toString().includes(search))
    );
  }

  selectBank(bank: BankDTO): void {
    this.selectedBank = bank;
  }

  onSearch(query: string): void {
    this.searchText = query;
    this.filterBanks();
  }

  delete(id: string | undefined): void {
    if (id) {
      this.bankService.deleteBank(id).subscribe({
        next: () => {
          this.loadAll();
        },
        error: () => {
          // Handle error
        },
      });
    }
  }

  refresh(): void {
    this.loadAll();
  }

  createNew(): void {
    const modalRef = this.modalService.open(BankCreateModalComponent, { backdrop: 'static' });
    modalRef.result.then(
      (result) => {
        if (result) {
          this.loadAll();
        }
      },
      () => {
        // Modal dismissed
      }
    );
  }

  getGrowthClass(value: number): string {
    return value > 0 ? 'text-success' : value < 0 ? 'text-danger' : 'text-muted';
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(value);
  }
}
