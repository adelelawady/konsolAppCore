import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SessionDetailsComponent } from './session-details.component';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';

describe('SessionDetailsComponent', () => {
  let component: SessionDetailsComponent;
  let fixture: ComponentFixture<SessionDetailsComponent>;
  let mockPlaystationResourceService: jasmine.SpyObj<PlaystationResourceService>;

  beforeEach(async () => {
    mockPlaystationResourceService = jasmine.createSpyObj('PlaystationResourceService', ['getSessionById']);

    await TestBed.configureTestingModule({
      declarations: [SessionDetailsComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { params: of({ id: '123' }) },
        },
        { provide: PlaystationResourceService, useValue: mockPlaystationResourceService },
      ],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SessionDetailsComponent);
    component = fixture.componentInstance;
    mockPlaystationResourceService.getSessionById.and.returnValue(
      of({ id: '123', type: { name: 'Game' }, invoice: { netPrice: 100 } } as PsSessionDTO)
    );
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load session details on init', () => {
    expect(component.session).toEqual(jasmine.objectContaining({ id: '123' }));
    expect(mockPlaystationResourceService.getSessionById).toHaveBeenCalledWith('123');
  });
});
