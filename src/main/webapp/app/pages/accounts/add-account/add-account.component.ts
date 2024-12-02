import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountUserDTO } from 'app/shared/model/account-user-dto.model';
import { AccountUserResourceService } from 'app/entities/account-user/account-user.service';

@Component({
  selector: 'app-add-account',
  templateUrl: './add-account.component.html',
  styleUrls: ['./add-account.component.scss'],
})
export class AddAccountComponent implements OnInit {
  @Input() showModal = false;
  @Input() account?: AccountUserDTO;
  @Output() closeModal = new EventEmitter<void>();
  @Output() accountSaved = new EventEmitter<AccountUserDTO>();

  accountForm!: FormGroup;
  loading = false;
  submitted = false;

  accountTypes = [
    { value: 'CUSTOMER', label: 'accounts.kind.CUSTOMER' },
    { value: 'SUPPLIER', label: 'accounts.kind.SUPPLIER' },
    { value: 'SALEMAN', label: 'accounts.kind.SALEMAN' },
  ];

  constructor(private fb: FormBuilder, private accountService: AccountUserResourceService) {
    this.initForm();
  }

  ngOnInit(): void {
    this.initForm();
  }

  ngOnChanges(): void {
    if (this.account && this.accountForm) {
      this.accountForm.patchValue({
        name: this.account.name,
        kind: this.account.kind,
        phone: this.account.phone,
        email: this.account.email,
        address: this.account.address,
        address2: this.account.address2,
        notes: this.account.notes,
        active: this.account.active,
      });
    } else if (!this.account && this.accountForm) {
      this.accountForm.reset({
        active: true,
      });
    }
  }

  private initForm(): void {
    this.accountForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      kind: ['CUSTOMER', [Validators.required]],
      phone: [''],
      email: ['', [Validators.email]],
      address: [''],
      address2: [''],
      notes: [''],
      active: [true],
    });
  }

  onSubmit(): void {
    this.submitted = true;

    if (this.accountForm.valid) {
      this.loading = true;
      const formData = this.accountForm.value;

      const accountData: AccountUserDTO = {
        ...formData,
        id: this.account?.id,
      };

      const request = this.account ? this.accountService.update(accountData) : this.accountService.create(accountData);

      request.subscribe({
        next: response => {
          this.accountSaved.emit(response);
          this.loading = false;
          this.submitted = false;
          this.accountForm.reset({ active: true });
        },
        error: error => {
          console.error('Error saving account:', error);
          this.loading = false;
        },
      });
    }
  }

  onClose(): void {
    this.submitted = false;
    this.closeModal.emit();
  }

  getFieldError(fieldName: string): string | null {
    const control = this.accountForm.get(fieldName);
    if (control?.invalid && (control.dirty || control.touched || this.submitted)) {
      if (control.errors?.['required']) {
        return 'accounts.validation.required';
      }
      if (control.errors?.['minlength']) {
        return 'accounts.validation.minLength';
      }
      if (control.errors?.['email']) {
        return 'accounts.validation.email';
      }
    }
    return null;
  }
}
