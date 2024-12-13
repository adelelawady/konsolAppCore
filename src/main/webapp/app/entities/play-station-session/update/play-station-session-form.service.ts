import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPlayStationSession, NewPlayStationSession } from '../play-station-session.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPlayStationSession for edit and NewPlayStationSessionFormGroupInput for create.
 */
type PlayStationSessionFormGroupInput = IPlayStationSession | PartialWithRequiredKeyOf<NewPlayStationSession>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IPlayStationSession | NewPlayStationSession> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

type PlayStationSessionFormRawValue = FormValueOf<IPlayStationSession>;

type NewPlayStationSessionFormRawValue = FormValueOf<NewPlayStationSession>;

type PlayStationSessionFormDefaults = Pick<NewPlayStationSession, 'id' | 'active' | 'startTime' | 'endTime'>;

type PlayStationSessionFormGroupContent = {
  id: FormControl<PlayStationSessionFormRawValue['id'] | NewPlayStationSession['id']>;
  active: FormControl<PlayStationSessionFormRawValue['active']>;
  startTime: FormControl<PlayStationSessionFormRawValue['startTime']>;
  endTime: FormControl<PlayStationSessionFormRawValue['endTime']>;
  invoiceId: FormControl<PlayStationSessionFormRawValue['invoiceId']>;
};

export type PlayStationSessionFormGroup = FormGroup<PlayStationSessionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PlayStationSessionFormService {
  createPlayStationSessionFormGroup(playStationSession: PlayStationSessionFormGroupInput = { id: null }): PlayStationSessionFormGroup {
    const playStationSessionRawValue = this.convertPlayStationSessionToPlayStationSessionRawValue({
      ...this.getFormDefaults(),
      ...playStationSession,
    });
    return new FormGroup<PlayStationSessionFormGroupContent>({
      id: new FormControl(
        { value: playStationSessionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      active: new FormControl(playStationSessionRawValue.active, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(playStationSessionRawValue.startTime, {
        validators: [Validators.required],
      }),
      endTime: new FormControl(playStationSessionRawValue.endTime),
      invoiceId: new FormControl(playStationSessionRawValue.invoiceId, {
        validators: [Validators.required],
      }),
    });
  }

  getPlayStationSession(form: PlayStationSessionFormGroup): IPlayStationSession | NewPlayStationSession {
    return this.convertPlayStationSessionRawValueToPlayStationSession(
      form.getRawValue() as PlayStationSessionFormRawValue | NewPlayStationSessionFormRawValue,
    );
  }

  resetForm(form: PlayStationSessionFormGroup, playStationSession: PlayStationSessionFormGroupInput): void {
    const playStationSessionRawValue = this.convertPlayStationSessionToPlayStationSessionRawValue({
      ...this.getFormDefaults(),
      ...playStationSession,
    });
    form.reset(
      {
        ...playStationSessionRawValue,
        id: { value: playStationSessionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): PlayStationSessionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      active: false,
      startTime: currentTime,
      endTime: currentTime,
    };
  }

  private convertPlayStationSessionRawValueToPlayStationSession(
    rawPlayStationSession: PlayStationSessionFormRawValue | NewPlayStationSessionFormRawValue,
  ): IPlayStationSession | NewPlayStationSession {
    return {
      ...rawPlayStationSession,
      startTime: dayjs(rawPlayStationSession.startTime, DATE_TIME_FORMAT),
      endTime: dayjs(rawPlayStationSession.endTime, DATE_TIME_FORMAT),
    };
  }

  private convertPlayStationSessionToPlayStationSessionRawValue(
    playStationSession: IPlayStationSession | (Partial<NewPlayStationSession> & PlayStationSessionFormDefaults),
  ): PlayStationSessionFormRawValue | PartialWithRequiredKeyOf<NewPlayStationSessionFormRawValue> {
    return {
      ...playStationSession,
      startTime: playStationSession.startTime ? playStationSession.startTime.format(DATE_TIME_FORMAT) : undefined,
      endTime: playStationSession.endTime ? playStationSession.endTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
