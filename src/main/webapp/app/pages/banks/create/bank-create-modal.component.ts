import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BankResourceService } from '../../../core/konsolApi/api/bankResource.service';

@Component({
  selector: 'jhi-bank-create-modal',
  template: `
    <div class="modal-header">
      <h4 class="modal-title" jhiTranslate="banks.create.title">Create New Bank</h4>
      <button type="button" class="btn-close" aria-label="Close" (click)="dismiss()"></button>
    </div>
    <div class="modal-body">
      <form [formGroup]="bankForm" (ngSubmit)="save()">
        <div class="mb-3">
          <label class="form-label" jhiTranslate="banks.name">Name</label>
          <input type="text" class="form-control" formControlName="name" />
          <div *ngIf="bankForm.get('name')?.invalid && (bankForm.get('name')?.dirty || bankForm.get('name')?.touched)" class="text-danger">
            <small *ngIf="bankForm.get('name')?.errors?.['required']" jhiTranslate="banks.validation.nameRequired">
              Name is required
            </small>
          </div>
        </div>
      </form>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" (click)="dismiss()" jhiTranslate="entity.action.cancel">Cancel</button>
      <button type="button" class="btn btn-primary" (click)="save()" [disabled]="bankForm.invalid" jhiTranslate="entity.action.save">
        Save
      </button>
    </div>
  `,
})
export class BankCreateModalComponent {
  bankForm: FormGroup;

  constructor(
    private activeModal: NgbActiveModal,
    private formBuilder: FormBuilder,
    private bankService: BankResourceService
  ) {
    this.bankForm = this.formBuilder.group({
      name: ['', [Validators.required]],
    });
  }

  save(): void {
    if (this.bankForm.valid) {
      this.bankService.createBank({ name: this.bankForm.get('name')?.value }).subscribe({
        next: (result) => {
          this.activeModal.close(result);
        },
        error: () => {
          // Handle error
        },
      });
    }
  }

  dismiss(): void {
    this.activeModal.dismiss();
  }
}
