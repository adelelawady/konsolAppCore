import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMoney } from '../money.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../money.test-samples';

import { MoneyService } from './money.service';

const requireRestSample: IMoney = {
  ...sampleWithRequiredData,
};

describe('Money Service', () => {
  let service: MoneyService;
  let httpMock: HttpTestingController;
  let expectedResult: IMoney | IMoney[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MoneyService);
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

    it('should create a Money', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const money = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(money).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Money', () => {
      const money = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(money).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Money', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Money', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Money', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMoneyToCollectionIfMissing', () => {
      it('should add a Money to an empty array', () => {
        const money: IMoney = sampleWithRequiredData;
        expectedResult = service.addMoneyToCollectionIfMissing([], money);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(money);
      });

      it('should not add a Money to an array that contains it', () => {
        const money: IMoney = sampleWithRequiredData;
        const moneyCollection: IMoney[] = [
          {
            ...money,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMoneyToCollectionIfMissing(moneyCollection, money);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Money to an array that doesn't contain it", () => {
        const money: IMoney = sampleWithRequiredData;
        const moneyCollection: IMoney[] = [sampleWithPartialData];
        expectedResult = service.addMoneyToCollectionIfMissing(moneyCollection, money);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(money);
      });

      it('should add only unique Money to an array', () => {
        const moneyArray: IMoney[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const moneyCollection: IMoney[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyToCollectionIfMissing(moneyCollection, ...moneyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const money: IMoney = sampleWithRequiredData;
        const money2: IMoney = sampleWithPartialData;
        expectedResult = service.addMoneyToCollectionIfMissing([], money, money2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(money);
        expect(expectedResult).toContain(money2);
      });

      it('should accept null and undefined values', () => {
        const money: IMoney = sampleWithRequiredData;
        expectedResult = service.addMoneyToCollectionIfMissing([], null, money, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(money);
      });

      it('should return initial array if no Money is added', () => {
        const moneyCollection: IMoney[] = [sampleWithRequiredData];
        expectedResult = service.addMoneyToCollectionIfMissing(moneyCollection, undefined, null);
        expect(expectedResult).toEqual(moneyCollection);
      });
    });

    describe('compareMoney', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMoney(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareMoney(entity1, entity2);
        const compareResult2 = service.compareMoney(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareMoney(entity1, entity2);
        const compareResult2 = service.compareMoney(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareMoney(entity1, entity2);
        const compareResult2 = service.compareMoney(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
