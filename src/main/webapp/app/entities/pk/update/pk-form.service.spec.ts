import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../pk.test-samples';

import { PkFormService } from './pk-form.service';

describe('Pk Form Service', () => {
  let service: PkFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PkFormService);
  });

  describe('Service methods', () => {
    describe('createPkFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPkFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kind: expect.any(Object),
            value: expect.any(Object),
          })
        );
      });

      it('passing IPk should create a new form with FormGroup', () => {
        const formGroup = service.createPkFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            kind: expect.any(Object),
            value: expect.any(Object),
          })
        );
      });
    });

    describe('getPk', () => {
      it('should return NewPk for default Pk initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createPkFormGroup(sampleWithNewData);

        const pk = service.getPk(formGroup) as any;

        expect(pk).toMatchObject(sampleWithNewData);
      });

      it('should return NewPk for empty Pk initial value', () => {
        const formGroup = service.createPkFormGroup();

        const pk = service.getPk(formGroup) as any;

        expect(pk).toMatchObject({});
      });

      it('should return IPk', () => {
        const formGroup = service.createPkFormGroup(sampleWithRequiredData);

        const pk = service.getPk(formGroup) as any;

        expect(pk).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPk should not enable id FormControl', () => {
        const formGroup = service.createPkFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPk should disable id FormControl', () => {
        const formGroup = service.createPkFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
