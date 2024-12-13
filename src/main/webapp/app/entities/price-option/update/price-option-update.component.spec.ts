import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PriceOptionService } from '../service/price-option.service';
import { IPriceOption } from '../price-option.model';
import { PriceOptionFormService } from './price-option-form.service';

import { PriceOptionUpdateComponent } from './price-option-update.component';

describe('PriceOption Management Update Component', () => {
  let comp: PriceOptionUpdateComponent;
  let fixture: ComponentFixture<PriceOptionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let priceOptionFormService: PriceOptionFormService;
  let priceOptionService: PriceOptionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PriceOptionUpdateComponent],
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
      .overrideTemplate(PriceOptionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PriceOptionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    priceOptionFormService = TestBed.inject(PriceOptionFormService);
    priceOptionService = TestBed.inject(PriceOptionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const priceOption: IPriceOption = { id: 'CBA' };

      activatedRoute.data = of({ priceOption });
      comp.ngOnInit();

      expect(comp.priceOption).toEqual(priceOption);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceOption>>();
      const priceOption = { id: 'ABC' };
      jest.spyOn(priceOptionFormService, 'getPriceOption').mockReturnValue(priceOption);
      jest.spyOn(priceOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceOption }));
      saveSubject.complete();

      // THEN
      expect(priceOptionFormService.getPriceOption).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(priceOptionService.update).toHaveBeenCalledWith(expect.objectContaining(priceOption));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceOption>>();
      const priceOption = { id: 'ABC' };
      jest.spyOn(priceOptionFormService, 'getPriceOption').mockReturnValue({ id: null });
      jest.spyOn(priceOptionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceOption: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: priceOption }));
      saveSubject.complete();

      // THEN
      expect(priceOptionFormService.getPriceOption).toHaveBeenCalled();
      expect(priceOptionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPriceOption>>();
      const priceOption = { id: 'ABC' };
      jest.spyOn(priceOptionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ priceOption });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(priceOptionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
