import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlayStationSession, NewPlayStationSession } from '../play-station-session.model';

export type PartialUpdatePlayStationSession = Partial<IPlayStationSession> & Pick<IPlayStationSession, 'id'>;

type RestOf<T extends IPlayStationSession | NewPlayStationSession> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestPlayStationSession = RestOf<IPlayStationSession>;

export type NewRestPlayStationSession = RestOf<NewPlayStationSession>;

export type PartialUpdateRestPlayStationSession = RestOf<PartialUpdatePlayStationSession>;

export type EntityResponseType = HttpResponse<IPlayStationSession>;
export type EntityArrayResponseType = HttpResponse<IPlayStationSession[]>;

@Injectable({ providedIn: 'root' })
export class PlayStationSessionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/play-station-sessions');

  create(playStationSession: NewPlayStationSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playStationSession);
    return this.http
      .post<RestPlayStationSession>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(playStationSession: IPlayStationSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playStationSession);
    return this.http
      .put<RestPlayStationSession>(`${this.resourceUrl}/${this.getPlayStationSessionIdentifier(playStationSession)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(playStationSession: PartialUpdatePlayStationSession): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(playStationSession);
    return this.http
      .patch<RestPlayStationSession>(`${this.resourceUrl}/${this.getPlayStationSessionIdentifier(playStationSession)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlayStationSession>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlayStationSession[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlayStationSessionIdentifier(playStationSession: Pick<IPlayStationSession, 'id'>): number {
    return playStationSession.id;
  }

  comparePlayStationSession(o1: Pick<IPlayStationSession, 'id'> | null, o2: Pick<IPlayStationSession, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlayStationSessionIdentifier(o1) === this.getPlayStationSessionIdentifier(o2) : o1 === o2;
  }

  addPlayStationSessionToCollectionIfMissing<Type extends Pick<IPlayStationSession, 'id'>>(
    playStationSessionCollection: Type[],
    ...playStationSessionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playStationSessions: Type[] = playStationSessionsToCheck.filter(isPresent);
    if (playStationSessions.length > 0) {
      const playStationSessionCollectionIdentifiers = playStationSessionCollection.map(playStationSessionItem =>
        this.getPlayStationSessionIdentifier(playStationSessionItem),
      );
      const playStationSessionsToAdd = playStationSessions.filter(playStationSessionItem => {
        const playStationSessionIdentifier = this.getPlayStationSessionIdentifier(playStationSessionItem);
        if (playStationSessionCollectionIdentifiers.includes(playStationSessionIdentifier)) {
          return false;
        }
        playStationSessionCollectionIdentifiers.push(playStationSessionIdentifier);
        return true;
      });
      return [...playStationSessionsToAdd, ...playStationSessionCollection];
    }
    return playStationSessionCollection;
  }

  protected convertDateFromClient<T extends IPlayStationSession | NewPlayStationSession | PartialUpdatePlayStationSession>(
    playStationSession: T,
  ): RestOf<T> {
    return {
      ...playStationSession,
      startTime: playStationSession.startTime?.toJSON() ?? null,
      endTime: playStationSession.endTime?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPlayStationSession: RestPlayStationSession): IPlayStationSession {
    return {
      ...restPlayStationSession,
      startTime: restPlayStationSession.startTime ? dayjs(restPlayStationSession.startTime) : undefined,
      endTime: restPlayStationSession.endTime ? dayjs(restPlayStationSession.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlayStationSession>): HttpResponse<IPlayStationSession> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlayStationSession[]>): HttpResponse<IPlayStationSession[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
