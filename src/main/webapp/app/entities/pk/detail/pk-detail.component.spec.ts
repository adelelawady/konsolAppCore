import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PkDetailComponent } from './pk-detail.component';

describe('Pk Management Detail Component', () => {
  let comp: PkDetailComponent;
  let fixture: ComponentFixture<PkDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PkDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ pk: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(PkDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PkDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load pk on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.pk).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
