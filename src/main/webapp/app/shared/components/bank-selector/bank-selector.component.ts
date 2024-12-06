import { Component, OnInit, Output, EventEmitter, Input, OnChanges, SimpleChanges } from '@angular/core';
import { BankResourceService } from 'app/core/konsolApi/api/bankResource.service';
import { BankDTO } from 'app/core/konsolApi/model/bankDTO';

@Component({
  selector: 'app-bank-selector',
  templateUrl: './bank-selector.component.html',
  styleUrls: ['./bank-selector.component.scss'],
})
export class BankSelectorComponent implements OnInit, OnChanges {
  @Output() bankSelected = new EventEmitter<BankDTO>();
  @Input() selectedBank: any | null = null;

  banks: BankDTO[] = [];
  loading = false;
  errorMessage: string | null = null;

  constructor(private bankService: BankResourceService) {}

  ngOnInit(): void {
    this.loadBanks();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['selectedBank'] && changes['selectedBank'].currentValue) {
      const newBank = changes['selectedBank'].currentValue;
      // Only reload if we don't have the bank in our list
      if (this.banks.length > 0 && !this.banks.find(b => b.id === newBank.id)) {
        this.loadBanks();
      } else if (this.banks.length === 0) {
        // If we don't have any banks yet, load them
        this.loadBanks();
      } else {
        // We have the bank in our list, just select it
        const matchingBank = this.banks.find(b => b.id === newBank.id);
        if (matchingBank) {
          this.selectedBank = matchingBank;
        }
      }
    }
  }

  loadBanks(): void {
    this.loading = true;
    this.errorMessage = null;

    this.bankService.getAllBanks().subscribe({
      next: banks => {
        this.banks = banks;

        // If we have a selected bank, find and select it from loaded banks
        if (this.selectedBank) {
          const matchingBank = this.banks.find(b => b.id === this.selectedBank?.id);
          if (matchingBank) {
            this.selectedBank = matchingBank;
          }
        }

        this.loading = false;
      },
      error: error => {
        console.error('Error loading banks:', error);
        this.errorMessage = 'Error loading banks. Please try again.';
        this.loading = false;
      },
    });
  }

  onBankChange(): void {
    if (this.selectedBank) {
      this.bankSelected.emit(this.selectedBank);
    }
  }
}
