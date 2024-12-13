import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlaystationDeviceType } from '../playstation-device-type.model';
import { PlaystationDeviceTypeService } from '../service/playstation-device-type.service';
import { PlaystationDeviceTypeFormGroup, PlaystationDeviceTypeFormService } from './playstation-device-type-form.service';

@Component({
  standalone: true,
  selector: 'jhi-playstation-device-type-update',
  templateUrl: './playstation-device-type-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlaystationDeviceTypeUpdateComponent implements OnInit {
  isSaving = false;
  playstationDeviceType: IPlaystationDeviceType | null = null;

  protected playstationDeviceTypeService = inject(PlaystationDeviceTypeService);
  protected playstationDeviceTypeFormService = inject(PlaystationDeviceTypeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlaystationDeviceTypeFormGroup = this.playstationDeviceTypeFormService.createPlaystationDeviceTypeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playstationDeviceType }) => {
      this.playstationDeviceType = playstationDeviceType;
      if (playstationDeviceType) {
        this.updateForm(playstationDeviceType);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playstationDeviceType = this.playstationDeviceTypeFormService.getPlaystationDeviceType(this.editForm);
    if (playstationDeviceType.id !== null) {
      this.subscribeToSaveResponse(this.playstationDeviceTypeService.update(playstationDeviceType));
    } else {
      this.subscribeToSaveResponse(this.playstationDeviceTypeService.create(playstationDeviceType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaystationDeviceType>>): void {
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

  protected updateForm(playstationDeviceType: IPlaystationDeviceType): void {
    this.playstationDeviceType = playstationDeviceType;
    this.playstationDeviceTypeFormService.resetForm(this.editForm, playstationDeviceType);
  }
}
