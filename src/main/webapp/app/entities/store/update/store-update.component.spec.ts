import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StoreFormService } from './store-form.service';
import { StoreService } from '../service/store.service';
import { IStore } from '../store.model';
import { IStoreItem } from 'app/entities/store-item/store-item.model';
import { StoreItemService } from 'app/entities/store-item/service/store-item.service';

import { StoreUpdateComponent } from './store-update.component';

describe('Store Management Update Component', () => {
  let comp: StoreUpdateComponent;
  let fixture: ComponentFixture<StoreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let storeFormService: StoreFormService;
  let storeService: StoreService;
  let storeItemService: StoreItemService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [StoreUpdateComponent],
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
      .overrideTemplate(StoreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StoreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    storeFormService = TestBed.inject(StoreFormService);
    storeService = TestBed.inject(StoreService);
    storeItemService = TestBed.inject(StoreItemService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call StoreItem query and add missing value', () => {
      const store: IStore = { id: 'CBA' };
      const items: IStoreItem[] = [{ id: 'd2274a9b-98fa-4236-b5e0-91fe05151683' }];
      store.items = items;

      const storeItemCollection: IStoreItem[] = [{ id: '17f8adc6-3040-4b35-871c-64bc9df7201d' }];
      jest.spyOn(storeItemService, 'query').mockReturnValue(of(new HttpResponse({ body: storeItemCollection })));
      const additionalStoreItems = [...items];
      const expectedCollection: IStoreItem[] = [...additionalStoreItems, ...storeItemCollection];
      jest.spyOn(storeItemService, 'addStoreItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ store });
      comp.ngOnInit();

      expect(storeItemService.query).toHaveBeenCalled();
      expect(storeItemService.addStoreItemToCollectionIfMissing).toHaveBeenCalledWith(
        storeItemCollection,
        ...additionalStoreItems.map(expect.objectContaining)
      );
      expect(comp.storeItemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const store: IStore = { id: 'CBA' };
      const items: IStoreItem = { id: 'a645f246-7097-4f6d-8377-557348ffa121' };
      store.items = [items];

      activatedRoute.data = of({ store });
      comp.ngOnInit();

      expect(comp.storeItemsSharedCollection).toContain(items);
      expect(comp.store).toEqual(store);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStore>>();
      const store = { id: 'ABC' };
      jest.spyOn(storeFormService, 'getStore').mockReturnValue(store);
      jest.spyOn(storeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ store });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: store }));
      saveSubject.complete();

      // THEN
      expect(storeFormService.getStore).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(storeService.update).toHaveBeenCalledWith(expect.objectContaining(store));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStore>>();
      const store = { id: 'ABC' };
      jest.spyOn(storeFormService, 'getStore').mockReturnValue({ id: null });
      jest.spyOn(storeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ store: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: store }));
      saveSubject.complete();

      // THEN
      expect(storeFormService.getStore).toHaveBeenCalled();
      expect(storeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStore>>();
      const store = { id: 'ABC' };
      jest.spyOn(storeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ store });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(storeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareStoreItem', () => {
      it('Should forward to storeItemService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(storeItemService, 'compareStoreItem');
        comp.compareStoreItem(entity, entity2);
        expect(storeItemService.compareStoreItem).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
