import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../play-station-session.test-samples';

import { PlayStationSessionFormService } from './play-station-session-form.service';

describe('PlayStationSession Form Service', () => {
  let service: PlayStationSessionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PlayStationSessionFormService);
  });

  describe('Service methods', () => {
    describe('createPlayStationSessionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createPlayStationSessionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            active: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            invoiceId: expect.any(Object),
          }),
        );
      });

      it('passing IPlayStationSession should create a new form with FormGroup', () => {
        const formGroup = service.createPlayStationSessionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            active: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            invoiceId: expect.any(Object),
          }),
        );
      });
    });

    describe('getPlayStationSession', () => {
      it('should return NewPlayStationSession for default PlayStationSession initial value', () => {
        const formGroup = service.createPlayStationSessionFormGroup(sampleWithNewData);

        const playStationSession = service.getPlayStationSession(formGroup) as any;

        expect(playStationSession).toMatchObject(sampleWithNewData);
      });

      it('should return NewPlayStationSession for empty PlayStationSession initial value', () => {
        const formGroup = service.createPlayStationSessionFormGroup();

        const playStationSession = service.getPlayStationSession(formGroup) as any;

        expect(playStationSession).toMatchObject({});
      });

      it('should return IPlayStationSession', () => {
        const formGroup = service.createPlayStationSessionFormGroup(sampleWithRequiredData);

        const playStationSession = service.getPlayStationSession(formGroup) as any;

        expect(playStationSession).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IPlayStationSession should not enable id FormControl', () => {
        const formGroup = service.createPlayStationSessionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewPlayStationSession should disable id FormControl', () => {
        const formGroup = service.createPlayStationSessionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
