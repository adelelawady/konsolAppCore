import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../playstation-device.test-samples';

import { PlaystationDeviceFormService } from './playstation-device-form.service';

describe('PlaystationDevice Form Service', () => {
  let service: PlaystationDeviceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlaystationDeviceFormService);
  });

  describe('Service methods', () => {
    describe('createPlaystationDeviceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlaystationDeviceFormGroup();

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

      it('passing IPlaystationDevice should create a new form with FormGroup', () => {
        const formGroup = service.createPlaystationDeviceFormGroup(sampleWithRequiredData);

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

    describe('getPlaystationDevice', () => {
      it('should return NewPlaystationDevice for default PlaystationDevice initial value', () => {
        const formGroup = service.createPlaystationDeviceFormGroup(sampleWithNewData);

        const playstationDevice = service.getPlaystationDevice(formGroup) as any;

        expect(playstationDevice).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlaystationDevice for empty PlaystationDevice initial value', () => {
        const formGroup = service.createPlaystationDeviceFormGroup();

        const playstationDevice = service.getPlaystationDevice(formGroup) as any;

        expect(playstationDevice).toMatchObject({});
      });

      it('should return IPlaystationDevice', () => {
        const formGroup = service.createPlaystationDeviceFormGroup(sampleWithRequiredData);

        const playstationDevice = service.getPlaystationDevice(formGroup) as any;

        expect(playstationDevice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlaystationDevice should not enable id FormControl', () => {
        const formGroup = service.createPlaystationDeviceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlaystationDevice should disable id FormControl', () => {
        const formGroup = service.createPlaystationDeviceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
