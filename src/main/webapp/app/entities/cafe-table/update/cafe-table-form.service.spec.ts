import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../cafe-table.test-samples';

import { CafeTableFormService } from './cafe-table-form.service';

describe('CafeTable Form Service', () => {
  let service: CafeTableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CafeTableFormService);
  });

  describe('Service methods', () => {
    describe('createCafeTableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCafeTableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pk: expect.any(Object),
            name: expect.any(Object),
            index: expect.any(Object),
            active: expect.any(Object),
          }),
        );
      });

      it('passing ICafeTable should create a new form with FormGroup', () => {
        const formGroup = service.createCafeTableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            pk: expect.any(Object),
            name: expect.any(Object),
            index: expect.any(Object),
            active: expect.any(Object),
          }),
        );
      });
    });

    describe('getCafeTable', () => {
      it('should return NewCafeTable for default CafeTable initial value', () => {
        const formGroup = service.createCafeTableFormGroup(sampleWithNewData);

        const cafeTable = service.getCafeTable(formGroup) as any;

        expect(cafeTable).toMatchObject(sampleWithNewData);
      });

      it('should return NewCafeTable for empty CafeTable initial value', () => {
        const formGroup = service.createCafeTableFormGroup();

        const cafeTable = service.getCafeTable(formGroup) as any;

        expect(cafeTable).toMatchObject({});
      });

      it('should return ICafeTable', () => {
        const formGroup = service.createCafeTableFormGroup(sampleWithRequiredData);

        const cafeTable = service.getCafeTable(formGroup) as any;

        expect(cafeTable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICafeTable should not enable id FormControl', () => {
        const formGroup = service.createCafeTableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCafeTable should disable id FormControl', () => {
        const formGroup = service.createCafeTableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
