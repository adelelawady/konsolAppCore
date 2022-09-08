import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PkFormService } from './pk-form.service';
import { PkService } from '../service/pk.service';
import { IPk } from '../pk.model';

import { PkUpdateComponent } from './pk-update.component';

describe('Pk Management Update Component', () => {
  let comp: PkUpdateComponent;
  let fixture: ComponentFixture<PkUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pkFormService: PkFormService;
  let pkService: PkService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PkUpdateComponent],
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
      .overrideTemplate(PkUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PkUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pkFormService = TestBed.inject(PkFormService);
    pkService = TestBed.inject(PkService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const pk: IPk = { id: 'CBA' };

      activatedRoute.data = of({ pk });
      comp.ngOnInit();

      expect(comp.pk).toEqual(pk);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPk>>();
      const pk = { id: 'ABC' };
      jest.spyOn(pkFormService, 'getPk').mockReturnValue(pk);
      jest.spyOn(pkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pk });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pk }));
      saveSubject.complete();

      // THEN
      expect(pkFormService.getPk).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(pkService.update).toHaveBeenCalledWith(expect.objectContaining(pk));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPk>>();
      const pk = { id: 'ABC' };
      jest.spyOn(pkFormService, 'getPk').mockReturnValue({ id: null });
      jest.spyOn(pkService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pk: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: pk }));
      saveSubject.complete();

      // THEN
      expect(pkFormService.getPk).toHaveBeenCalled();
      expect(pkService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPk>>();
      const pk = { id: 'ABC' };
      jest.spyOn(pkService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ pk });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pkService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
