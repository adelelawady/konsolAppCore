import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemUnitFormService } from './item-unit-form.service';
import { ItemUnitService } from '../service/item-unit.service';
import { IItemUnit } from '../item-unit.model';

import { ItemUnitUpdateComponent } from './item-unit-update.component';

describe('ItemUnit Management Update Component', () => {
  let comp: ItemUnitUpdateComponent;
  let fixture: ComponentFixture<ItemUnitUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemUnitFormService: ItemUnitFormService;
  let itemUnitService: ItemUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemUnitUpdateComponent],
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
      .overrideTemplate(ItemUnitUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUnitUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemUnitFormService = TestBed.inject(ItemUnitFormService);
    itemUnitService = TestBed.inject(ItemUnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const itemUnit: IItemUnit = { id: 'CBA' };

      activatedRoute.data = of({ itemUnit });
      comp.ngOnInit();

      expect(comp.itemUnit).toEqual(itemUnit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemUnit>>();
      const itemUnit = { id: 'ABC' };
      jest.spyOn(itemUnitFormService, 'getItemUnit').mockReturnValue(itemUnit);
      jest.spyOn(itemUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemUnit }));
      saveSubject.complete();

      // THEN
      expect(itemUnitFormService.getItemUnit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemUnitService.update).toHaveBeenCalledWith(expect.objectContaining(itemUnit));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemUnit>>();
      const itemUnit = { id: 'ABC' };
      jest.spyOn(itemUnitFormService, 'getItemUnit').mockReturnValue({ id: null });
      jest.spyOn(itemUnitService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemUnit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: itemUnit }));
      saveSubject.complete();

      // THEN
      expect(itemUnitFormService.getItemUnit).toHaveBeenCalled();
      expect(itemUnitService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItemUnit>>();
      const itemUnit = { id: 'ABC' };
      jest.spyOn(itemUnitService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ itemUnit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemUnitService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
