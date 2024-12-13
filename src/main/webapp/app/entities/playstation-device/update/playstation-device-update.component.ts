import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlaystationDevice } from '../playstation-device.model';
import { PlaystationDeviceService } from '../service/playstation-device.service';
import { PlaystationDeviceFormGroup, PlaystationDeviceFormService } from './playstation-device-form.service';

@Component({
  standalone: true,
  selector: 'jhi-playstation-device-update',
  templateUrl: './playstation-device-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlaystationDeviceUpdateComponent implements OnInit {
  isSaving = false;
  playstationDevice: IPlaystationDevice | null = null;

  protected playstationDeviceService = inject(PlaystationDeviceService);
  protected playstationDeviceFormService = inject(PlaystationDeviceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlaystationDeviceFormGroup = this.playstationDeviceFormService.createPlaystationDeviceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playstationDevice }) => {
      this.playstationDevice = playstationDevice;
      if (playstationDevice) {
        this.updateForm(playstationDevice);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playstationDevice = this.playstationDeviceFormService.getPlaystationDevice(this.editForm);
    if (playstationDevice.id !== null) {
      this.subscribeToSaveResponse(this.playstationDeviceService.update(playstationDevice));
    } else {
      this.subscribeToSaveResponse(this.playstationDeviceService.create(playstationDevice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaystationDevice>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(playstationDevice: IPlaystationDevice): void {
    this.playstationDevice = playstationDevice;
    this.playstationDeviceFormService.resetForm(this.editForm, playstationDevice);
  }
}
