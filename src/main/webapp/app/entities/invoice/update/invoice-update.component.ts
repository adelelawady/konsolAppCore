import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { InvoiceFormService, InvoiceFormGroup } from './invoice-form.service';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IAccountUser } from 'app/entities/account-user/account-user.model';
import { AccountUserService } from 'app/entities/account-user/service/account-user.service';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';
import { InvoiceKind } from 'app/entities/enumerations/invoice-kind.model';

@Component({
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;
  invoiceKindValues = Object.keys(InvoiceKind);

  banksSharedCollection: IBank[] = [];
  itemsSharedCollection: IItem[] = [];
  accountUsersSharedCollection: IAccountUser[] = [];
  invoiceItemsSharedCollection: IInvoiceItem[] = [];

  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  constructor(
    protected invoiceService: InvoiceService,
    protected invoiceFormService: InvoiceFormService,
    protected bankService: BankService,
    protected itemService: ItemService,
    protected accountUserService: AccountUserService,
    protected invoiceItemService: InvoiceItemService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareBank = (o1: IBank | null, o2: IBank | null): boolean => this.bankService.compareBank(o1, o2);

  compareItem = (o1: IItem | null, o2: IItem | null): boolean => this.itemService.compareItem(o1, o2);

  compareAccountUser = (o1: IAccountUser | null, o2: IAccountUser | null): boolean => this.accountUserService.compareAccountUser(o1, o2);

  compareInvoiceItem = (o1: IInvoiceItem | null, o2: IInvoiceItem | null): boolean => this.invoiceItemService.compareInvoiceItem(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.banksSharedCollection = this.bankService.addBankToCollectionIfMissing<IBank>(this.banksSharedCollection, invoice.bank);
    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing<IItem>(this.itemsSharedCollection, invoice.item);
    this.accountUsersSharedCollection = this.accountUserService.addAccountUserToCollectionIfMissing<IAccountUser>(
      this.accountUsersSharedCollection,
      invoice.account
    );
    this.invoiceItemsSharedCollection = this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(
      this.invoiceItemsSharedCollection,
      ...(invoice.invoiceItems ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.bankService
      .query()
      .pipe(map((res: HttpResponse<IBank[]>) => res.body ?? []))
      .pipe(map((banks: IBank[]) => this.bankService.addBankToCollectionIfMissing<IBank>(banks, this.invoice?.bank)))
      .subscribe((banks: IBank[]) => (this.banksSharedCollection = banks));

    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing<IItem>(items, this.invoice?.item)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));

    this.accountUserService
      .query()
      .pipe(map((res: HttpResponse<IAccountUser[]>) => res.body ?? []))
      .pipe(
        map((accountUsers: IAccountUser[]) =>
          this.accountUserService.addAccountUserToCollectionIfMissing<IAccountUser>(accountUsers, this.invoice?.account)
        )
      )
      .subscribe((accountUsers: IAccountUser[]) => (this.accountUsersSharedCollection = accountUsers));

    this.invoiceItemService
      .query()
      .pipe(map((res: HttpResponse<IInvoiceItem[]>) => res.body ?? []))
      .pipe(
        map((invoiceItems: IInvoiceItem[]) =>
          this.invoiceItemService.addInvoiceItemToCollectionIfMissing<IInvoiceItem>(invoiceItems, ...(this.invoice?.invoiceItems ?? []))
        )
      )
      .subscribe((invoiceItems: IInvoiceItem[]) => (this.invoiceItemsSharedCollection = invoiceItems));
  }
}
