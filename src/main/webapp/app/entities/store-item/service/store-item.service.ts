import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStoreItem, NewStoreItem } from '../store-item.model';

export type PartialUpdateStoreItem = Partial<IStoreItem> & Pick<IStoreItem, 'id'>;

export type EntityResponseType = HttpResponse<IStoreItem>;
export type EntityArrayResponseType = HttpResponse<IStoreItem[]>;

@Injectable({ providedIn: 'root' })
export class StoreItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/store-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(storeItem: NewStoreItem): Observable<EntityResponseType> {
    return this.http.post<IStoreItem>(this.resourceUrl, storeItem, { observe: 'response' });
  }

  update(storeItem: IStoreItem): Observable<EntityResponseType> {
    return this.http.put<IStoreItem>(`${this.resourceUrl}/${this.getStoreItemIdentifier(storeItem)}`, storeItem, { observe: 'response' });
  }

  partialUpdate(storeItem: PartialUpdateStoreItem): Observable<EntityResponseType> {
    return this.http.patch<IStoreItem>(`${this.resourceUrl}/${this.getStoreItemIdentifier(storeItem)}`, storeItem, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IStoreItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStoreItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStoreItemIdentifier(storeItem: Pick<IStoreItem, 'id'>): string {
    return storeItem.id;
  }

  compareStoreItem(o1: Pick<IStoreItem, 'id'> | null, o2: Pick<IStoreItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getStoreItemIdentifier(o1) === this.getStoreItemIdentifier(o2) : o1 === o2;
  }

  addStoreItemToCollectionIfMissing<Type extends Pick<IStoreItem, 'id'>>(
    storeItemCollection: Type[],
    ...storeItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const storeItems: Type[] = storeItemsToCheck.filter(isPresent);
    if (storeItems.length > 0) {
      const storeItemCollectionIdentifiers = storeItemCollection.map(storeItemItem => this.getStoreItemIdentifier(storeItemItem)!);
      const storeItemsToAdd = storeItems.filter(storeItemItem => {
        const storeItemIdentifier = this.getStoreItemIdentifier(storeItemItem);
        if (storeItemCollectionIdentifiers.includes(storeItemIdentifier)) {
          return false;
        }
        storeItemCollectionIdentifiers.push(storeItemIdentifier);
        return true;
      });
      return [...storeItemsToAdd, ...storeItemCollection];
    }
    return storeItemCollection;
  }
}
