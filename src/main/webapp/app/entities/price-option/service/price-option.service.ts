import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPriceOption, NewPriceOption } from '../price-option.model';

export type PartialUpdatePriceOption = Partial<IPriceOption> & Pick<IPriceOption, 'id'>;

export type EntityResponseType = HttpResponse<IPriceOption>;
export type EntityArrayResponseType = HttpResponse<IPriceOption[]>;

@Injectable({ providedIn: 'root' })
export class PriceOptionService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/price-options');

  create(priceOption: NewPriceOption): Observable<EntityResponseType> {
    return this.http.post<IPriceOption>(this.resourceUrl, priceOption, { observe: 'response' });
  }

  update(priceOption: IPriceOption): Observable<EntityResponseType> {
    return this.http.put<IPriceOption>(`${this.resourceUrl}/${this.getPriceOptionIdentifier(priceOption)}`, priceOption, {
      observe: 'response',
    });
  }

  partialUpdate(priceOption: PartialUpdatePriceOption): Observable<EntityResponseType> {
    return this.http.patch<IPriceOption>(`${this.resourceUrl}/${this.getPriceOptionIdentifier(priceOption)}`, priceOption, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPriceOption>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPriceOption[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPriceOptionIdentifier(priceOption: Pick<IPriceOption, 'id'>): string {
    return priceOption.id;
  }

  comparePriceOption(o1: Pick<IPriceOption, 'id'> | null, o2: Pick<IPriceOption, 'id'> | null): boolean {
    return o1 && o2 ? this.getPriceOptionIdentifier(o1) === this.getPriceOptionIdentifier(o2) : o1 === o2;
  }

  addPriceOptionToCollectionIfMissing<Type extends Pick<IPriceOption, 'id'>>(
    priceOptionCollection: Type[],
    ...priceOptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const priceOptions: Type[] = priceOptionsToCheck.filter(isPresent);
    if (priceOptions.length > 0) {
      const priceOptionCollectionIdentifiers = priceOptionCollection.map(priceOptionItem => this.getPriceOptionIdentifier(priceOptionItem));
      const priceOptionsToAdd = priceOptions.filter(priceOptionItem => {
        const priceOptionIdentifier = this.getPriceOptionIdentifier(priceOptionItem);
        if (priceOptionCollectionIdentifiers.includes(priceOptionIdentifier)) {
          return false;
        }
        priceOptionCollectionIdentifiers.push(priceOptionIdentifier);
        return true;
      });
      return [...priceOptionsToAdd, ...priceOptionCollection];
    }
    return priceOptionCollection;
  }
}
