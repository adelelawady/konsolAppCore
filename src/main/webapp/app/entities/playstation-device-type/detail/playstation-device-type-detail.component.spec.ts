import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlaystationDeviceTypeDetailComponent } from './playstation-device-type-detail.component';

describe('PlaystationDeviceType Management Detail Component', () => {
  let comp: PlaystationDeviceTypeDetailComponent;
  let fixture: ComponentFixture<PlaystationDeviceTypeDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaystationDeviceTypeDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./playstation-device-type-detail.component').then(m => m.PlaystationDeviceTypeDetailComponent),
              resolve: { playstationDeviceType: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlaystationDeviceTypeDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaystationDeviceTypeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load playstationDeviceType on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlaystationDeviceTypeDetailComponent);

      // THEN
      expect(instance.playstationDeviceType()).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
