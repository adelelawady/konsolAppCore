import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISheft } from '../sheft.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../sheft.test-samples';

import { RestSheft, SheftService } from './sheft.service';

const requireRestSample: RestSheft = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.format(DATE_FORMAT),
  endTime: sampleWithRequiredData.endTime?.format(DATE_FORMAT),
};

describe('Sheft Service', () => {
  let service: SheftService;
  let httpMock: HttpTestingController;
  let expectedResult: ISheft | ISheft[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SheftService);
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

    it('should create a Sheft', () => {
      const sheft = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sheft).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sheft', () => {
      const sheft = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sheft).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sheft', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sheft', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sheft', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSheftToCollectionIfMissing', () => {
      it('should add a Sheft to an empty array', () => {
        const sheft: ISheft = sampleWithRequiredData;
        expectedResult = service.addSheftToCollectionIfMissing([], sheft);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sheft);
      });

      it('should not add a Sheft to an array that contains it', () => {
        const sheft: ISheft = sampleWithRequiredData;
        const sheftCollection: ISheft[] = [
          {
            ...sheft,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, sheft);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sheft to an array that doesn't contain it", () => {
        const sheft: ISheft = sampleWithRequiredData;
        const sheftCollection: ISheft[] = [sampleWithPartialData];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, sheft);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sheft);
      });

      it('should add only unique Sheft to an array', () => {
        const sheftArray: ISheft[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sheftCollection: ISheft[] = [sampleWithRequiredData];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, ...sheftArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sheft: ISheft = sampleWithRequiredData;
        const sheft2: ISheft = sampleWithPartialData;
        expectedResult = service.addSheftToCollectionIfMissing([], sheft, sheft2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sheft);
        expect(expectedResult).toContain(sheft2);
      });

      it('should accept null and undefined values', () => {
        const sheft: ISheft = sampleWithRequiredData;
        expectedResult = service.addSheftToCollectionIfMissing([], null, sheft, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sheft);
      });

      it('should return initial array if no Sheft is added', () => {
        const sheftCollection: ISheft[] = [sampleWithRequiredData];
        expectedResult = service.addSheftToCollectionIfMissing(sheftCollection, undefined, null);
        expect(expectedResult).toEqual(sheftCollection);
      });
    });

    describe('compareSheft', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSheft(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareSheft(entity1, entity2);
        const compareResult2 = service.compareSheft(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareSheft(entity1, entity2);
        const compareResult2 = service.compareSheft(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareSheft(entity1, entity2);
        const compareResult2 = service.compareSheft(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
