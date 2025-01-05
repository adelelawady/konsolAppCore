import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountUserDTO } from '../../../core/konsolApi/model/accountUserDTO';
import { AccountUserResourceService } from '../../../core/konsolApi/api/accountUserResource.service';

@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'app-add-account',
  templateUrl: './add-account.component.html',
  styleUrls: ['./add-account.component.scss'],
})
export class AddEmployeeAccountComponent implements OnInit {
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

  // eslint-disable-next-line @angular-eslint/use-lifecycle-interface
  ngOnChanges(): void {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    if (this.account && this.accountForm) {
      this.accountForm.patchValue({
        name: this.account.name,
        kind: 'EMPLOYEE',
        phone: this.account.phone,
        address: this.account.address,
        balanceIn: this.account.balanceIn ?? 0,
        balanceOut: this.account.balanceOut ?? 0,
      });
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    } else if (!this.account && this.accountForm) {
      this.accountForm.reset({
        kind: AccountUserDTO.KindEnum.Employee,
        balanceIn: 0,
        balanceOut: 0,
      });
    }
  }

  initForm(): void {
    this.accountForm = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3)]],
      kind: [AccountUserDTO.KindEnum.Employee, [Validators.required]],
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
      formData.kind = AccountUserDTO.KindEnum.Employee;
      const accountData: AccountUserDTO = {
        employee: true,
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
            kind: AccountUserDTO.KindEnum.Employee,
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
    if (control?.touched && control.errors) {
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
