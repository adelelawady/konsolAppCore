import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { PriceOptionDetailComponent } from './price-option-detail.component';

describe('PriceOption Management Detail Component', () => {
  let comp: PriceOptionDetailComponent;
  let fixture: ComponentFixture<PriceOptionDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PriceOptionDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./price-option-detail.component').then(m => m.PriceOptionDetailComponent),
              resolve: { priceOption: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PriceOptionDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PriceOptionDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load priceOption on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PriceOptionDetailComponent);

      // THEN
      expect(instance.priceOption()).toEqual(expect.objectContaining({ id: 'ABC' }));
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
