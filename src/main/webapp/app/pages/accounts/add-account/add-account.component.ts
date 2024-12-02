import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountUserDTO } from '../../../core/konsolApi/model/accountUserDTO';
import { AccountUserResourceService } from '../../../core/konsolApi/api/accountUserResource.service';

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

  accountTypes = Object.entries(AccountUserDTO.KindEnum).map(([key, value]) => ({
    value,
    label: `accounts.kind.${value}`,
  }));

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
        address: this.account.address,
        balanceIn: this.account.balanceIn || 0,
        balanceOut: this.account.balanceOut || 0,
      });
    } else if (!this.account && this.accountForm) {
      this.accountForm.reset({
        kind: AccountUserDTO.KindEnum.Customer,
        balanceIn: 0,
        balanceOut: 0,
      });
    }
  }

  private initForm(): void {
    this.accountForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      kind: [AccountUserDTO.KindEnum.Customer, [Validators.required]],
      phone: [''],
      address: [''],
      balanceIn: [0, [Validators.required]],
      balanceOut: [0, [Validators.required]],
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
        balanceIn: Number(formData.balanceIn),
        balanceOut: Number(formData.balanceOut),
      };

      const request = this.account?.id
        ? this.accountService.updateAccountUser(this.account.id, accountData)
        : this.accountService.createAccountUser(accountData);

      request.subscribe({
        next: response => {
          this.accountSaved.emit(response);
          this.loading = false;
          this.submitted = false;
          this.accountForm.reset({
            kind: AccountUserDTO.KindEnum.Customer,
            balanceIn: 0,
            balanceOut: 0,
          });
          this.onClose();
        },
        error: error => {
          console.error('Error saving account:', error);
          this.loading = false;
        },
      });
    }
  }

  getFieldError(field: string): string | null {
    const control = this.accountForm.get(field);
    if (control && control.touched && control.errors) {
      if (control.errors['required']) {
        return 'accounts.validation.required';
      }
      if (control.errors['minlength']) {
        return 'accounts.validation.minLength';
      }
    }
    return null;
  }

  onClose(): void {
    this.submitted = false;
    this.closeModal.emit();
  }
}
