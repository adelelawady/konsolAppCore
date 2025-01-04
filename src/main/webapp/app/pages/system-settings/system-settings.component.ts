import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { GLOBALService } from 'app/core/konsolApi/api/gLOBAL.service';
import { ServerSettings } from 'app/core/konsolApi/model/serverSettings';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'jhi-system-settings',
  templateUrl: './system-settings.component.html',
  styleUrls: ['./system-settings.component.scss'],
})
export class SystemSettingsComponent implements OnInit {
  settingsForm: FormGroup;
  loading = false;
  settings: ServerSettings = {};
  constructor(
    private globalService: GLOBALService,
    private fb: FormBuilder,
    private toastr: ToastrService,
    private translateService: TranslateService
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
      },
      error: error => {
        console.error('Error loading settings:', error);
        this.toastr.error('Error loading settings');
        this.loading = false;
      },
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
}
