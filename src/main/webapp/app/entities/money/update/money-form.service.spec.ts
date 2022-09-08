import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../money.test-samples';

import { MoneyFormService } from './money-form.service';

describe('Money Form Service', () => {
  let service: MoneyFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MoneyFormService);
  });

  describe('Service methods', () => {
    describe('createMoneyFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMoneyFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            pk: expect.any(Object),
            id: expect.any(Object),
            kind: expect.any(Object),
            moneyIn: expect.any(Object),
            moneyOut: expect.any(Object),
            bank: expect.any(Object),
            item: expect.any(Object),
            account: expect.any(Object),
          })
        );
      });

      it('passing IMoney should create a new form with FormGroup', () => {
        const formGroup = service.createMoneyFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            pk: expect.any(Object),
            id: expect.any(Object),
            kind: expect.any(Object),
            moneyIn: expect.any(Object),
            moneyOut: expect.any(Object),
            bank: expect.any(Object),
            item: expect.any(Object),
            account: expect.any(Object),
          })
        );
      });
    });

    describe('getMoney', () => {
      it('should return NewMoney for default Money initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMoneyFormGroup(sampleWithNewData);

        const money = service.getMoney(formGroup) as any;

        expect(money).toMatchObject(sampleWithNewData);
      });

      it('should return NewMoney for empty Money initial value', () => {
        const formGroup = service.createMoneyFormGroup();

        const money = service.getMoney(formGroup) as any;

        expect(money).toMatchObject({});
      });

      it('should return IMoney', () => {
        const formGroup = service.createMoneyFormGroup(sampleWithRequiredData);

        const money = service.getMoney(formGroup) as any;

        expect(money).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMoney should not enable id FormControl', () => {
        const formGroup = service.createMoneyFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMoney should disable id FormControl', () => {
        const formGroup = service.createMoneyFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
