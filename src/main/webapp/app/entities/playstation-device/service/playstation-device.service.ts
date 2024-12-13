import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlaystationDevice, NewPlaystationDevice } from '../playstation-device.model';

export type PartialUpdatePlaystationDevice = Partial<IPlaystationDevice> & Pick<IPlaystationDevice, 'id'>;

export type EntityResponseType = HttpResponse<IPlaystationDevice>;
export type EntityArrayResponseType = HttpResponse<IPlaystationDevice[]>;

@Injectable({ providedIn: 'root' })
export class PlaystationDeviceService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/playstation-devices');

  create(playstationDevice: NewPlaystationDevice): Observable<EntityResponseType> {
    return this.http.post<IPlaystationDevice>(this.resourceUrl, playstationDevice, { observe: 'response' });
  }

  update(playstationDevice: IPlaystationDevice): Observable<EntityResponseType> {
    return this.http.put<IPlaystationDevice>(
      `${this.resourceUrl}/${this.getPlaystationDeviceIdentifier(playstationDevice)}`,
      playstationDevice,
      { observe: 'response' },
    );
  }

  partialUpdate(playstationDevice: PartialUpdatePlaystationDevice): Observable<EntityResponseType> {
    return this.http.patch<IPlaystationDevice>(
      `${this.resourceUrl}/${this.getPlaystationDeviceIdentifier(playstationDevice)}`,
      playstationDevice,
      { observe: 'response' },
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPlaystationDevice>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlaystationDevice[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlaystationDeviceIdentifier(playstationDevice: Pick<IPlaystationDevice, 'id'>): string {
    return playstationDevice.id;
  }

  comparePlaystationDevice(o1: Pick<IPlaystationDevice, 'id'> | null, o2: Pick<IPlaystationDevice, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlaystationDeviceIdentifier(o1) === this.getPlaystationDeviceIdentifier(o2) : o1 === o2;
  }

  addPlaystationDeviceToCollectionIfMissing<Type extends Pick<IPlaystationDevice, 'id'>>(
    playstationDeviceCollection: Type[],
    ...playstationDevicesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playstationDevices: Type[] = playstationDevicesToCheck.filter(isPresent);
    if (playstationDevices.length > 0) {
      const playstationDeviceCollectionIdentifiers = playstationDeviceCollection.map(playstationDeviceItem =>
        this.getPlaystationDeviceIdentifier(playstationDeviceItem),
      );
      const playstationDevicesToAdd = playstationDevices.filter(playstationDeviceItem => {
        const playstationDeviceIdentifier = this.getPlaystationDeviceIdentifier(playstationDeviceItem);
        if (playstationDeviceCollectionIdentifiers.includes(playstationDeviceIdentifier)) {
          return false;
        }
        playstationDeviceCollectionIdentifiers.push(playstationDeviceIdentifier);
        return true;
      });
      return [...playstationDevicesToAdd, ...playstationDeviceCollection];
    }
    return playstationDeviceCollection;
  }
}
