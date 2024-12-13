import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PlaystationDeviceService } from '../service/playstation-device.service';
import { IPlaystationDevice } from '../playstation-device.model';
import { PlaystationDeviceFormService } from './playstation-device-form.service';

import { PlaystationDeviceUpdateComponent } from './playstation-device-update.component';

describe('PlaystationDevice Management Update Component', () => {
  let comp: PlaystationDeviceUpdateComponent;
  let fixture: ComponentFixture<PlaystationDeviceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playstationDeviceFormService: PlaystationDeviceFormService;
  let playstationDeviceService: PlaystationDeviceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlaystationDeviceUpdateComponent],
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
      .overrideTemplate(PlaystationDeviceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaystationDeviceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playstationDeviceFormService = TestBed.inject(PlaystationDeviceFormService);
    playstationDeviceService = TestBed.inject(PlaystationDeviceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const playstationDevice: IPlaystationDevice = { id: 'CBA' };

      activatedRoute.data = of({ playstationDevice });
      comp.ngOnInit();

      expect(comp.playstationDevice).toEqual(playstationDevice);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationDevice>>();
      const playstationDevice = { id: 'ABC' };
      jest.spyOn(playstationDeviceFormService, 'getPlaystationDevice').mockReturnValue(playstationDevice);
      jest.spyOn(playstationDeviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationDevice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playstationDevice }));
      saveSubject.complete();

      // THEN
      expect(playstationDeviceFormService.getPlaystationDevice).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playstationDeviceService.update).toHaveBeenCalledWith(expect.objectContaining(playstationDevice));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationDevice>>();
      const playstationDevice = { id: 'ABC' };
      jest.spyOn(playstationDeviceFormService, 'getPlaystationDevice').mockReturnValue({ id: null });
      jest.spyOn(playstationDeviceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationDevice: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playstationDevice }));
      saveSubject.complete();

      // THEN
      expect(playstationDeviceFormService.getPlaystationDevice).toHaveBeenCalled();
      expect(playstationDeviceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationDevice>>();
      const playstationDevice = { id: 'ABC' };
      jest.spyOn(playstationDeviceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationDevice });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playstationDeviceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
