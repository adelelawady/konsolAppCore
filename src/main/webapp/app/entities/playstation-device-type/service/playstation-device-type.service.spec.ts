import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPlaystationDeviceType } from '../playstation-device-type.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../playstation-device-type.test-samples';

import { PlaystationDeviceTypeService } from './playstation-device-type.service';

const requireRestSample: IPlaystationDeviceType = {
  ...sampleWithRequiredData,
};

describe('PlaystationDeviceType Service', () => {
  let service: PlaystationDeviceTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlaystationDeviceType | IPlaystationDeviceType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlaystationDeviceTypeService);
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

    it('should create a PlaystationDeviceType', () => {
      const playstationDeviceType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playstationDeviceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlaystationDeviceType', () => {
      const playstationDeviceType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playstationDeviceType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlaystationDeviceType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlaystationDeviceType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlaystationDeviceType', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlaystationDeviceTypeToCollectionIfMissing', () => {
      it('should add a PlaystationDeviceType to an empty array', () => {
        const playstationDeviceType: IPlaystationDeviceType = sampleWithRequiredData;
        expectedResult = service.addPlaystationDeviceTypeToCollectionIfMissing([], playstationDeviceType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playstationDeviceType);
      });

      it('should not add a PlaystationDeviceType to an array that contains it', () => {
        const playstationDeviceType: IPlaystationDeviceType = sampleWithRequiredData;
        const playstationDeviceTypeCollection: IPlaystationDeviceType[] = [
          {
            ...playstationDeviceType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlaystationDeviceTypeToCollectionIfMissing(playstationDeviceTypeCollection, playstationDeviceType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlaystationDeviceType to an array that doesn't contain it", () => {
        const playstationDeviceType: IPlaystationDeviceType = sampleWithRequiredData;
        const playstationDeviceTypeCollection: IPlaystationDeviceType[] = [sampleWithPartialData];
        expectedResult = service.addPlaystationDeviceTypeToCollectionIfMissing(playstationDeviceTypeCollection, playstationDeviceType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playstationDeviceType);
      });

      it('should add only unique PlaystationDeviceType to an array', () => {
        const playstationDeviceTypeArray: IPlaystationDeviceType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playstationDeviceTypeCollection: IPlaystationDeviceType[] = [sampleWithRequiredData];
        expectedResult = service.addPlaystationDeviceTypeToCollectionIfMissing(
          playstationDeviceTypeCollection,
          ...playstationDeviceTypeArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playstationDeviceType: IPlaystationDeviceType = sampleWithRequiredData;
        const playstationDeviceType2: IPlaystationDeviceType = sampleWithPartialData;
        expectedResult = service.addPlaystationDeviceTypeToCollectionIfMissing([], playstationDeviceType, playstationDeviceType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playstationDeviceType);
        expect(expectedResult).toContain(playstationDeviceType2);
      });

      it('should accept null and undefined values', () => {
        const playstationDeviceType: IPlaystationDeviceType = sampleWithRequiredData;
        expectedResult = service.addPlaystationDeviceTypeToCollectionIfMissing([], null, playstationDeviceType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playstationDeviceType);
      });

      it('should return initial array if no PlaystationDeviceType is added', () => {
        const playstationDeviceTypeCollection: IPlaystationDeviceType[] = [sampleWithRequiredData];
        expectedResult = service.addPlaystationDeviceTypeToCollectionIfMissing(playstationDeviceTypeCollection, undefined, null);
        expect(expectedResult).toEqual(playstationDeviceTypeCollection);
      });
    });

    describe('comparePlaystationDeviceType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlaystationDeviceType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.comparePlaystationDeviceType(entity1, entity2);
        const compareResult2 = service.comparePlaystationDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.comparePlaystationDeviceType(entity1, entity2);
        const compareResult2 = service.comparePlaystationDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.comparePlaystationDeviceType(entity1, entity2);
        const compareResult2 = service.comparePlaystationDeviceType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
