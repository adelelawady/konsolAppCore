import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../item-unit.test-samples';

import { ItemUnitFormService } from './item-unit-form.service';

describe('ItemUnit Form Service', () => {
  let service: ItemUnitFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ItemUnitFormService);
  });

  describe('Service methods', () => {
    describe('createItemUnitFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createItemUnitFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            pieces: expect.any(Object),
            price: expect.any(Object),
            items: expect.any(Object),
          })
        );
      });

      it('passing IItemUnit should create a new form with FormGroup', () => {
        const formGroup = service.createItemUnitFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            pieces: expect.any(Object),
            price: expect.any(Object),
            items: expect.any(Object),
          })
        );
      });
    });

    describe('getItemUnit', () => {
      it('should return NewItemUnit for default ItemUnit initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createItemUnitFormGroup(sampleWithNewData);

        const itemUnit = service.getItemUnit(formGroup) as any;

        expect(itemUnit).toMatchObject(sampleWithNewData);
      });

      it('should return NewItemUnit for empty ItemUnit initial value', () => {
        const formGroup = service.createItemUnitFormGroup();

        const itemUnit = service.getItemUnit(formGroup) as any;

        expect(itemUnit).toMatchObject({});
      });

      it('should return IItemUnit', () => {
        const formGroup = service.createItemUnitFormGroup(sampleWithRequiredData);

        const itemUnit = service.getItemUnit(formGroup) as any;

        expect(itemUnit).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IItemUnit should not enable id FormControl', () => {
        const formGroup = service.createItemUnitFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewItemUnit should disable id FormControl', () => {
        const formGroup = service.createItemUnitFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
