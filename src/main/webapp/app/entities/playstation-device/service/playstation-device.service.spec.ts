import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPlaystationDevice } from '../playstation-device.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../playstation-device.test-samples';

import { PlaystationDeviceService } from './playstation-device.service';

const requireRestSample: IPlaystationDevice = {
  ...sampleWithRequiredData,
};

describe('PlaystationDevice Service', () => {
  let service: PlaystationDeviceService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlaystationDevice | IPlaystationDevice[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlaystationDeviceService);
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

    it('should create a PlaystationDevice', () => {
      const playstationDevice = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playstationDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlaystationDevice', () => {
      const playstationDevice = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playstationDevice).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlaystationDevice', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlaystationDevice', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlaystationDevice', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlaystationDeviceToCollectionIfMissing', () => {
      it('should add a PlaystationDevice to an empty array', () => {
        const playstationDevice: IPlaystationDevice = sampleWithRequiredData;
        expectedResult = service.addPlaystationDeviceToCollectionIfMissing([], playstationDevice);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playstationDevice);
      });

      it('should not add a PlaystationDevice to an array that contains it', () => {
        const playstationDevice: IPlaystationDevice = sampleWithRequiredData;
        const playstationDeviceCollection: IPlaystationDevice[] = [
          {
            ...playstationDevice,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlaystationDeviceToCollectionIfMissing(playstationDeviceCollection, playstationDevice);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlaystationDevice to an array that doesn't contain it", () => {
        const playstationDevice: IPlaystationDevice = sampleWithRequiredData;
        const playstationDeviceCollection: IPlaystationDevice[] = [sampleWithPartialData];
        expectedResult = service.addPlaystationDeviceToCollectionIfMissing(playstationDeviceCollection, playstationDevice);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playstationDevice);
      });

      it('should add only unique PlaystationDevice to an array', () => {
        const playstationDeviceArray: IPlaystationDevice[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playstationDeviceCollection: IPlaystationDevice[] = [sampleWithRequiredData];
        expectedResult = service.addPlaystationDeviceToCollectionIfMissing(playstationDeviceCollection, ...playstationDeviceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playstationDevice: IPlaystationDevice = sampleWithRequiredData;
        const playstationDevice2: IPlaystationDevice = sampleWithPartialData;
        expectedResult = service.addPlaystationDeviceToCollectionIfMissing([], playstationDevice, playstationDevice2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playstationDevice);
        expect(expectedResult).toContain(playstationDevice2);
      });

      it('should accept null and undefined values', () => {
        const playstationDevice: IPlaystationDevice = sampleWithRequiredData;
        expectedResult = service.addPlaystationDeviceToCollectionIfMissing([], null, playstationDevice, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playstationDevice);
      });

      it('should return initial array if no PlaystationDevice is added', () => {
        const playstationDeviceCollection: IPlaystationDevice[] = [sampleWithRequiredData];
        expectedResult = service.addPlaystationDeviceToCollectionIfMissing(playstationDeviceCollection, undefined, null);
        expect(expectedResult).toEqual(playstationDeviceCollection);
      });
    });

    describe('comparePlaystationDevice', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlaystationDevice(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.comparePlaystationDevice(entity1, entity2);
        const compareResult2 = service.comparePlaystationDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.comparePlaystationDevice(entity1, entity2);
        const compareResult2 = service.comparePlaystationDevice(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.comparePlaystationDevice(entity1, entity2);
        const compareResult2 = service.comparePlaystationDevice(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
