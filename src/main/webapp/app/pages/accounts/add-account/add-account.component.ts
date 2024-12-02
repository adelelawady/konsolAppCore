import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AccountUserDTO } from 'app/core/konsolApi/model/accountUserDTO';
import { AccountUserResourceService } from 'app/core/konsolApi/api/accountUserResource.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';

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

  accountForm: FormGroup;
  loading = false;
  accountKinds: AccountUserDTO['kind'][] = ['CUSTOMER', 'SUPPLIER', 'SALEMAN'];

  constructor(
    private fb: FormBuilder,
    private accountService: AccountUserResourceService,
    private toastr: ToastrService,
    private translate: TranslateService
  ) {
    this.accountForm = this.createForm();
  }

  ngOnInit(): void {
    if (this.account) {
      this.accountForm.patchValue(this.account);
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      id: [null],
      name: ['', [Validators.required, Validators.minLength(3)]],
      kind: ['CUSTOMER', Validators.required],
      phone: [''],
      address: [''],
      address2: [''],
      email: ['', [Validators.email]],
      notes: [''],
      active: [true],
      balanceIn: [0],
      balanceOut: [0],
    });
  }

  onSubmit(): void {
    if (this.accountForm.invalid || this.loading) {
      return;
    }

    this.loading = true;
    const accountData: AccountUserDTO = this.accountForm.value;

    const operation = accountData.id
      ? this.accountService.updateAccountUser(accountData, accountData.id)
      : this.accountService.createAccountUser(accountData);

    operation.subscribe({
      next: (response: AccountUserDTO) => {
        this.toastr.success(
          this.translate.instant(accountData.id ? 'accounts.messages.updateSuccess' : 'accounts.messages.createSuccess'),
          this.translate.instant('success.title')
        );
        this.accountSaved.emit(response);
        this.onClose();
      },
      error: error => {
        console.error('Error saving account:', error);
        this.toastr.error(
          this.translate.instant(accountData.id ? 'accounts.messages.updateError' : 'accounts.messages.createError'),
          this.translate.instant('error.title')
        );
        this.loading = false;
      },
    });
  }

  onClose(): void {
    this.accountForm.reset({ kind: 'CUSTOMER', active: true });
    this.closeModal.emit();
  }

  getFieldError(fieldName: string): string {
    const control = this.accountForm.get(fieldName);
    if (control?.errors && control.touched) {
      if (control.errors['required']) {
        return 'accounts.validation.required';
      }
      if (control.errors['minlength']) {
        return 'accounts.validation.minLength';
      }
      if (control.errors['email']) {
        return 'accounts.validation.email';
      }
    }
    return '';
  }
}
