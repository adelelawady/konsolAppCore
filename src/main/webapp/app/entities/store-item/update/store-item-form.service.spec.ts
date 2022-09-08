import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../store-item.test-samples';

import { StoreItemFormService } from './store-item-form.service';

describe('StoreItem Form Service', () => {
  let service: StoreItemFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StoreItemFormService);
  });

  describe('Service methods', () => {
    describe('createStoreItemFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStoreItemFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            qty: expect.any(Object),
            item: expect.any(Object),
            storeIds: expect.any(Object),
          })
        );
      });

      it('passing IStoreItem should create a new form with FormGroup', () => {
        const formGroup = service.createStoreItemFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            qty: expect.any(Object),
            item: expect.any(Object),
            storeIds: expect.any(Object),
          })
        );
      });
    });

    describe('getStoreItem', () => {
      it('should return NewStoreItem for default StoreItem initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createStoreItemFormGroup(sampleWithNewData);

        const storeItem = service.getStoreItem(formGroup) as any;

        expect(storeItem).toMatchObject(sampleWithNewData);
      });

      it('should return NewStoreItem for empty StoreItem initial value', () => {
        const formGroup = service.createStoreItemFormGroup();

        const storeItem = service.getStoreItem(formGroup) as any;

        expect(storeItem).toMatchObject({});
      });

      it('should return IStoreItem', () => {
        const formGroup = service.createStoreItemFormGroup(sampleWithRequiredData);

        const storeItem = service.getStoreItem(formGroup) as any;

        expect(storeItem).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStoreItem should not enable id FormControl', () => {
        const formGroup = service.createStoreItemFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStoreItem should disable id FormControl', () => {
        const formGroup = service.createStoreItemFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
