import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../account-user.test-samples';

import { AccountUserFormService } from './account-user-form.service';

describe('AccountUser Form Service', () => {
  let service: AccountUserFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountUserFormService);
  });

  describe('Service methods', () => {
    describe('createAccountUserFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccountUserFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            kind: expect.any(Object),
            balanceIn: expect.any(Object),
            balanceOut: expect.any(Object),
            phone: expect.any(Object),
            address: expect.any(Object),
            address2: expect.any(Object),
          })
        );
      });

      it('passing IAccountUser should create a new form with FormGroup', () => {
        const formGroup = service.createAccountUserFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            kind: expect.any(Object),
            balanceIn: expect.any(Object),
            balanceOut: expect.any(Object),
            phone: expect.any(Object),
            address: expect.any(Object),
            address2: expect.any(Object),
          })
        );
      });
    });

    describe('getAccountUser', () => {
      it('should return NewAccountUser for default AccountUser initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAccountUserFormGroup(sampleWithNewData);

        const accountUser = service.getAccountUser(formGroup) as any;

        expect(accountUser).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccountUser for empty AccountUser initial value', () => {
        const formGroup = service.createAccountUserFormGroup();

        const accountUser = service.getAccountUser(formGroup) as any;

        expect(accountUser).toMatchObject({});
      });

      it('should return IAccountUser', () => {
        const formGroup = service.createAccountUserFormGroup(sampleWithRequiredData);

        const accountUser = service.getAccountUser(formGroup) as any;

        expect(accountUser).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccountUser should not enable id FormControl', () => {
        const formGroup = service.createAccountUserFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccountUser should disable id FormControl', () => {
        const formGroup = service.createAccountUserFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
