import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../price-option.test-samples';

import { PriceOptionFormService } from './price-option-form.service';

describe('PriceOption Form Service', () => {
  let service: PriceOptionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PriceOptionFormService);
  });

  describe('Service methods', () => {
    describe('createPriceOptionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPriceOptionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            productId: expect.any(Object),
          }),
        );
      });

      it('passing IPriceOption should create a new form with FormGroup', () => {
        const formGroup = service.createPriceOptionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            value: expect.any(Object),
            productId: expect.any(Object),
          }),
        );
      });
    });

    describe('getPriceOption', () => {
      it('should return NewPriceOption for default PriceOption initial value', () => {
        const formGroup = service.createPriceOptionFormGroup(sampleWithNewData);

        const priceOption = service.getPriceOption(formGroup) as any;

        expect(priceOption).toMatchObject(sampleWithNewData);
      });

      it('should return NewPriceOption for empty PriceOption initial value', () => {
        const formGroup = service.createPriceOptionFormGroup();

        const priceOption = service.getPriceOption(formGroup) as any;

        expect(priceOption).toMatchObject({});
      });

      it('should return IPriceOption', () => {
        const formGroup = service.createPriceOptionFormGroup(sampleWithRequiredData);

        const priceOption = service.getPriceOption(formGroup) as any;

        expect(priceOption).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPriceOption should not enable id FormControl', () => {
        const formGroup = service.createPriceOptionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPriceOption should disable id FormControl', () => {
        const formGroup = service.createPriceOptionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
