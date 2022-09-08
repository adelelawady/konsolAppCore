import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MoneyFormService, MoneyFormGroup } from './money-form.service';
import { IMoney } from '../money.model';
import { MoneyService } from '../service/money.service';
import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IAccountUser } from 'app/entities/account-user/account-user.model';
import { AccountUserService } from 'app/entities/account-user/service/account-user.service';
import { MoneyKind } from 'app/entities/enumerations/money-kind.model';

@Component({
  selector: 'jhi-money-update',
  templateUrl: './money-update.component.html',
})
export class MoneyUpdateComponent implements OnInit {
  isSaving = false;
  money: IMoney | null = null;
  moneyKindValues = Object.keys(MoneyKind);

  banksSharedCollection: IBank[] = [];
  itemsSharedCollection: IItem[] = [];
  accountUsersSharedCollection: IAccountUser[] = [];

  editForm: MoneyFormGroup = this.moneyFormService.createMoneyFormGroup();

  constructor(
    protected moneyService: MoneyService,
    protected moneyFormService: MoneyFormService,
    protected bankService: BankService,
    protected itemService: ItemService,
    protected accountUserService: AccountUserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  compareAccountUser = (o1: IAccountUser | null, o2: IAccountUser | null): boolean => this.accountUserService.compareAccountUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ money }) => {
      this.money = money;
      if (money) {
        this.updateForm(money);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const money = this.moneyFormService.getMoney(this.editForm);
    if (money.id !== null) {
      this.subscribeToSaveResponse(this.moneyService.update(money));
    } else {
      this.subscribeToSaveResponse(this.moneyService.create(money));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMoney>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(money: IMoney): void {
    this.money = money;
    this.moneyFormService.resetForm(this.editForm, money);

    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(this.banksSharedCollection, money.bank);
    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, money.item);
    this.accountUsersSharedCollection = this.accountUserService.addAccountUserToCollectionIfMissing<IAccountUser>(
      this.accountUsersSharedCollection,
      money.account
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(map((banks: IBank[]) => this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.money?.bank)))
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));

    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.money?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));

    this.accountUserService
      .query()
      .pipe(map((res: HttpResponse<IAccountUser[]>) => res.body ?? []))
      .pipe(
        map((accountUsers: IAccountUser[]) =>
          this.accountUserService.addAccountUserToCollectionIfMissing<IAccountUser>(accountUsers, this.money?.account)
        )
      )
      .subscribe((accountUsers: IAccountUser[]) => (this.accountUsersSharedCollection = accountUsers));
  }
}
