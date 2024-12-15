import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-device-type-form',
  templateUrl: './device-type-form.component.html',
})
export class DeviceTypeFormComponent implements OnInit {
  isSaving = false;
  deviceType?: PsDeviceType;
  editForm: FormGroup;

  constructor(
    private playstationResourceService: PlaystationResourceService,
    private formBuilder: FormBuilder,
    private activatedRoute: ActivatedRoute
  ) {
    this.editForm = this.formBuilder.group({
      id: [{ value: null, disabled: true }],
      name: ['', [Validators.required]],
      price: [null, [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deviceType }) => {
      if (deviceType) {
        this.deviceType = deviceType;
        this.updateForm(deviceType);
      }
    });
  }

  save(): void {
    this.isSaving = true;
    const deviceType = this.createFromForm();
    if (deviceType.id) {
      this.subscribeToSaveResponse(
        this.playstationResourceService.updateDeviceType(deviceType.id, deviceType)
      );
    } else {
      this.subscribeToSaveResponse(
        this.playstationResourceService.createPlayStationDeviceType(deviceType)
      );
    }
  }

  previousState(): void {
    window.history.back();
  }

  private createFromForm(): PsDeviceType {
    return {
      ...this.deviceType,
      id: this.editForm.get(['id'])?.value,
      name: this.editForm.get(['name'])?.value,
      price: this.editForm.get(['price'])?.value
    };
  }

  private updateForm(deviceType: PsDeviceType): void {
    this.editForm.patchValue({
      id: deviceType.id,
      name: deviceType.name,
      price: deviceType.price
    });
  }

  private subscribeToSaveResponse(result: Observable<PsDeviceType>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  private onSaveSuccess(): void {
    this.previousState();
  }

  private onSaveError(): void {
    // Handle error
  }

  private onSaveFinalize(): void {
    this.isSaving = false;
  }
} 