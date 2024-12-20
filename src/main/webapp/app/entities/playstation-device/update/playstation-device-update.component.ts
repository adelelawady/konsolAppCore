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
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';
import { PlaystationContainerStateService } from 'app/pages/playstation/services/playstation-container.service';

@Component({
  standalone: true,
  selector: 'jhi-playstation-device-update',
  templateUrl: './playstation-device-update.component.html',
  imports: [[SharedModule], FormsModule, ReactiveFormsModule],
})
export class PlaystationDeviceUpdateComponent implements OnInit {
  isSaving = false;
  device: PsDeviceDTO | null = null;
  deviceTypes: PsDeviceType[] = [];
  private containerStateService: PlaystationContainerStateService = inject(PlaystationContainerStateService);

  playstationResourceService = inject(PlaystationResourceService);
  playstationDeviceFormService = inject(PlaystationDeviceFormService);
  activatedRoute = inject(ActivatedRoute);

  editForm: PlaystationDeviceFormGroup = this.playstationDeviceFormService.createPlaystationDeviceFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playstationDevice }) => {
      this.device = playstationDevice;
      // First load device types, then update form
      this.loadDeviceTypes().then(() => {
        if (playstationDevice) {
          this.updateForm(playstationDevice);
          // Find and set the matching device type
          if (playstationDevice.type) {
            const matchingType = this.deviceTypes.find(t => t.id === playstationDevice.type.id);
            if (matchingType) {
              this.editForm.patchValue({
                type: matchingType,
              });
            }
          }
        }
      });
    });
  }

  loadDeviceTypes(): Promise<void> {
    return new Promise(resolve => {
      this.playstationResourceService.getDevicesTypes().subscribe(types => {
        this.deviceTypes = types;
        resolve();
      });
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const device = this.playstationDeviceFormService.getPlaystationDevice(this.editForm);
    if (device.id) {
      this.subscribeToSaveResponse(this.playstationResourceService.updateDevice(device.id, device));
    } else {
      this.subscribeToSaveResponse(this.playstationResourceService.createPlayStationDevice(device));
    }
  }
  onActiveChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    this.editForm.get('active')?.setValue(checkbox.checked);
  }

  onTimeManagemnetChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    // eslint-disable-next-line no-console
    console.log(checkbox.checked);
    this.editForm.get('timeManagement')?.setValue(checkbox.checked);
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
}
