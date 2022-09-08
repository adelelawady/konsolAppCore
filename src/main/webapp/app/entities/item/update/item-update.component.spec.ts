import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ItemFormService } from './item-form.service';
import { ItemService } from '../service/item.service';
import { IItem } from '../item.model';
import { IItemUnit } from 'app/entities/item-unit/item-unit.model';
import { ItemUnitService } from 'app/entities/item-unit/service/item-unit.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Item Management Update Component', () => {
  let comp: ItemUpdateComponent;
  let fixture: ComponentFixture<ItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let itemFormService: ItemFormService;
  let itemService: ItemService;
  let itemUnitService: ItemUnitService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ItemUpdateComponent],
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
      .overrideTemplate(ItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    itemFormService = TestBed.inject(ItemFormService);
    itemService = TestBed.inject(ItemService);
    itemUnitService = TestBed.inject(ItemUnitService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ItemUnit query and add missing value', () => {
      const item: IItem = { id: 'CBA' };
      const itemUnits: IItemUnit[] = [{ id: '4169472e-1b5e-44ca-aeaa-7973a143180f' }];
      item.itemUnits = itemUnits;

      const itemUnitCollection: IItemUnit[] = [{ id: '21b8b0f0-bf16-4d71-8914-33ee4ac1f180' }];
      jest.spyOn(itemUnitService, 'query').mockReturnValue(of(new HttpResponse({ body: itemUnitCollection })));
      const additionalItemUnits = [...itemUnits];
      const expectedCollection: IItemUnit[] = [...additionalItemUnits, ...itemUnitCollection];
      jest.spyOn(itemUnitService, 'addItemUnitToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(itemUnitService.query).toHaveBeenCalled();
      expect(itemUnitService.addItemUnitToCollectionIfMissing).toHaveBeenCalledWith(
        itemUnitCollection,
        ...additionalItemUnits.map(expect.objectContaining)
      );
      expect(comp.itemUnitsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const item: IItem = { id: 'CBA' };
      const itemUnits: IItemUnit = { id: '963d67d2-771a-4920-bbb7-da4abf722f32' };
      item.itemUnits = [itemUnits];

      activatedRoute.data = of({ item });
      comp.ngOnInit();

      expect(comp.itemUnitsSharedCollection).toContain(itemUnits);
      expect(comp.item).toEqual(item);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 'ABC' };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue(item);
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(itemService.update).toHaveBeenCalledWith(expect.objectContaining(item));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 'ABC' };
      jest.spyOn(itemFormService, 'getItem').mockReturnValue({ id: null });
      jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: item }));
      saveSubject.complete();

      // THEN
      expect(itemFormService.getItem).toHaveBeenCalled();
      expect(itemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IItem>>();
      const item = { id: 'ABC' };
      jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ item });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(itemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItemUnit', () => {
      it('Should forward to itemUnitService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(itemUnitService, 'compareItemUnit');
        comp.compareItemUnit(entity, entity2);
        expect(itemUnitService.compareItemUnit).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
