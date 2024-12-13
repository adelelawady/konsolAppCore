jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, fakeAsync, inject, tick } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PlaystationDeviceTypeService } from '../service/playstation-device-type.service';

import { PlaystationDeviceTypeDeleteDialogComponent } from './playstation-device-type-delete-dialog.component';

describe('PlaystationDeviceType Management Delete Component', () => {
  let comp: PlaystationDeviceTypeDeleteDialogComponent;
  let fixture: ComponentFixture<PlaystationDeviceTypeDeleteDialogComponent>;
  let service: PlaystationDeviceTypeService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [PlaystationDeviceTypeDeleteDialogComponent],
      providers: [provideHttpClient(), NgbActiveModal],
    })
      .overrideTemplate(PlaystationDeviceTypeDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PlaystationDeviceTypeDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(PlaystationDeviceTypeService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('ABC');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('ABC');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
