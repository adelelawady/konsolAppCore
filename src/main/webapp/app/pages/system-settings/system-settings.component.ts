import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GLOBALService } from 'app/core/konsolApi/api/gLOBAL.service';
import { ServerSettings } from 'app/core/konsolApi/model/serverSettings';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { StoreDTO } from 'app/core/konsolApi/model/storeDTO';
import { StoreResourceService } from 'app/core/konsolApi/api/storeResource.service';
import { BankDTO, BankResourceService } from 'app/core/konsolApi';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { PerformRestoreRequest } from 'app/core/konsolApi/model/performRestoreRequest';
import { LicenseDTO } from 'app/core/konsolApi/model/licenseDTO';

@Component({
  selector: 'jhi-system-settings',
  templateUrl: './system-settings.component.html',
  styleUrls: ['./system-settings.component.scss'],
})
export class SystemSettingsComponent implements OnInit {
  @ViewChild('restoreDialog') restoreDialog!: TemplateRef<any>;
  @ViewChild('licenseDialog') licenseDialog!: TemplateRef<any>;

  settingsForm!: FormGroup;
  loading = false;
  settings: ServerSettings = {};
  mainStore: StoreDTO | null = null;
  playstationStore: StoreDTO | null = null;
  mainBank: BankDTO | null = null;
  playstationBank: BankDTO | null = null;
  backupInProgress = false;
  restoreInProgress = false;
  restorePath = '';
  licenseKey: string = '';
  processingLicense = false;

  weekDays = [
    { value: 'MONDAY', label: 'systemSettings.backup.days.monday' },
    { value: 'TUESDAY', label: 'systemSettings.backup.days.tuesday' },
    { value: 'WEDNESDAY', label: 'systemSettings.backup.days.wednesday' },
    { value: 'THURSDAY', label: 'systemSettings.backup.days.thursday' },
    { value: 'FRIDAY', label: 'systemSettings.backup.days.friday' },
    { value: 'SATURDAY', label: 'systemSettings.backup.days.saturday' },
    { value: 'SUNDAY', label: 'systemSettings.backup.days.sunday' },
  ];

  constructor(
    private globalService: GLOBALService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private translateService: TranslateService,
    private storeService: StoreResourceService,
    private bankService: BankResourceService,
    private modalService: NgbModal
  ) {
    this.initForm();
  }

  private initForm(): void {
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
      BACKUP_ENABLED: [false],
      BACKUP_SCHEDULE_TYPE: ['DAILY'],
      BACKUP_TIME: ['23:00'],
      BACKUP_DAYS: [[]],
      BACKUP_RETENTION_DAYS: [30, [Validators.required, Validators.min(1)]],
      BACKUP_LOCATION: ['C:/KonsolBackups', Validators.required],
      BACKUP_INCLUDE_FILES: [true],
      BACKUP_COMPRESS: [true],
      MONGODB_DUMP_PATH: ['C:/Program Files/MongoDB/Tools/100/bin/mongodump.exe', Validators.required],
      MONGODB_RESTORE_PATH: ['C:/Program Files/MongoDB/Tools/100/bin/mongorestore.exe', Validators.required],
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

  isBackupDaySelected(day: string): boolean {
    const backupDays = this.settingsForm.get('BACKUP_DAYS')?.value || [];
    return backupDays.includes(day);
  }

  onBackupDayChange(event: any, day: string): void {
    const backupDays = this.settingsForm.get('BACKUP_DAYS')?.value || [];
    if (event.target.checked) {
      backupDays.push(day);
    } else {
      const index = backupDays.indexOf(day);
      if (index > -1) {
        backupDays.splice(index, 1);
      }
    }
    this.settingsForm.patchValue({ BACKUP_DAYS: backupDays });
  }

  performBackup(): void {
    this.backupInProgress = true;
    this.globalService.performBackup().subscribe({
      next: (success: boolean) => {
        if (success) {
          this.toastr.success(this.translateService.instant('systemSettings.backup.backupSuccess'));
        } else {
          this.toastr.error(this.translateService.instant('systemSettings.backup.backupError'));
        }
      },
      error: (error: Error) => {
        this.toastr.error(this.translateService.instant('systemSettings.backup.backupError'));
        console.error('Backup failed:', error);
      },
      complete: () => {
        this.backupInProgress = false;
      },
    });
  }

  openRestoreDialog(): void {
    this.restorePath = '';
    this.modalService.open(this.restoreDialog, { backdrop: 'static' });
  }

  closeRestoreDialog(): void {
    this.modalService.dismissAll();
  }

  performRestore(): void {
    this.restoreInProgress = true;
    const request: PerformRestoreRequest = {
      backupPath: this.restorePath,
    };

    this.globalService.performRestore(request).subscribe({
      next: (success: boolean) => {
        if (success) {
          this.toastr.success(this.translateService.instant('systemSettings.backup.restoreSuccess'));
          this.closeRestoreDialog();
        } else {
          this.toastr.error(this.translateService.instant('systemSettings.backup.restoreError'));
        }
      },
      error: (error: Error) => {
        this.toastr.error(this.translateService.instant('systemSettings.backup.restoreError'));
        console.error('Restore failed:', error);
      },
      complete: () => {
        this.restoreInProgress = false;
      },
    });
  }

  private showSuccessMessage(key: string): void {
    this.toastr.success(this.translateService.instant(key));
  }

  private showErrorMessage(key: string): void {
    this.toastr.error(this.translateService.instant(key));
  }

  calculateDaysRemaining(license: LicenseDTO): number {
    if (!license.expiryDate) return 0;
    const today = new Date();
    const expiry = new Date(license.expiryDate);
    const diffTime = expiry.getTime() - today.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  getLicenseDaysClass(license: LicenseDTO): string {
    const daysRemaining = this.calculateDaysRemaining(license);
    if (daysRemaining <= 0) return 'text-danger';
    if (daysRemaining <= 30) return 'text-warning';
    return 'text-success';
  }

  getLicenseStatus(license: LicenseDTO): string {
    const daysRemaining = this.calculateDaysRemaining(license);
    if (daysRemaining <= 0) return 'systemSettings.license.status.expired';
    return 'systemSettings.license.status.active';
  }

  getLicenseStatusClass(license: LicenseDTO): string {
    const daysRemaining = this.calculateDaysRemaining(license);
    return daysRemaining <= 0 ? 'badge bg-danger' : 'badge bg-success';
  }

  openLicenseDialog(): void {
    this.licenseKey = '';
    this.modalService.open(this.licenseDialog, { backdrop: 'static' });
  }

  closeLicenseDialog(): void {
    this.modalService.dismissAll();
  }

  processLicense(): void {
    if (!this.licenseKey) return;

    this.processingLicense = true;
    const request = {
      encryptedKey: this.licenseKey,
    };

    this.globalService.processSuperAdminLicense(request).subscribe({
      next: response => {
        this.toastr.success(this.translateService.instant('systemSettings.license.success'));
        this.closeLicenseDialog();
        this.loadSettings(); // Reload settings to show new license
      },
      error: error => {
        this.processingLicense = false;
        console.error('Error processing license:', error);
        this.toastr.error(error.error?.detail || this.translateService.instant('systemSettings.license.error'));
      },
      complete: () => {
        this.processingLicense = false;
      },
    });
  }
}
