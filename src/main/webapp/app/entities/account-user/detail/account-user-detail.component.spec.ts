import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountUserDetailComponent } from './account-user-detail.component';

describe('AccountUser Management Detail Component', () => {
  let comp: AccountUserDetailComponent;
  let fixture: ComponentFixture<AccountUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AccountUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ accountUser: { id: 'ABC' } }) },
        },
      ],
    })
      .overrideTemplate(AccountUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AccountUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load accountUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.accountUser).toEqual(expect.objectContaining({ id: 'ABC' }));
    });
  });
});
