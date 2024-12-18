import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlaystationContainer } from '../playstation-container.model';
import { PlaystationContainerService } from '../service/playstation-container.service';
import { PlaystationContainerFormGroup, PlaystationContainerFormService } from './playstation-container-form.service';

@Component({
  standalone: true,
  selector: 'jhi-playstation-container-update',
  templateUrl: './playstation-container-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlaystationContainerUpdateComponent implements OnInit {
  isSaving = false;
  playstationContainer: IPlaystationContainer | null = null;

  protected playstationContainerService = inject(PlaystationContainerService);
  protected playstationContainerFormService = inject(PlaystationContainerFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlaystationContainerFormGroup = this.playstationContainerFormService.createPlaystationContainerFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playstationContainer }) => {
      this.playstationContainer = playstationContainer;
      if (playstationContainer) {
        this.updateForm(playstationContainer);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playstationContainer = this.playstationContainerFormService.getPlaystationContainer(this.editForm);
    if (playstationContainer.id !== null) {
      this.subscribeToSaveResponse(this.playstationContainerService.update(playstationContainer));
    } else {
      this.subscribeToSaveResponse(this.playstationContainerService.create(playstationContainer));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlaystationContainer>>): void {
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

  protected updateForm(playstationContainer: IPlaystationContainer): void {
    this.playstationContainer = playstationContainer;
    this.playstationContainerFormService.resetForm(this.editForm, playstationContainer);
  }
}
