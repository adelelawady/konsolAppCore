import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { BankResourceService } from 'app/core/konsolApi/api/bankResource.service';
import { BankDTO } from 'app/core/konsolApi/model/bankDTO';

@Component({
  selector: 'app-bank-selector',
  templateUrl: './bank-selector.component.html',
  styleUrls: ['./bank-selector.component.scss'],
})
export class BankSelectorComponent implements OnInit {
  @Output() bankSelected = new EventEmitter<BankDTO>();

  banks: BankDTO[] = [];
  selectedBank: BankDTO | null = null;
  loading = false;
  errorMessage: string | null = null;

  constructor(private bankService: BankResourceService) {}

  ngOnInit(): void {
    this.loadBanks();
  }

  loadBanks(): void {
    this.loading = true;
    this.bankService.getAllBanks().subscribe({
      next: banks => {
        this.banks = banks;
        this.loading = false;
      },
      error: error => {
        console.error('Error loading banks:', error);
        this.loading = false;
        this.errorMessage = 'Failed to load banks';
      },
    });
  }

  onBankChange(): void {
    this.bankSelected.emit(this.selectedBank);
  }
}
