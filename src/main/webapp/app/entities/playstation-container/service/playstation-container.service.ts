import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlaystationContainer, NewPlaystationContainer } from '../playstation-container.model';

export type PartialUpdatePlaystationContainer = Partial<IPlaystationContainer> & Pick<IPlaystationContainer, 'id'>;

export type EntityResponseType = HttpResponse<IPlaystationContainer>;
export type EntityArrayResponseType = HttpResponse<IPlaystationContainer[]>;

@Injectable({ providedIn: 'root' })
export class PlaystationContainerService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/playstation-containers');

  create(playstationContainer: NewPlaystationContainer): Observable<EntityResponseType> {
    return this.http.post<IPlaystationContainer>(this.resourceUrl, playstationContainer, { observe: 'response' });
  }

  update(playstationContainer: IPlaystationContainer): Observable<EntityResponseType> {
    return this.http.put<IPlaystationContainer>(
      `${this.resourceUrl}/${this.getPlaystationContainerIdentifier(playstationContainer)}`,
      playstationContainer,
      { observe: 'response' }
    );
  }

  partialUpdate(playstationContainer: PartialUpdatePlaystationContainer): Observable<EntityResponseType> {
    return this.http.patch<IPlaystationContainer>(
      `${this.resourceUrl}/${this.getPlaystationContainerIdentifier(playstationContainer)}`,
      playstationContainer,
      { observe: 'response' }
    );
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPlaystationContainer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlaystationContainer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPlaystationContainerIdentifier(playstationContainer: Pick<IPlaystationContainer, 'id'>): string {
    return playstationContainer.id;
  }

  comparePlaystationContainer(o1: Pick<IPlaystationContainer, 'id'> | null, o2: Pick<IPlaystationContainer, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlaystationContainerIdentifier(o1) === this.getPlaystationContainerIdentifier(o2) : o1 === o2;
  }

  addPlaystationContainerToCollectionIfMissing<Type extends Pick<IPlaystationContainer, 'id'>>(
    playstationContainerCollection: Type[],
    ...playstationContainersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const playstationContainers: Type[] = playstationContainersToCheck.filter(isPresent);
    if (playstationContainers.length > 0) {
      const playstationContainerCollectionIdentifiers = playstationContainerCollection.map(playstationContainerItem =>
        this.getPlaystationContainerIdentifier(playstationContainerItem)
      );
      const playstationContainersToAdd = playstationContainers.filter(playstationContainerItem => {
        const playstationContainerIdentifier = this.getPlaystationContainerIdentifier(playstationContainerItem);
        if (playstationContainerCollectionIdentifiers.includes(playstationContainerIdentifier)) {
          return false;
        }
        playstationContainerCollectionIdentifiers.push(playstationContainerIdentifier);
        return true;
      });
      return [...playstationContainersToAdd, ...playstationContainerCollection];
    }
    return playstationContainerCollection;
  }
}
