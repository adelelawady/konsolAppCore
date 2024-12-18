import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPlaystationContainer } from '../playstation-container.model';
import {
  sampleWithFullData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithRequiredData,
} from '../playstation-container.test-samples';

import { PlaystationContainerService } from './playstation-container.service';

const requireRestSample: IPlaystationContainer = {
  ...sampleWithRequiredData,
};

describe('PlaystationContainer Service', () => {
  let service: PlaystationContainerService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlaystationContainer | IPlaystationContainer[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlaystationContainerService);
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

    it('should create a PlaystationContainer', () => {
      const playstationContainer = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playstationContainer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlaystationContainer', () => {
      const playstationContainer = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playstationContainer).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlaystationContainer', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlaystationContainer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlaystationContainer', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlaystationContainerToCollectionIfMissing', () => {
      it('should add a PlaystationContainer to an empty array', () => {
        const playstationContainer: IPlaystationContainer = sampleWithRequiredData;
        expectedResult = service.addPlaystationContainerToCollectionIfMissing([], playstationContainer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playstationContainer);
      });

      it('should not add a PlaystationContainer to an array that contains it', () => {
        const playstationContainer: IPlaystationContainer = sampleWithRequiredData;
        const playstationContainerCollection: IPlaystationContainer[] = [
          {
            ...playstationContainer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlaystationContainerToCollectionIfMissing(playstationContainerCollection, playstationContainer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlaystationContainer to an array that doesn't contain it", () => {
        const playstationContainer: IPlaystationContainer = sampleWithRequiredData;
        const playstationContainerCollection: IPlaystationContainer[] = [sampleWithPartialData];
        expectedResult = service.addPlaystationContainerToCollectionIfMissing(playstationContainerCollection, playstationContainer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playstationContainer);
      });

      it('should add only unique PlaystationContainer to an array', () => {
        const playstationContainerArray: IPlaystationContainer[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playstationContainerCollection: IPlaystationContainer[] = [sampleWithRequiredData];
        expectedResult = service.addPlaystationContainerToCollectionIfMissing(playstationContainerCollection, ...playstationContainerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playstationContainer: IPlaystationContainer = sampleWithRequiredData;
        const playstationContainer2: IPlaystationContainer = sampleWithPartialData;
        expectedResult = service.addPlaystationContainerToCollectionIfMissing([], playstationContainer, playstationContainer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playstationContainer);
        expect(expectedResult).toContain(playstationContainer2);
      });

      it('should accept null and undefined values', () => {
        const playstationContainer: IPlaystationContainer = sampleWithRequiredData;
        expectedResult = service.addPlaystationContainerToCollectionIfMissing([], null, playstationContainer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playstationContainer);
      });

      it('should return initial array if no PlaystationContainer is added', () => {
        const playstationContainerCollection: IPlaystationContainer[] = [sampleWithRequiredData];
        expectedResult = service.addPlaystationContainerToCollectionIfMissing(playstationContainerCollection, undefined, null);
        expect(expectedResult).toEqual(playstationContainerCollection);
      });
    });

    describe('comparePlaystationContainer', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlaystationContainer(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.comparePlaystationContainer(entity1, entity2);
        const compareResult2 = service.comparePlaystationContainer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.comparePlaystationContainer(entity1, entity2);
        const compareResult2 = service.comparePlaystationContainer(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.comparePlaystationContainer(entity1, entity2);
        const compareResult2 = service.comparePlaystationContainer(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
