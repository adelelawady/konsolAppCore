import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAccountUser } from '../account-user.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../account-user.test-samples';

import { AccountUserService } from './account-user.service';

const requireRestSample: IAccountUser = {
  ...sampleWithRequiredData,
};

describe('AccountUser Service', () => {
  let service: AccountUserService;
  let httpMock: HttpTestingController;
  let expectedResult: IAccountUser | IAccountUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AccountUserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find('ABC').subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AccountUser', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const accountUser = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(accountUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AccountUser', () => {
      const accountUser = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(accountUser).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AccountUser', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AccountUser', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AccountUser', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAccountUserToCollectionIfMissing', () => {
      it('should add a AccountUser to an empty array', () => {
        const accountUser: IAccountUser = sampleWithRequiredData;
        expectedResult = service.addAccountUserToCollectionIfMissing([], accountUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountUser);
      });

      it('should not add a AccountUser to an array that contains it', () => {
        const accountUser: IAccountUser = sampleWithRequiredData;
        const accountUserCollection: IAccountUser[] = [
          {
            ...accountUser,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAccountUserToCollectionIfMissing(accountUserCollection, accountUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AccountUser to an array that doesn't contain it", () => {
        const accountUser: IAccountUser = sampleWithRequiredData;
        const accountUserCollection: IAccountUser[] = [sampleWithPartialData];
        expectedResult = service.addAccountUserToCollectionIfMissing(accountUserCollection, accountUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountUser);
      });

      it('should add only unique AccountUser to an array', () => {
        const accountUserArray: IAccountUser[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const accountUserCollection: IAccountUser[] = [sampleWithRequiredData];
        expectedResult = service.addAccountUserToCollectionIfMissing(accountUserCollection, ...accountUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const accountUser: IAccountUser = sampleWithRequiredData;
        const accountUser2: IAccountUser = sampleWithPartialData;
        expectedResult = service.addAccountUserToCollectionIfMissing([], accountUser, accountUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(accountUser);
        expect(expectedResult).toContain(accountUser2);
      });

      it('should accept null and undefined values', () => {
        const accountUser: IAccountUser = sampleWithRequiredData;
        expectedResult = service.addAccountUserToCollectionIfMissing([], null, accountUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(accountUser);
      });

      it('should return initial array if no AccountUser is added', () => {
        const accountUserCollection: IAccountUser[] = [sampleWithRequiredData];
        expectedResult = service.addAccountUserToCollectionIfMissing(accountUserCollection, undefined, null);
        expect(expectedResult).toEqual(accountUserCollection);
      });
    });

    describe('compareAccountUser', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAccountUser(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareAccountUser(entity1, entity2);
        const compareResult2 = service.compareAccountUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareAccountUser(entity1, entity2);
        const compareResult2 = service.compareAccountUser(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareAccountUser(entity1, entity2);
        const compareResult2 = service.compareAccountUser(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
