import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMoney, NewMoney } from '../money.model';

export type PartialUpdateMoney = Partial<IMoney> & Pick<IMoney, 'id'>;

export type EntityResponseType = HttpResponse<IMoney>;
export type EntityArrayResponseType = HttpResponse<IMoney[]>;

@Injectable({ providedIn: 'root' })
export class MoneyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/monies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(money: NewMoney): Observable<EntityResponseType> {
    return this.http.post<IMoney>(this.resourceUrl, money, { observe: 'response' });
  }

  update(money: IMoney): Observable<EntityResponseType> {
    return this.http.put<IMoney>(`${this.resourceUrl}/${this.getMoneyIdentifier(money)}`, money, { observe: 'response' });
  }

  partialUpdate(money: PartialUpdateMoney): Observable<EntityResponseType> {
    return this.http.patch<IMoney>(`${this.resourceUrl}/${this.getMoneyIdentifier(money)}`, money, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IMoney>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMoney[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMoneyIdentifier(money: Pick<IMoney, 'id'>): string {
    return money.id;
  }

  compareMoney(o1: Pick<IMoney, 'id'> | null, o2: Pick<IMoney, 'id'> | null): boolean {
    return o1 && o2 ? this.getMoneyIdentifier(o1) === this.getMoneyIdentifier(o2) : o1 === o2;
  }

  addMoneyToCollectionIfMissing<Type extends Pick<IMoney, 'id'>>(
    moneyCollection: Type[],
    ...moniesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const monies: Type[] = moniesToCheck.filter(isPresent);
    if (monies.length > 0) {
      const moneyCollectionIdentifiers = moneyCollection.map(moneyItem => this.getMoneyIdentifier(moneyItem)!);
      const moniesToAdd = monies.filter(moneyItem => {
        const moneyIdentifier = this.getMoneyIdentifier(moneyItem);
        if (moneyCollectionIdentifiers.includes(moneyIdentifier)) {
          return false;
        }
        moneyCollectionIdentifiers.push(moneyIdentifier);
        return true;
      });
      return [...moniesToAdd, ...moneyCollection];
    }
    return moneyCollection;
  }
}
