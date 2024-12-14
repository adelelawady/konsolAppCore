import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { SharedModule } from 'app/shared/shared.module';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PlaystationDeviceFormGroup, PlaystationDeviceFormService } from './playstation-device-form.service';

@Component({
  standalone: true,
  selector: 'jhi-playstation-device-update',
  templateUrl: './playstation-device-update.component.html',
  imports: [[SharedModule], FormsModule, ReactiveFormsModule],
})
export class PlaystationDeviceUpdateComponent implements OnInit {
  isSaving = false;
  device: PsDeviceDTO | null = null;

  protected playstationResourceService = inject(PlaystationResourceService);
  protected playstationDeviceFormService = inject(PlaystationDeviceFormService);
  protected activatedRoute = inject(ActivatedRoute);

  editForm: PlaystationDeviceFormGroup = this.playstationDeviceFormService.createPlaystationDeviceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playstationDevice }) => {
      this.device = playstationDevice;
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
    const device = this.playstationDeviceFormService.getPlaystationDevice(this.editForm);
    if (device.id) {
      this.subscribeToSaveResponse(
        this.playstationResourceService.updateDevice(device.id, device)
      );
    } else {
      this.subscribeToSaveResponse(
        this.playstationResourceService.createPlayStationDevice(device)
      );
    }
  }

  protected subscribeToSaveResponse(result: Observable<PsDeviceDTO>): void {
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

  protected updateForm(device: PsDeviceDTO): void {
    this.device = device;
    this.playstationDeviceFormService.resetForm(this.editForm, device);
  }

  onActiveChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.editForm.get('active')?.setValue(checkbox.checked ? 'true' : 'false');
  }
}
