import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlaystationDeviceType, NewPlaystationDeviceType } from '../playstation-device-type.model';

export type PartialUpdatePlaystationDeviceType = Partial<IPlaystationDeviceType> & Pick<IPlaystationDeviceType, 'id'>;

export type EntityResponseType = HttpResponse<IPlaystationDeviceType>;
export type EntityArrayResponseType = HttpResponse<IPlaystationDeviceType[]>;

@Injectable({ providedIn: 'root' })
export class PlaystationDeviceTypeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/playstation-device-types');

  create(playstationDeviceType: NewPlaystationDeviceType): Observable<EntityResponseType> {
    return this.http.post<IPlaystationDeviceType>(this.resourceUrl, playstationDeviceType, { observe: 'response' });
  }

  update(playstationDeviceType: IPlaystationDeviceType): Observable<EntityResponseType> {
    return this.http.put<IPlaystationDeviceType>(
      `${this.resourceUrl}/${this.getPlaystationDeviceTypeIdentifier(playstationDeviceType)}`,
      playstationDeviceType,
      { observe: 'response' },
    );
  }

  partialUpdate(playstationDeviceType: PartialUpdatePlaystationDeviceType): Observable<EntityResponseType> {
    return this.http.patch<IPlaystationDeviceType>(
      `${this.resourceUrl}/${this.getPlaystationDeviceTypeIdentifier(playstationDeviceType)}`,
      playstationDeviceType,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPlaystationDeviceType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlaystationDeviceType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlaystationDeviceTypeIdentifier(playstationDeviceType: Pick<IPlaystationDeviceType, 'id'>): string {
    return playstationDeviceType.id;
  }

  comparePlaystationDeviceType(o1: Pick<IPlaystationDeviceType, 'id'> | null, o2: Pick<IPlaystationDeviceType, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlaystationDeviceTypeIdentifier(o1) === this.getPlaystationDeviceTypeIdentifier(o2) : o1 === o2;
  }

  addPlaystationDeviceTypeToCollectionIfMissing<Type extends Pick<IPlaystationDeviceType, 'id'>>(
    playstationDeviceTypeCollection: Type[],
    ...playstationDeviceTypesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playstationDeviceTypes: Type[] = playstationDeviceTypesToCheck.filter(isPresent);
    if (playstationDeviceTypes.length > 0) {
      const playstationDeviceTypeCollectionIdentifiers = playstationDeviceTypeCollection.map(playstationDeviceTypeItem =>
        this.getPlaystationDeviceTypeIdentifier(playstationDeviceTypeItem),
      );
      const playstationDeviceTypesToAdd = playstationDeviceTypes.filter(playstationDeviceTypeItem => {
        const playstationDeviceTypeIdentifier = this.getPlaystationDeviceTypeIdentifier(playstationDeviceTypeItem);
        if (playstationDeviceTypeCollectionIdentifiers.includes(playstationDeviceTypeIdentifier)) {
          return false;
        }
        playstationDeviceTypeCollectionIdentifiers.push(playstationDeviceTypeIdentifier);
        return true;
      });
      return [...playstationDeviceTypesToAdd, ...playstationDeviceTypeCollection];
    }
    return playstationDeviceTypeCollection;
  }
}
