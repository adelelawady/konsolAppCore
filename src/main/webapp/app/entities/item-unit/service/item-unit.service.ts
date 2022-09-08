import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IItemUnit, NewItemUnit } from '../item-unit.model';

export type PartialUpdateItemUnit = Partial<IItemUnit> & Pick<IItemUnit, 'id'>;

export type EntityResponseType = HttpResponse<IItemUnit>;
export type EntityArrayResponseType = HttpResponse<IItemUnit[]>;

@Injectable({ providedIn: 'root' })
export class ItemUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/item-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(itemUnit: NewItemUnit): Observable<EntityResponseType> {
    return this.http.post<IItemUnit>(this.resourceUrl, itemUnit, { observe: 'response' });
  }

  update(itemUnit: IItemUnit): Observable<EntityResponseType> {
    return this.http.put<IItemUnit>(`${this.resourceUrl}/${this.getItemUnitIdentifier(itemUnit)}`, itemUnit, { observe: 'response' });
  }

  partialUpdate(itemUnit: PartialUpdateItemUnit): Observable<EntityResponseType> {
    return this.http.patch<IItemUnit>(`${this.resourceUrl}/${this.getItemUnitIdentifier(itemUnit)}`, itemUnit, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IItemUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IItemUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getItemUnitIdentifier(itemUnit: Pick<IItemUnit, 'id'>): string {
    return itemUnit.id;
  }

  compareItemUnit(o1: Pick<IItemUnit, 'id'> | null, o2: Pick<IItemUnit, 'id'> | null): boolean {
    return o1 && o2 ? this.getItemUnitIdentifier(o1) === this.getItemUnitIdentifier(o2) : o1 === o2;
  }

  addItemUnitToCollectionIfMissing<Type extends Pick<IItemUnit, 'id'>>(
    itemUnitCollection: Type[],
    ...itemUnitsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const itemUnits: Type[] = itemUnitsToCheck.filter(isPresent);
    if (itemUnits.length > 0) {
      const itemUnitCollectionIdentifiers = itemUnitCollection.map(itemUnitItem => this.getItemUnitIdentifier(itemUnitItem)!);
      const itemUnitsToAdd = itemUnits.filter(itemUnitItem => {
        const itemUnitIdentifier = this.getItemUnitIdentifier(itemUnitItem);
        if (itemUnitCollectionIdentifiers.includes(itemUnitIdentifier)) {
          return false;
        }
        itemUnitCollectionIdentifiers.push(itemUnitIdentifier);
        return true;
      });
      return [...itemUnitsToAdd, ...itemUnitCollection];
    }
    return itemUnitCollection;
  }
}
