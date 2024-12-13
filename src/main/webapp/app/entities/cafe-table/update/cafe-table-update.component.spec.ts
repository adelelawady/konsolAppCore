import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CafeTableService } from '../service/cafe-table.service';
import { ICafeTable } from '../cafe-table.model';
import { CafeTableFormService } from './cafe-table-form.service';

import { CafeTableUpdateComponent } from './cafe-table-update.component';

describe('CafeTable Management Update Component', () => {
  let comp: CafeTableUpdateComponent;
  let fixture: ComponentFixture<CafeTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let cafeTableFormService: CafeTableFormService;
  let cafeTableService: CafeTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CafeTableUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CafeTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CafeTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    cafeTableFormService = TestBed.inject(CafeTableFormService);
    cafeTableService = TestBed.inject(CafeTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const cafeTable: ICafeTable = { id: 'CBA' };

      activatedRoute.data = of({ cafeTable });
      comp.ngOnInit();

      expect(comp.cafeTable).toEqual(cafeTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICafeTable>>();
      const cafeTable = { id: 'ABC' };
      jest.spyOn(cafeTableFormService, 'getCafeTable').mockReturnValue(cafeTable);
      jest.spyOn(cafeTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cafeTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cafeTable }));
      saveSubject.complete();

      // THEN
      expect(cafeTableFormService.getCafeTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(cafeTableService.update).toHaveBeenCalledWith(expect.objectContaining(cafeTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICafeTable>>();
      const cafeTable = { id: 'ABC' };
      jest.spyOn(cafeTableFormService, 'getCafeTable').mockReturnValue({ id: null });
      jest.spyOn(cafeTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cafeTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: cafeTable }));
      saveSubject.complete();

      // THEN
      expect(cafeTableFormService.getCafeTable).toHaveBeenCalled();
      expect(cafeTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICafeTable>>();
      const cafeTable = { id: 'ABC' };
      jest.spyOn(cafeTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ cafeTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(cafeTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
