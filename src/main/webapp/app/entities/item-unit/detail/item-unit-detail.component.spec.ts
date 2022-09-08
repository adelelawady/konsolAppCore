import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ItemUnitDetailComponent } from './item-unit-detail.component';

describe('ItemUnit Management Detail Component', () => {
  let comp: ItemUnitDetailComponent;
  let fixture: ComponentFixture<ItemUnitDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ItemUnitDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ itemUnit: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(ItemUnitDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ItemUnitDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load itemUnit on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.itemUnit).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
