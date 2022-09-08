import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InvoiceFormService } from './invoice-form.service';
import { InvoiceService } from '../service/invoice.service';
import { IInvoice } from '../invoice.model';
import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IAccountUser } from 'app/entities/account-user/account-user.model';
import { AccountUserService } from 'app/entities/account-user/service/account-user.service';
import { IInvoiceItem } from 'app/entities/invoice-item/invoice-item.model';
import { InvoiceItemService } from 'app/entities/invoice-item/service/invoice-item.service';

import { InvoiceUpdateComponent } from './invoice-update.component';

describe('Invoice Management Update Component', () => {
  let comp: InvoiceUpdateComponent;
  let fixture: ComponentFixture<InvoiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let invoiceFormService: InvoiceFormService;
  let invoiceService: InvoiceService;
  let bankService: BankService;
  let itemService: ItemService;
  let accountUserService: AccountUserService;
  let invoiceItemService: InvoiceItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [InvoiceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(InvoiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InvoiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    invoiceFormService = TestBed.inject(InvoiceFormService);
    invoiceService = TestBed.inject(InvoiceService);
    bankService = TestBed.inject(BankService);
    itemService = TestBed.inject(ItemService);
    accountUserService = TestBed.inject(AccountUserService);
    invoiceItemService = TestBed.inject(InvoiceItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bank query and add missing value', () => {
      const invoice: IInvoice = { id: 'CBA' };
      const bank: IBank = { id: '1e42dda6-5438-4998-997a-37a9dc893122' };
      invoice.bank = bank;

      const bankCollection: IBank[] = [{ id: '0c5116ef-14a2-4579-92d9-ed9a20f74127' }];
      jest.spyOn(bankService, 'query').mockReturnValue(of(new HttpResponse({ body: bankCollection })));
      const additionalBanks = [bank];
      const expectedCollection: IBank[] = [...additionalBanks, ...bankCollection];
      jest.spyOn(bankService, 'addBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(bankService.query).toHaveBeenCalled();
      expect(bankService.addBankToCollectionIfMissing).toHaveBeenCalledWith(
        bankCollection,
        ...additionalBanks.map(expect.objectContaining)
      );
      expect(comp.banksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Item query and add missing value', () => {
      const invoice: IInvoice = { id: 'CBA' };
      const item: IItem = { id: 'fc8b36f3-724c-4616-bf02-c26053bea12b' };
      invoice.item = item;

      const itemCollection: IItem[] = [{ id: '7e5f5cfa-16c7-40c9-9f68-e0305e8b080d' }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining)
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountUser query and add missing value', () => {
      const invoice: IInvoice = { id: 'CBA' };
      const account: IAccountUser = { id: 'b9eefc17-4f23-4335-89da-3621117eb471' };
      invoice.account = account;

      const accountUserCollection: IAccountUser[] = [{ id: 'f3b90a59-62e3-43ad-b093-cc13509ddc9f' }];
      jest.spyOn(accountUserService, 'query').mockReturnValue(of(new HttpResponse({ body: accountUserCollection })));
      const additionalAccountUsers = [account];
      const expectedCollection: IAccountUser[] = [...additionalAccountUsers, ...accountUserCollection];
      jest.spyOn(accountUserService, 'addAccountUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(accountUserService.query).toHaveBeenCalled();
      expect(accountUserService.addAccountUserToCollectionIfMissing).toHaveBeenCalledWith(
        accountUserCollection,
        ...additionalAccountUsers.map(expect.objectContaining)
      );
      expect(comp.accountUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InvoiceItem query and add missing value', () => {
      const invoice: IInvoice = { id: 'CBA' };
      const invoiceItems: IInvoiceItem[] = [{ id: '6be33fe0-84fe-47fb-b86b-d5bcc0222b23' }];
      invoice.invoiceItems = invoiceItems;

      const invoiceItemCollection: IInvoiceItem[] = [{ id: '852ad81e-f5d0-40f8-a749-994368246258' }];
      jest.spyOn(invoiceItemService, 'query').mockReturnValue(of(new HttpResponse({ body: invoiceItemCollection })));
      const additionalInvoiceItems = [...invoiceItems];
      const expectedCollection: IInvoiceItem[] = [...additionalInvoiceItems, ...invoiceItemCollection];
      jest.spyOn(invoiceItemService, 'addInvoiceItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(invoiceItemService.query).toHaveBeenCalled();
      expect(invoiceItemService.addInvoiceItemToCollectionIfMissing).toHaveBeenCalledWith(
        invoiceItemCollection,
        ...additionalInvoiceItems.map(expect.objectContaining)
      );
      expect(comp.invoiceItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const invoice: IInvoice = { id: 'CBA' };
      const bank: IBank = { id: 'db1b0f5e-524d-4472-af8d-5aca73a7abcf' };
      invoice.bank = bank;
      const item: IItem = { id: 'ca7b17ac-fccc-4c94-a074-d598a3909f45' };
      invoice.item = item;
      const account: IAccountUser = { id: '371baf4b-71da-491f-bc3b-c4242f21b827' };
      invoice.account = account;
      const invoiceItems: IInvoiceItem = { id: 'a7397fe7-2de7-4e1d-a70d-35924bb80c08' };
      invoice.invoiceItems = [invoiceItems];

      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      expect(comp.banksSharedCollection).toContain(bank);
      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.accountUsersSharedCollection).toContain(account);
      expect(comp.invoiceItemsSharedCollection).toContain(invoiceItems);
      expect(comp.invoice).toEqual(invoice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 'ABC' };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue(invoice);
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(invoiceService.update).toHaveBeenCalledWith(expect.objectContaining(invoice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 'ABC' };
      jest.spyOn(invoiceFormService, 'getInvoice').mockReturnValue({ id: null });
      jest.spyOn(invoiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: invoice }));
      saveSubject.complete();

      // THEN
      expect(invoiceFormService.getInvoice).toHaveBeenCalled();
      expect(invoiceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInvoice>>();
      const invoice = { id: 'ABC' };
      jest.spyOn(invoiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ invoice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(invoiceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareBank', () => {
      it('Should forward to bankService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(bankService, 'compareBank');
        comp.compareBank(entity, entity2);
        expect(bankService.compareBank).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareAccountUser', () => {
      it('Should forward to accountUserService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(accountUserService, 'compareAccountUser');
        comp.compareAccountUser(entity, entity2);
        expect(accountUserService.compareAccountUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInvoiceItem', () => {
      it('Should forward to invoiceItemService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(invoiceItemService, 'compareInvoiceItem');
        comp.compareInvoiceItem(entity, entity2);
        expect(invoiceItemService.compareInvoiceItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
