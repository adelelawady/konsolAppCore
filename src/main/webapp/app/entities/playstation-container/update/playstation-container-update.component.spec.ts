import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PlaystationContainerService } from '../service/playstation-container.service';
import { IPlaystationContainer } from '../playstation-container.model';
import { PlaystationContainerFormService } from './playstation-container-form.service';

import { PlaystationContainerUpdateComponent } from './playstation-container-update.component';

describe('PlaystationContainer Management Update Component', () => {
  let comp: PlaystationContainerUpdateComponent;
  let fixture: ComponentFixture<PlaystationContainerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playstationContainerFormService: PlaystationContainerFormService;
  let playstationContainerService: PlaystationContainerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlaystationContainerUpdateComponent],
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
      .overrideTemplate(PlaystationContainerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlaystationContainerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playstationContainerFormService = TestBed.inject(PlaystationContainerFormService);
    playstationContainerService = TestBed.inject(PlaystationContainerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const playstationContainer: IPlaystationContainer = { id: 'CBA' };

      activatedRoute.data = of({ playstationContainer });
      comp.ngOnInit();

      expect(comp.playstationContainer).toEqual(playstationContainer);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationContainer>>();
      const playstationContainer = { id: 'ABC' };
      jest.spyOn(playstationContainerFormService, 'getPlaystationContainer').mockReturnValue(playstationContainer);
      jest.spyOn(playstationContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playstationContainer }));
      saveSubject.complete();

      // THEN
      expect(playstationContainerFormService.getPlaystationContainer).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playstationContainerService.update).toHaveBeenCalledWith(expect.objectContaining(playstationContainer));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationContainer>>();
      const playstationContainer = { id: 'ABC' };
      jest.spyOn(playstationContainerFormService, 'getPlaystationContainer').mockReturnValue({ id: null });
      jest.spyOn(playstationContainerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationContainer: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playstationContainer }));
      saveSubject.complete();

      // THEN
      expect(playstationContainerFormService.getPlaystationContainer).toHaveBeenCalled();
      expect(playstationContainerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlaystationContainer>>();
      const playstationContainer = { id: 'ABC' };
      jest.spyOn(playstationContainerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playstationContainer });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playstationContainerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
