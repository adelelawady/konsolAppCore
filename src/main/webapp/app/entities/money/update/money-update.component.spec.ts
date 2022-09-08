import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MoneyFormService } from './money-form.service';
import { MoneyService } from '../service/money.service';
import { IMoney } from '../money.model';
import { IBank } from 'app/entities/bank/bank.model';
import { BankService } from 'app/entities/bank/service/bank.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IAccountUser } from 'app/entities/account-user/account-user.model';
import { AccountUserService } from 'app/entities/account-user/service/account-user.service';

import { MoneyUpdateComponent } from './money-update.component';

describe('Money Management Update Component', () => {
  let comp: MoneyUpdateComponent;
  let fixture: ComponentFixture<MoneyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let moneyFormService: MoneyFormService;
  let moneyService: MoneyService;
  let bankService: BankService;
  let itemService: ItemService;
  let accountUserService: AccountUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MoneyUpdateComponent],
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
      .overrideTemplate(MoneyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MoneyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    moneyFormService = TestBed.inject(MoneyFormService);
    moneyService = TestBed.inject(MoneyService);
    bankService = TestBed.inject(BankService);
    itemService = TestBed.inject(ItemService);
    accountUserService = TestBed.inject(AccountUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Bank query and add missing value', () => {
      const money: IMoney = { id: 'CBA' };
      const bank: IBank = { id: 'e9f00a45-3ad8-414a-b8ab-6f9cfba178d6' };
      money.bank = bank;

      const bankCollection: IBank[] = [{ id: 'ffa6a303-a0c2-4416-8548-8f16817c5232' }];
      jest.spyOn(bankService, 'query').mockReturnValue(of(new HttpResponse({ body: bankCollection })));
      const additionalBanks = [bank];
      const expectedCollection: IBank[] = [...additionalBanks, ...bankCollection];
      jest.spyOn(bankService, 'addBankToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ money });
      comp.ngOnInit();

      expect(bankService.query).toHaveBeenCalled();
      expect(bankService.addBankToCollectionIfMissing).toHaveBeenCalledWith(
        bankCollection,
        ...additionalBanks.map(expect.objectContaining)
      );
      expect(comp.banksSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Item query and add missing value', () => {
      const money: IMoney = { id: 'CBA' };
      const item: IItem = { id: '63f0d2be-fca6-4350-b4fe-b514c810a1f5' };
      money.item = item;

      const itemCollection: IItem[] = [{ id: '90520191-e44c-4277-b8f1-76f69889d556' }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ money });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining)
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call AccountUser query and add missing value', () => {
      const money: IMoney = { id: 'CBA' };
      const account: IAccountUser = { id: '905b0453-b914-49a6-a9ba-398d9759c599' };
      money.account = account;

      const accountUserCollection: IAccountUser[] = [{ id: 'a1067969-1b4a-4bad-b861-7f627a963bef' }];
      jest.spyOn(accountUserService, 'query').mockReturnValue(of(new HttpResponse({ body: accountUserCollection })));
      const additionalAccountUsers = [account];
      const expectedCollection: IAccountUser[] = [...additionalAccountUsers, ...accountUserCollection];
      jest.spyOn(accountUserService, 'addAccountUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ money });
      comp.ngOnInit();

      expect(accountUserService.query).toHaveBeenCalled();
      expect(accountUserService.addAccountUserToCollectionIfMissing).toHaveBeenCalledWith(
        accountUserCollection,
        ...additionalAccountUsers.map(expect.objectContaining)
      );
      expect(comp.accountUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const money: IMoney = { id: 'CBA' };
      const bank: IBank = { id: 'c8ddf0ec-cc2e-447f-b670-1d026a3c36f4' };
      money.bank = bank;
      const item: IItem = { id: '1400829e-e75d-43f8-a680-9f02be254327' };
      money.item = item;
      const account: IAccountUser = { id: 'ae401619-3c68-4859-adbd-b497c1778f45' };
      money.account = account;

      activatedRoute.data = of({ money });
      comp.ngOnInit();

      expect(comp.banksSharedCollection).toContain(bank);
      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.accountUsersSharedCollection).toContain(account);
      expect(comp.money).toEqual(money);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoney>>();
      const money = { id: 'ABC' };
      jest.spyOn(moneyFormService, 'getMoney').mockReturnValue(money);
      jest.spyOn(moneyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ money });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: money }));
      saveSubject.complete();

      // THEN
      expect(moneyFormService.getMoney).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(moneyService.update).toHaveBeenCalledWith(expect.objectContaining(money));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoney>>();
      const money = { id: 'ABC' };
      jest.spyOn(moneyFormService, 'getMoney').mockReturnValue({ id: null });
      jest.spyOn(moneyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ money: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: money }));
      saveSubject.complete();

      // THEN
      expect(moneyFormService.getMoney).toHaveBeenCalled();
      expect(moneyService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMoney>>();
      const money = { id: 'ABC' };
      jest.spyOn(moneyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ money });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(moneyService.update).toHaveBeenCalled();
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
  });
});
