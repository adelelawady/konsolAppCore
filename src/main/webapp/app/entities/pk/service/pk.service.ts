import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPk, NewPk } from '../pk.model';

export type PartialUpdatePk = Partial<IPk> & Pick<IPk, 'id'>;

export type EntityResponseType = HttpResponse<IPk>;
export type EntityArrayResponseType = HttpResponse<IPk[]>;

@Injectable({ providedIn: 'root' })
export class PkService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pks');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pk: NewPk): Observable<EntityResponseType> {
    return this.http.post<IPk>(this.resourceUrl, pk, { observe: 'response' });
  }

  update(pk: IPk): Observable<EntityResponseType> {
    return this.http.put<IPk>(`${this.resourceUrl}/${this.getPkIdentifier(pk)}`, pk, { observe: 'response' });
  }

  partialUpdate(pk: PartialUpdatePk): Observable<EntityResponseType> {
    return this.http.patch<IPk>(`${this.resourceUrl}/${this.getPkIdentifier(pk)}`, pk, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IPk>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPk[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPkIdentifier(pk: Pick<IPk, 'id'>): string {
    return pk.id;
  }

  comparePk(o1: Pick<IPk, 'id'> | null, o2: Pick<IPk, 'id'> | null): boolean {
    return o1 && o2 ? this.getPkIdentifier(o1) === this.getPkIdentifier(o2) : o1 === o2;
  }

  addPkToCollectionIfMissing<Type extends Pick<IPk, 'id'>>(pkCollection: Type[], ...pksToCheck: (Type | null | undefined)[]): Type[] {
    const pks: Type[] = pksToCheck.filter(isPresent);
    if (pks.length > 0) {
      const pkCollectionIdentifiers = pkCollection.map(pkItem => this.getPkIdentifier(pkItem)!);
      const pksToAdd = pks.filter(pkItem => {
        const pkIdentifier = this.getPkIdentifier(pkItem);
        if (pkCollectionIdentifiers.includes(pkIdentifier)) {
          return false;
        }
        pkCollectionIdentifiers.push(pkIdentifier);
        return true;
      });
      return [...pksToAdd, ...pkCollection];
    }
    return pkCollection;
  }
}
