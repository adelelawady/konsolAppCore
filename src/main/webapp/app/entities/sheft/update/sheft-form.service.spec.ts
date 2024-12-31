import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../sheft.test-samples';

import { SheftFormService } from './sheft-form.service';

describe('Sheft Form Service', () => {
  let service: SheftFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SheftFormService);
  });

  describe('Service methods', () => {
    describe('createSheftFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSheftFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            active: expect.any(Object),
            assignedEmployee: expect.any(Object),
            duration: expect.any(Object),
            totalprice: expect.any(Object),
            totalCost: expect.any(Object),
            netPrice: expect.any(Object),
            netCost: expect.any(Object),
            netUserPrice: expect.any(Object),
            totalItemsOut: expect.any(Object),
            discount: expect.any(Object),
            invoicesAdditions: expect.any(Object),
            additions: expect.any(Object),
            additionsNotes: expect.any(Object),
            invoicesExpenses: expect.any(Object),
            sheftExpenses: expect.any(Object),
            totalinvoices: expect.any(Object),
            totaldeletedItems: expect.any(Object),
            totaldeletedItemsPrice: expect.any(Object),
            notes: expect.any(Object),
          })
        );
      });

      it('passing ISheft should create a new form with FormGroup', () => {
        const formGroup = service.createSheftFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            active: expect.any(Object),
            assignedEmployee: expect.any(Object),
            duration: expect.any(Object),
            totalprice: expect.any(Object),
            totalCost: expect.any(Object),
            netPrice: expect.any(Object),
            netCost: expect.any(Object),
            netUserPrice: expect.any(Object),
            totalItemsOut: expect.any(Object),
            discount: expect.any(Object),
            invoicesAdditions: expect.any(Object),
            additions: expect.any(Object),
            additionsNotes: expect.any(Object),
            invoicesExpenses: expect.any(Object),
            sheftExpenses: expect.any(Object),
            totalinvoices: expect.any(Object),
            totaldeletedItems: expect.any(Object),
            totaldeletedItemsPrice: expect.any(Object),
            notes: expect.any(Object),
          })
        );
      });
    });

    describe('getSheft', () => {
      it('should return NewSheft for default Sheft initial value', () => {
        const formGroup = service.createSheftFormGroup(sampleWithNewData);

        const sheft = service.getSheft(formGroup) as any;

        expect(sheft).toMatchObject(sampleWithNewData);
      });

      it('should return NewSheft for empty Sheft initial value', () => {
        const formGroup = service.createSheftFormGroup();

        const sheft = service.getSheft(formGroup) as any;

        expect(sheft).toMatchObject({});
      });

      it('should return ISheft', () => {
        const formGroup = service.createSheftFormGroup(sampleWithRequiredData);

        const sheft = service.getSheft(formGroup) as any;

        expect(sheft).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISheft should not enable id FormControl', () => {
        const formGroup = service.createSheftFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSheft should disable id FormControl', () => {
        const formGroup = service.createSheftFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
