import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StoreItemFormService } from './store-item-form.service';
import { StoreItemService } from '../service/store-item.service';
import { IStoreItem } from '../store-item.model';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';

import { StoreItemUpdateComponent } from './store-item-update.component';

describe('StoreItem Management Update Component', () => {
  let comp: StoreItemUpdateComponent;
  let fixture: ComponentFixture<StoreItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let storeItemFormService: StoreItemFormService;
  let storeItemService: StoreItemService;
  let itemService: ItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StoreItemUpdateComponent],
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
      .overrideTemplate(StoreItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StoreItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    storeItemFormService = TestBed.inject(StoreItemFormService);
    storeItemService = TestBed.inject(StoreItemService);
    itemService = TestBed.inject(ItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Item query and add missing value', () => {
      const storeItem: IStoreItem = { id: 'CBA' };
      const item: IItem = { id: '1227e593-7e30-4fb4-88f1-724c28dcd094' };
      storeItem.item = item;

      const itemCollection: IItem[] = [{ id: '2a9974a6-205c-4dac-9052-d68dbf778e30' }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ storeItem });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(
        itemCollection,
        ...additionalItems.map(expect.objectContaining)
      );
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const storeItem: IStoreItem = { id: 'CBA' };
      const item: IItem = { id: 'e6db177c-3838-4b59-8935-e25dc703a658' };
      storeItem.item = item;

      activatedRoute.data = of({ storeItem });
      comp.ngOnInit();

      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.storeItem).toEqual(storeItem);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStoreItem>>();
      const storeItem = { id: 'ABC' };
      jest.spyOn(storeItemFormService, 'getStoreItem').mockReturnValue(storeItem);
      jest.spyOn(storeItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storeItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: storeItem }));
      saveSubject.complete();

      // THEN
      expect(storeItemFormService.getStoreItem).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(storeItemService.update).toHaveBeenCalledWith(expect.objectContaining(storeItem));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStoreItem>>();
      const storeItem = { id: 'ABC' };
      jest.spyOn(storeItemFormService, 'getStoreItem').mockReturnValue({ id: null });
      jest.spyOn(storeItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storeItem: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: storeItem }));
      saveSubject.complete();

      // THEN
      expect(storeItemFormService.getStoreItem).toHaveBeenCalled();
      expect(storeItemService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStoreItem>>();
      const storeItem = { id: 'ABC' };
      jest.spyOn(storeItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ storeItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(storeItemService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareItem', () => {
      it('Should forward to itemService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(itemService, 'compareItem');
        comp.compareItem(entity, entity2);
        expect(itemService.compareItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
