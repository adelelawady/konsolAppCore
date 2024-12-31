import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISheft, NewSheft } from '../sheft.model';

export type PartialUpdateSheft = Partial<ISheft> & Pick<ISheft, 'id'>;

type RestOf<T extends ISheft | NewSheft> = Omit<T, 'startTime' | 'endTime'> & {
  startTime?: string | null;
  endTime?: string | null;
};

export type RestSheft = RestOf<ISheft>;

export type NewRestSheft = RestOf<NewSheft>;

export type PartialUpdateRestSheft = RestOf<PartialUpdateSheft>;

export type EntityResponseType = HttpResponse<ISheft>;
export type EntityArrayResponseType = HttpResponse<ISheft[]>;

@Injectable({ providedIn: 'root' })
export class SheftService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shefts');

  create(sheft: NewSheft): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sheft);
    return this.http.post<RestSheft>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(sheft: ISheft): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sheft);
    return this.http
      .put<RestSheft>(`${this.resourceUrl}/${this.getSheftIdentifier(sheft)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(sheft: PartialUpdateSheft): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sheft);
    return this.http
      .patch<RestSheft>(`${this.resourceUrl}/${this.getSheftIdentifier(sheft)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<RestSheft>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSheft[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSheftIdentifier(sheft: Pick<ISheft, 'id'>): string {
    return sheft.id;
  }

  compareSheft(o1: Pick<ISheft, 'id'> | null, o2: Pick<ISheft, 'id'> | null): boolean {
    return o1 && o2 ? this.getSheftIdentifier(o1) === this.getSheftIdentifier(o2) : o1 === o2;
  }

  addSheftToCollectionIfMissing<Type extends Pick<ISheft, 'id'>>(
    sheftCollection: Type[],
    ...sheftsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shefts: Type[] = sheftsToCheck.filter(isPresent);
    if (shefts.length > 0) {
      const sheftCollectionIdentifiers = sheftCollection.map(sheftItem => this.getSheftIdentifier(sheftItem));
      const sheftsToAdd = shefts.filter(sheftItem => {
        const sheftIdentifier = this.getSheftIdentifier(sheftItem);
        if (sheftCollectionIdentifiers.includes(sheftIdentifier)) {
          return false;
        }
        sheftCollectionIdentifiers.push(sheftIdentifier);
        return true;
      });
      return [...sheftsToAdd, ...sheftCollection];
    }
    return sheftCollection;
  }

  protected convertDateFromClient<T extends ISheft | NewSheft | PartialUpdateSheft>(sheft: T): RestOf<T> {
    return {
      ...sheft,
      startTime: sheft.startTime?.format(DATE_FORMAT) ?? null,
      endTime: sheft.endTime?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSheft: RestSheft): ISheft {
    return {
      ...restSheft,
      startTime: restSheft.startTime ? dayjs(restSheft.startTime) : undefined,
      endTime: restSheft.endTime ? dayjs(restSheft.endTime) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSheft>): HttpResponse<ISheft> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSheft[]>): HttpResponse<ISheft[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
