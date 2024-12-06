import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BankResourceService } from '../../../core/konsolApi/api/bankResource.service';

@Component({
  selector: 'app-bank-form',
  template: `
    <div class="modal-header">
      <h4 class="modal-title">{{ bank.id ? 'Edit Bank' : 'Create New Bank' }}</h4>
      <button type="button" class="btn-close" aria-label="Close" (click)="dismiss()"></button>
    </div>
    <div class="modal-body">
      <form [formGroup]="bankForm">
        <div class="mb-3">
          <label class="form-label">Name</label>
          <input type="text" class="form-control" formControlName="name" />
        </div>
        <!-- Add other bank fields as needed -->
      </form>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" (click)="dismiss()">Cancel</button>
      <button type="button" class="btn btn-primary" (click)="save()" [disabled]="bankForm.invalid">Save</button>
    </div>
  `,
})
export class BankFormComponent implements OnInit {
  @Input() bank: any;
  bankForm: FormGroup;

  constructor(private activeModal: NgbActiveModal, private fb: FormBuilder, private bankService: BankResourceService) {
    this.bankForm = this.fb.group({
      name: ['', Validators.required],
      // Add other form controls as needed
    });
  }

  ngOnInit(): void {
    if (this.bank.id) {
      this.bankForm.patchValue(this.bank);
    }
  }

  save(): void {
    if (this.bankForm.valid) {
      const bankData = {
        ...this.bank,
        ...this.bankForm.value,
      };

      const request = this.bank.id
        ? this.bankService.updateBank(this.bank.id, bankData, 'response', true)
        : this.bankService.createBank(bankData, 'response', true);

      request.subscribe({
        next: response => {
          this.activeModal.close(response.body);
        },
        error: error => {
          console.error('Error saving bank:', error);
          // Handle error appropriately
        },
      });
    }
  }

  dismiss(): void {
    this.activeModal.dismiss();
  }
}
