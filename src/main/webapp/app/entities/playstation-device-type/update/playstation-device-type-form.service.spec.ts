import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../playstation-device-type.test-samples';

import { PlaystationDeviceTypeFormService } from './playstation-device-type-form.service';

describe('PlaystationDeviceType Form Service', () => {
  let service: PlaystationDeviceTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlaystationDeviceTypeFormService);
  });

  describe('Service methods', () => {
    describe('createPlaystationDeviceTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlaystationDeviceTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            defaultMainPrice: expect.any(Object),
            productId: expect.any(Object),
          }),
        );
      });

      it('passing IPlaystationDeviceType should create a new form with FormGroup', () => {
        const formGroup = service.createPlaystationDeviceTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            defaultMainPrice: expect.any(Object),
            productId: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlaystationDeviceType', () => {
      it('should return NewPlaystationDeviceType for default PlaystationDeviceType initial value', () => {
        const formGroup = service.createPlaystationDeviceTypeFormGroup(sampleWithNewData);

        const playstationDeviceType = service.getPlaystationDeviceType(formGroup) as any;

        expect(playstationDeviceType).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlaystationDeviceType for empty PlaystationDeviceType initial value', () => {
        const formGroup = service.createPlaystationDeviceTypeFormGroup();

        const playstationDeviceType = service.getPlaystationDeviceType(formGroup) as any;

        expect(playstationDeviceType).toMatchObject({});
      });

      it('should return IPlaystationDeviceType', () => {
        const formGroup = service.createPlaystationDeviceTypeFormGroup(sampleWithRequiredData);

        const playstationDeviceType = service.getPlaystationDeviceType(formGroup) as any;

        expect(playstationDeviceType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlaystationDeviceType should not enable id FormControl', () => {
        const formGroup = service.createPlaystationDeviceTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlaystationDeviceType should disable id FormControl', () => {
        const formGroup = service.createPlaystationDeviceTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
