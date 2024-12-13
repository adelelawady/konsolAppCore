import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICafeTable, NewCafeTable } from '../cafe-table.model';

export type PartialUpdateCafeTable = Partial<ICafeTable> & Pick<ICafeTable, 'id'>;

export type EntityResponseType = HttpResponse<ICafeTable>;
export type EntityArrayResponseType = HttpResponse<ICafeTable[]>;

@Injectable({ providedIn: 'root' })
export class CafeTableService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cafe-tables');

  create(cafeTable: NewCafeTable): Observable<EntityResponseType> {
    return this.http.post<ICafeTable>(this.resourceUrl, cafeTable, { observe: 'response' });
  }

  update(cafeTable: ICafeTable): Observable<EntityResponseType> {
    return this.http.put<ICafeTable>(`${this.resourceUrl}/${this.getCafeTableIdentifier(cafeTable)}`, cafeTable, { observe: 'response' });
  }

  partialUpdate(cafeTable: PartialUpdateCafeTable): Observable<EntityResponseType> {
    return this.http.patch<ICafeTable>(`${this.resourceUrl}/${this.getCafeTableIdentifier(cafeTable)}`, cafeTable, { observe: 'response' });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<ICafeTable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICafeTable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCafeTableIdentifier(cafeTable: Pick<ICafeTable, 'id'>): string {
    return cafeTable.id;
  }

  compareCafeTable(o1: Pick<ICafeTable, 'id'> | null, o2: Pick<ICafeTable, 'id'> | null): boolean {
    return o1 && o2 ? this.getCafeTableIdentifier(o1) === this.getCafeTableIdentifier(o2) : o1 === o2;
  }

  addCafeTableToCollectionIfMissing<Type extends Pick<ICafeTable, 'id'>>(
    cafeTableCollection: Type[],
    ...cafeTablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const cafeTables: Type[] = cafeTablesToCheck.filter(isPresent);
    if (cafeTables.length > 0) {
      const cafeTableCollectionIdentifiers = cafeTableCollection.map(cafeTableItem => this.getCafeTableIdentifier(cafeTableItem));
      const cafeTablesToAdd = cafeTables.filter(cafeTableItem => {
        const cafeTableIdentifier = this.getCafeTableIdentifier(cafeTableItem);
        if (cafeTableCollectionIdentifiers.includes(cafeTableIdentifier)) {
          return false;
        }
        cafeTableCollectionIdentifiers.push(cafeTableIdentifier);
        return true;
      });
      return [...cafeTablesToAdd, ...cafeTableCollection];
    }
    return cafeTableCollection;
  }
}
