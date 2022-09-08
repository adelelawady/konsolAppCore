import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPk } from '../pk.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../pk.test-samples';

import { PkService } from './pk.service';

const requireRestSample: IPk = {
  ...sampleWithRequiredData,
};

describe('Pk Service', () => {
  let service: PkService;
  let httpMock: HttpTestingController;
  let expectedResult: IPk | IPk[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PkService);
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

    it('should create a Pk', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const pk = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(pk).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Pk', () => {
      const pk = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(pk).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Pk', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Pk', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Pk', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPkToCollectionIfMissing', () => {
      it('should add a Pk to an empty array', () => {
        const pk: IPk = sampleWithRequiredData;
        expectedResult = service.addPkToCollectionIfMissing([], pk);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pk);
      });

      it('should not add a Pk to an array that contains it', () => {
        const pk: IPk = sampleWithRequiredData;
        const pkCollection: IPk[] = [
          {
            ...pk,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPkToCollectionIfMissing(pkCollection, pk);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Pk to an array that doesn't contain it", () => {
        const pk: IPk = sampleWithRequiredData;
        const pkCollection: IPk[] = [sampleWithPartialData];
        expectedResult = service.addPkToCollectionIfMissing(pkCollection, pk);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pk);
      });

      it('should add only unique Pk to an array', () => {
        const pkArray: IPk[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const pkCollection: IPk[] = [sampleWithRequiredData];
        expectedResult = service.addPkToCollectionIfMissing(pkCollection, ...pkArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pk: IPk = sampleWithRequiredData;
        const pk2: IPk = sampleWithPartialData;
        expectedResult = service.addPkToCollectionIfMissing([], pk, pk2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pk);
        expect(expectedResult).toContain(pk2);
      });

      it('should accept null and undefined values', () => {
        const pk: IPk = sampleWithRequiredData;
        expectedResult = service.addPkToCollectionIfMissing([], null, pk, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pk);
      });

      it('should return initial array if no Pk is added', () => {
        const pkCollection: IPk[] = [sampleWithRequiredData];
        expectedResult = service.addPkToCollectionIfMissing(pkCollection, undefined, null);
        expect(expectedResult).toEqual(pkCollection);
      });
    });

    describe('comparePk', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePk(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.comparePk(entity1, entity2);
        const compareResult2 = service.comparePk(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.comparePk(entity1, entity2);
        const compareResult2 = service.comparePk(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.comparePk(entity1, entity2);
        const compareResult2 = service.comparePk(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
