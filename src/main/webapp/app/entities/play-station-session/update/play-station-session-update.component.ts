import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPlayStationSession } from '../play-station-session.model';
import { PlayStationSessionService } from '../service/play-station-session.service';
import { PlayStationSessionFormGroup, PlayStationSessionFormService } from './play-station-session-form.service';

@Component({
  standalone: true,
  selector: 'jhi-play-station-session-update',
  templateUrl: './play-station-session-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PlayStationSessionUpdateComponent implements OnInit {
  isSaving = false;
  playStationSession: IPlayStationSession | null = null;

  protected playStationSessionService = inject(PlayStationSessionService);
  protected playStationSessionFormService = inject(PlayStationSessionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PlayStationSessionFormGroup = this.playStationSessionFormService.createPlayStationSessionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playStationSession }) => {
      this.playStationSession = playStationSession;
      if (playStationSession) {
        this.updateForm(playStationSession);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playStationSession = this.playStationSessionFormService.getPlayStationSession(this.editForm);
    if (playStationSession.id !== null) {
      this.subscribeToSaveResponse(this.playStationSessionService.update(playStationSession));
    } else {
      this.subscribeToSaveResponse(this.playStationSessionService.create(playStationSession));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayStationSession>>): void {
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

  protected updateForm(playStationSession: IPlayStationSession): void {
    this.playStationSession = playStationSession;
    this.playStationSessionFormService.resetForm(this.editForm, playStationSession);
  }
}
