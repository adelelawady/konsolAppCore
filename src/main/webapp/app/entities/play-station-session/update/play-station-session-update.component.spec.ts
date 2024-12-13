import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { PlayStationSessionService } from '../service/play-station-session.service';
import { IPlayStationSession } from '../play-station-session.model';
import { PlayStationSessionFormService } from './play-station-session-form.service';

import { PlayStationSessionUpdateComponent } from './play-station-session-update.component';

describe('PlayStationSession Management Update Component', () => {
  let comp: PlayStationSessionUpdateComponent;
  let fixture: ComponentFixture<PlayStationSessionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let playStationSessionFormService: PlayStationSessionFormService;
  let playStationSessionService: PlayStationSessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlayStationSessionUpdateComponent],
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
      .overrideTemplate(PlayStationSessionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PlayStationSessionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    playStationSessionFormService = TestBed.inject(PlayStationSessionFormService);
    playStationSessionService = TestBed.inject(PlayStationSessionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const playStationSession: IPlayStationSession = { id: 456 };

      activatedRoute.data = of({ playStationSession });
      comp.ngOnInit();

      expect(comp.playStationSession).toEqual(playStationSession);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayStationSession>>();
      const playStationSession = { id: 123 };
      jest.spyOn(playStationSessionFormService, 'getPlayStationSession').mockReturnValue(playStationSession);
      jest.spyOn(playStationSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playStationSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playStationSession }));
      saveSubject.complete();

      // THEN
      expect(playStationSessionFormService.getPlayStationSession).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(playStationSessionService.update).toHaveBeenCalledWith(expect.objectContaining(playStationSession));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayStationSession>>();
      const playStationSession = { id: 123 };
      jest.spyOn(playStationSessionFormService, 'getPlayStationSession').mockReturnValue({ id: null });
      jest.spyOn(playStationSessionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playStationSession: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: playStationSession }));
      saveSubject.complete();

      // THEN
      expect(playStationSessionFormService.getPlayStationSession).toHaveBeenCalled();
      expect(playStationSessionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPlayStationSession>>();
      const playStationSession = { id: 123 };
      jest.spyOn(playStationSessionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ playStationSession });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(playStationSessionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
