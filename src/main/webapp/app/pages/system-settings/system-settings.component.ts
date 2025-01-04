import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { GLOBALService } from 'app/core/konsolApi/api/gLOBAL.service';
import { ServerSettings } from 'app/core/konsolApi/model/serverSettings';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';
import { StoreResourceService } from 'app/core/konsolApi/api/storeResource.service';
import { BankDTO, BankResourceService } from 'app/core/konsolApi';

@Component({
  selector: 'jhi-system-settings',
  templateUrl: './system-settings.component.html',
  styleUrls: ['./system-settings.component.scss'],
})
export class SystemSettingsComponent implements OnInit {
  settingsForm: FormGroup;
  loading = false;
  settings: ServerSettings = {};
  mainStore: StoreDTO | null = null;
  playstationStore: StoreDTO | null = null;
  mainBank: BankDTO | null = null;
  playstationBank: BankDTO | null = null;

  constructor(
    private globalService: GLOBALService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private translateService: TranslateService,
    private storeService: StoreResourceService,
    private bankService: BankResourceService
  ) {
    this.settingsForm = this.fb.group({
      MAIN_SELECTED_STORE_ID: [''],
      MAIN_SELECTED_BANK_ID: [''],
      PLAYSTATION_SELECTED_STORE_ID: [''],
      PLAYSTATION_SELECTED_BANK_ID: [''],
      SALES_CHECK_ITEM_QTY: [false],
      SALES_UPDATE_ITEM_QTY_AFTER_SAVE: [false],
      PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE: [false],
      ALLOW_NEGATIVE_INVENTORY: [false],
      SAVE_INVOICE_DELETETED_INVOICEITEMS: [false],
    });
  }

  ngOnInit(): void {
    this.loadSettings();
  }

  loadSettings(): void {
    this.loading = true;
    this.globalService.getServerSettings().subscribe({
      next: (settings: ServerSettings) => {
        this.settings = settings;
        this.settingsForm.patchValue(settings);
        this.loading = false;
        this.loadMainStore();
        this.loadPlaystationStore();
        this.loadMainBank();
        this.loadPlaystationBank();
      },
      error: error => {
        console.error('Error loading settings:', error);
        this.toastr.error('Error loading settings');
        this.loading = false;
      },
    });
  }

  loadMainStore(): void {
    if (this.settings.MAIN_SELECTED_STORE_ID) {
      this.storeService.getStore(this.settings.MAIN_SELECTED_STORE_ID).subscribe(store => {
        this.mainStore = store;
      });
    }
  }

  loadPlaystationStore(): void {
    if (this.settings.PLAYSTATION_SELECTED_STORE_ID) {
      this.storeService.getStore(this.settings.PLAYSTATION_SELECTED_STORE_ID).subscribe(store => {
        this.playstationStore = store;
      });
    }
  }

  loadMainBank(): void {
    if (this.settings.MAIN_SELECTED_BANK_ID) {
      this.bankService.getBank(this.settings.MAIN_SELECTED_BANK_ID).subscribe(bank => {
        this.mainBank = bank;
      });
    }
  }

  loadPlaystationBank(): void {
    if (this.settings.PLAYSTATION_SELECTED_BANK_ID) {
      this.bankService.getBank(this.settings.PLAYSTATION_SELECTED_BANK_ID).subscribe(bank => {
        this.playstationBank = bank;
      });
    }
  }
  onMainBankSelected(bank: BankDTO): void {
    this.mainBank = bank;
    this.settingsForm.patchValue({
      MAIN_SELECTED_BANK_ID: bank.id,
    });
  }

  onPlaystationBankSelected(bank: BankDTO): void {
    this.playstationBank = bank;
    this.settingsForm.patchValue({
      PLAYSTATION_SELECTED_BANK_ID: bank.id,
    });
  }

  onSubmit(): void {
    if (this.settingsForm.valid) {
      this.loading = true;
      const settings: ServerSettings = this.settingsForm.value;
      settings.id = this.settings.id;
      this.globalService.updateServerSettings(settings).subscribe({
        next: () => {
          this.toastr.success('Settings updated successfully');
          this.loading = false;
        },
        error: error => {
          console.error('Error updating settings:', error);
          this.toastr.error('Error updating settings');
          this.loading = false;
        },
      });
    }
  }

  onMainStoreSelected(store: StoreDTO): void {
    this.mainStore = store;
    this.settingsForm.patchValue({
      MAIN_SELECTED_STORE_ID: store.id,
    });
  }

  onPlaystationStoreSelected(store: StoreDTO): void {
    this.playstationStore = store;
    this.settingsForm.patchValue({
      PLAYSTATION_SELECTED_STORE_ID: store.id,
    });
  }
}
