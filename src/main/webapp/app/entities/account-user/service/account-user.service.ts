import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccountUser, NewAccountUser } from '../account-user.model';

export type PartialUpdateAccountUser = Partial<IAccountUser> & Pick<IAccountUser, 'id'>;

export type EntityResponseType = HttpResponse<IAccountUser>;
export type EntityArrayResponseType = HttpResponse<IAccountUser[]>;

@Injectable({ providedIn: 'root' })
export class AccountUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(accountUser: NewAccountUser): Observable<EntityResponseType> {
    return this.http.post<IAccountUser>(this.resourceUrl, accountUser, { observe: 'response' });
  }

  update(accountUser: IAccountUser): Observable<EntityResponseType> {
    return this.http.put<IAccountUser>(`${this.resourceUrl}/${this.getAccountUserIdentifier(accountUser)}`, accountUser, {
      observe: 'response',
    });
  }

  partialUpdate(accountUser: PartialUpdateAccountUser): Observable<EntityResponseType> {
    return this.http.patch<IAccountUser>(`${this.resourceUrl}/${this.getAccountUserIdentifier(accountUser)}`, accountUser, {
      observe: 'response',
    });
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http.get<IAccountUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAccountUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccountUserIdentifier(accountUser: Pick<IAccountUser, 'id'>): string {
    return accountUser.id;
  }

  compareAccountUser(o1: Pick<IAccountUser, 'id'> | null, o2: Pick<IAccountUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountUserIdentifier(o1) === this.getAccountUserIdentifier(o2) : o1 === o2;
  }

  addAccountUserToCollectionIfMissing<Type extends Pick<IAccountUser, 'id'>>(
    accountUserCollection: Type[],
    ...accountUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountUsers: Type[] = accountUsersToCheck.filter(isPresent);
    if (accountUsers.length > 0) {
      const accountUserCollectionIdentifiers = accountUserCollection.map(
        accountUserItem => this.getAccountUserIdentifier(accountUserItem)!
      );
      const accountUsersToAdd = accountUsers.filter(accountUserItem => {
        const accountUserIdentifier = this.getAccountUserIdentifier(accountUserItem);
        if (accountUserCollectionIdentifiers.includes(accountUserIdentifier)) {
          return false;
        }
        accountUserCollectionIdentifiers.push(accountUserIdentifier);
        return true;
      });
      return [...accountUsersToAdd, ...accountUserCollection];
    }
    return accountUserCollection;
  }
}
