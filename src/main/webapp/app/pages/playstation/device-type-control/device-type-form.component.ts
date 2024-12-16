import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PlaystationResourceService } from 'app/core/konsolApi/api/playstationResource.service';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize, map } from 'rxjs/operators';
import { PsDeviceType } from 'app/core/konsolApi/model/psDeviceType';

@Component({
  selector: 'jhi-device-type-form',
  templateUrl: './device-type-form.component.html'
})
export class DeviceTypeFormComponent implements OnInit {
  deviceTypeForm: FormGroup;
  isSaving = false;
  isEditing = false;

  constructor(
    private fb: FormBuilder,
    private playstationResourceService: PlaystationResourceService,
    private route: ActivatedRoute
  ) {
    this.deviceTypeForm = this.fb.group({
      id: [],
      name: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      price: ['', [Validators.required, Validators.min(0)]]
    });
  }

  ngOnInit(): void {
    this.route.data.subscribe(({ deviceType }) => {
      if (deviceType) {
        this.isEditing = true;
        this.updateForm(deviceType);
      }
    });
  }

  private updateForm(deviceType: PsDeviceType): void {
    this.deviceTypeForm.patchValue({
      id: deviceType.id,
      name: deviceType.name,
      price: deviceType.price
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    if (this.deviceTypeForm.invalid) {
      return;
    }

    this.isSaving = true;
    const deviceType = this.createFromForm();

    if (deviceType.id) {
      this.subscribeToSaveResponse(
        this.playstationResourceService.updateDeviceType(deviceType.id, deviceType).pipe(
          map(res => new HttpResponse({ body: res }))
        )
      );
    } else {
      this.subscribeToSaveResponse(
        this.playstationResourceService.createPlayStationDeviceType(deviceType).pipe(
          map(res => new HttpResponse({ body: res }))
        )
      );
    }
  }

  private createFromForm(): PsDeviceType {
    return {
      id: this.deviceTypeForm.get(['id'])?.value,
      name: this.deviceTypeForm.get(['name'])?.value,
      price: this.deviceTypeForm.get(['price'])?.value
    };
  }

  private subscribeToSaveResponse(result: Observable<HttpResponse<PsDeviceType>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError()
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