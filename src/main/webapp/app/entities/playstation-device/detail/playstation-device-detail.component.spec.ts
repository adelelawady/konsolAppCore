import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlaystationDeviceDetailComponent } from './playstation-device-detail.component';

describe('PlaystationDevice Management Detail Component', () => {
  let comp: PlaystationDeviceDetailComponent;
  let fixture: ComponentFixture<PlaystationDeviceDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlaystationDeviceDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./playstation-device-detail.component').then(m => m.PlaystationDeviceDetailComponent),
              resolve: { playstationDevice: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PlaystationDeviceDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PlaystationDeviceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load playstationDevice on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlaystationDeviceDetailComponent);

      // THEN
      expect(instance.playstationDevice()).toEqual(expect.objectContaining({ id: 'ABC' }));
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
