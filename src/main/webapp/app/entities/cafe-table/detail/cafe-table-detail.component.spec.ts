import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { CafeTableDetailComponent } from './cafe-table-detail.component';

describe('CafeTable Management Detail Component', () => {
  let comp: CafeTableDetailComponent;
  let fixture: ComponentFixture<CafeTableDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CafeTableDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./cafe-table-detail.component').then(m => m.CafeTableDetailComponent),
              resolve: { cafeTable: () => of({ id: 'ABC' }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CafeTableDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CafeTableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load cafeTable on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CafeTableDetailComponent);

      // THEN
      expect(instance.cafeTable()).toEqual(expect.objectContaining({ id: 'ABC' }));
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
