import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../playstation-container.test-samples';

import { PlaystationContainerFormService } from './playstation-container-form.service';

describe('PlaystationContainer Form Service', () => {
  let service: PlaystationContainerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlaystationContainerFormService);
  });

  describe('Service methods', () => {
    describe('createPlaystationContainerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlaystationContainerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            category: expect.any(Object),
            defaultIcon: expect.any(Object),
            hasTimeManagement: expect.any(Object),
            showType: expect.any(Object),
            showTime: expect.any(Object),
            canMoveDevice: expect.any(Object),
            canHaveMultiTimeManagement: expect.any(Object),
            acceptedOrderCategories: expect.any(Object),
            orderSelectedPriceCategory: expect.any(Object),
          })
        );
      });

      it('passing IPlaystationContainer should create a new form with FormGroup', () => {
        const formGroup = service.createPlaystationContainerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            category: expect.any(Object),
            defaultIcon: expect.any(Object),
            hasTimeManagement: expect.any(Object),
            showType: expect.any(Object),
            showTime: expect.any(Object),
            canMoveDevice: expect.any(Object),
            canHaveMultiTimeManagement: expect.any(Object),
            acceptedOrderCategories: expect.any(Object),
            orderSelectedPriceCategory: expect.any(Object),
          })
        );
      });
    });

    describe('getPlaystationContainer', () => {
      it('should return NewPlaystationContainer for default PlaystationContainer initial value', () => {
        const formGroup = service.createPlaystationContainerFormGroup(sampleWithNewData);

        const playstationContainer = service.getPlaystationContainer(formGroup) as any;

        expect(playstationContainer).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlaystationContainer for empty PlaystationContainer initial value', () => {
        const formGroup = service.createPlaystationContainerFormGroup();

        const playstationContainer = service.getPlaystationContainer(formGroup) as any;

        expect(playstationContainer).toMatchObject({});
      });

      it('should return IPlaystationContainer', () => {
        const formGroup = service.createPlaystationContainerFormGroup(sampleWithRequiredData);

        const playstationContainer = service.getPlaystationContainer(formGroup) as any;

        expect(playstationContainer).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlaystationContainer should not enable id FormControl', () => {
        const formGroup = service.createPlaystationContainerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlaystationContainer should disable id FormControl', () => {
        const formGroup = service.createPlaystationContainerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
