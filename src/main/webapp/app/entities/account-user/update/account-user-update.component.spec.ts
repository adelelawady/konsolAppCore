import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AccountUserFormService } from './account-user-form.service';
import { AccountUserService } from '../service/account-user.service';
import { IAccountUser } from '../account-user.model';

import { AccountUserUpdateComponent } from './account-user-update.component';

describe('AccountUser Management Update Component', () => {
  let comp: AccountUserUpdateComponent;
  let fixture: ComponentFixture<AccountUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountUserFormService: AccountUserFormService;
  let accountUserService: AccountUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AccountUserUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AccountUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountUserFormService = TestBed.inject(AccountUserFormService);
    accountUserService = TestBed.inject(AccountUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const accountUser: IAccountUser = { id: 'CBA' };

      activatedRoute.data = of({ accountUser });
      comp.ngOnInit();

      expect(comp.accountUser).toEqual(accountUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountUser>>();
      const accountUser = { id: 'ABC' };
      jest.spyOn(accountUserFormService, 'getAccountUser').mockReturnValue(accountUser);
      jest.spyOn(accountUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountUser }));
      saveSubject.complete();

      // THEN
      expect(accountUserFormService.getAccountUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountUserService.update).toHaveBeenCalledWith(expect.objectContaining(accountUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountUser>>();
      const accountUser = { id: 'ABC' };
      jest.spyOn(accountUserFormService, 'getAccountUser').mockReturnValue({ id: null });
      jest.spyOn(accountUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountUser }));
      saveSubject.complete();

      // THEN
      expect(accountUserFormService.getAccountUser).toHaveBeenCalled();
      expect(accountUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountUser>>();
      const accountUser = { id: 'ABC' };
      jest.spyOn(accountUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
