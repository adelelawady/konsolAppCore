import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StoreItemDetailComponent } from './store-item-detail.component';

describe('StoreItem Management Detail Component', () => {
  let comp: StoreItemDetailComponent;
  let fixture: ComponentFixture<StoreItemDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StoreItemDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ storeItem: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(StoreItemDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StoreItemDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load storeItem on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.storeItem).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
