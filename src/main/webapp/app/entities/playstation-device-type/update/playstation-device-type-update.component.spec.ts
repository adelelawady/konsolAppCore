import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PlaystationDeviceTypeService } from '../service/playstation-device-type.service';
import { IPlaystationDeviceType } from '../playstation-device-type.model';
import { PlaystationDeviceTypeFormService } from './playstation-device-type-form.service';

import { PlaystationDeviceTypeUpdateComponent } from './playstation-device-type-update.component';

describe('PlaystationDeviceType Management Update Component', () => {
  let comp: PlaystationDeviceTypeUpdateComponent;
  let fixture: ComponentFixture<PlaystationDeviceTypeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playstationDeviceTypeFormService: PlaystationDeviceTypeFormService;
  let playstationDeviceTypeService: PlaystationDeviceTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlaystationDeviceTypeUpdateComponent],
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
      .overrideTemplate(PlaystationDeviceTypeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaystationDeviceTypeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playstationDeviceTypeFormService = TestBed.inject(PlaystationDeviceTypeFormService);
    playstationDeviceTypeService = TestBed.inject(PlaystationDeviceTypeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const playstationDeviceType: IPlaystationDeviceType = { id: 'CBA' };

      activatedRoute.data = of({ playstationDeviceType });
      comp.ngOnInit();

      expect(comp.playstationDeviceType).toEqual(playstationDeviceType);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationDeviceType>>();
      const playstationDeviceType = { id: 'ABC' };
      jest.spyOn(playstationDeviceTypeFormService, 'getPlaystationDeviceType').mockReturnValue(playstationDeviceType);
      jest.spyOn(playstationDeviceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationDeviceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playstationDeviceType }));
      saveSubject.complete();

      // THEN
      expect(playstationDeviceTypeFormService.getPlaystationDeviceType).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playstationDeviceTypeService.update).toHaveBeenCalledWith(expect.objectContaining(playstationDeviceType));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationDeviceType>>();
      const playstationDeviceType = { id: 'ABC' };
      jest.spyOn(playstationDeviceTypeFormService, 'getPlaystationDeviceType').mockReturnValue({ id: null });
      jest.spyOn(playstationDeviceTypeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationDeviceType: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playstationDeviceType }));
      saveSubject.complete();

      // THEN
      expect(playstationDeviceTypeFormService.getPlaystationDeviceType).toHaveBeenCalled();
      expect(playstationDeviceTypeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationDeviceType>>();
      const playstationDeviceType = { id: 'ABC' };
      jest.spyOn(playstationDeviceTypeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationDeviceType });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playstationDeviceTypeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
