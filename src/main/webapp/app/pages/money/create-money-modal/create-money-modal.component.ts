import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { MoneyResourceService } from '../../../core/konsolApi/api/moneyResource.service';
import { MoneyDTO } from '../../../core/konsolApi/model/moneyDTO';
import { CreateMoneyDTO } from '../../../core/konsolApi/model/createMoneyDTO';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-create-money-modal',
  templateUrl: './create-money-modal.component.html',
})
export class CreateMoneyModalComponent implements OnInit {
  @Input() money?: MoneyDTO;
  moneyForm: FormGroup;
  loading = false;
  isEdit = false;

  constructor(public activeModal: NgbActiveModal, private moneyService: MoneyResourceService, private fb: FormBuilder) {
    this.moneyForm = this.fb.group({
      kind: ['PAYMENT', Validators.required],
      details: ['', Validators.required],
      amount: [0, Validators.required],
      account: ['', Validators.required],
      bank: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    if (this.money) {
      this.isEdit = true;
      this.moneyForm.patchValue({
        kind: this.money.kind,
        details: this.money.details,
        bank: this.money.bank,
        account: this.money.account,
      });

      if (this.money.kind == 'PAYMENT') {
        this.moneyForm.patchValue({
          amount: this.money.moneyIn,
        });
      } else {
        this.moneyForm.patchValue({
          amount: this.money.moneyOut,
        });
      }
    }
  }

  onAccountSelect(account: any): void {
    this.moneyForm.patchValue({
      account,
    });
  }

  onBankSelect(bank: any): void {
    this.moneyForm.patchValue({
      bank,
    });
  }

  onSubmit(): void {
    if (this.moneyForm.valid) {
      this.loading = true;
      const formValue = this.moneyForm.value;
      const moneyDTO: any = {
        kind: formValue.kind,
        details: formValue.details,
        accountId: formValue.account?.id,
        bankId: formValue.bank?.id,
      };

      if (formValue.kind == 'PAYMENT') {
        moneyDTO.moneyIn = formValue.amount;
      } else if (formValue.kind == 'RECEIPT') {
        moneyDTO.moneyOut = formValue.amount;
      }

      if (this.isEdit && this.money?.id) {
        moneyDTO.id = this.money?.id;
        moneyDTO.bank = formValue.bank;
        moneyDTO.bank.name = 'BankName';
        moneyDTO.account = formValue.account;
        this.moneyService.updateMoney(this.money.id, moneyDTO).subscribe(
          response => {
            this.loading = false;
            this.activeModal.close(response);
          },
          error => {
            console.error('Error updating money:', error);
            this.loading = false;
          }
        );
      } else {
        this.moneyService.createMoney(moneyDTO).subscribe(
          response => {
            this.loading = false;
            this.activeModal.close(response);
          },
          error => {
            console.error('Error creating money:', error);
            this.loading = false;
          }
        );
      }
    }
  }

  dismiss(): void {
    this.activeModal.dismiss();
  }
}
