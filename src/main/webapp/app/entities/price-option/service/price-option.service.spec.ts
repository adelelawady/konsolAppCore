import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPriceOption } from '../price-option.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../price-option.test-samples';

import { PriceOptionService } from './price-option.service';

const requireRestSample: IPriceOption = {
  ...sampleWithRequiredData,
};

describe('PriceOption Service', () => {
  let service: PriceOptionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPriceOption | IPriceOption[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PriceOptionService);
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

    it('should create a PriceOption', () => {
      const priceOption = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(priceOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PriceOption', () => {
      const priceOption = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(priceOption).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PriceOption', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PriceOption', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PriceOption', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPriceOptionToCollectionIfMissing', () => {
      it('should add a PriceOption to an empty array', () => {
        const priceOption: IPriceOption = sampleWithRequiredData;
        expectedResult = service.addPriceOptionToCollectionIfMissing([], priceOption);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priceOption);
      });

      it('should not add a PriceOption to an array that contains it', () => {
        const priceOption: IPriceOption = sampleWithRequiredData;
        const priceOptionCollection: IPriceOption[] = [
          {
            ...priceOption,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPriceOptionToCollectionIfMissing(priceOptionCollection, priceOption);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PriceOption to an array that doesn't contain it", () => {
        const priceOption: IPriceOption = sampleWithRequiredData;
        const priceOptionCollection: IPriceOption[] = [sampleWithPartialData];
        expectedResult = service.addPriceOptionToCollectionIfMissing(priceOptionCollection, priceOption);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priceOption);
      });

      it('should add only unique PriceOption to an array', () => {
        const priceOptionArray: IPriceOption[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const priceOptionCollection: IPriceOption[] = [sampleWithRequiredData];
        expectedResult = service.addPriceOptionToCollectionIfMissing(priceOptionCollection, ...priceOptionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const priceOption: IPriceOption = sampleWithRequiredData;
        const priceOption2: IPriceOption = sampleWithPartialData;
        expectedResult = service.addPriceOptionToCollectionIfMissing([], priceOption, priceOption2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(priceOption);
        expect(expectedResult).toContain(priceOption2);
      });

      it('should accept null and undefined values', () => {
        const priceOption: IPriceOption = sampleWithRequiredData;
        expectedResult = service.addPriceOptionToCollectionIfMissing([], null, priceOption, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(priceOption);
      });

      it('should return initial array if no PriceOption is added', () => {
        const priceOptionCollection: IPriceOption[] = [sampleWithRequiredData];
        expectedResult = service.addPriceOptionToCollectionIfMissing(priceOptionCollection, undefined, null);
        expect(expectedResult).toEqual(priceOptionCollection);
      });
    });

    describe('comparePriceOption', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePriceOption(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.comparePriceOption(entity1, entity2);
        const compareResult2 = service.comparePriceOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.comparePriceOption(entity1, entity2);
        const compareResult2 = service.comparePriceOption(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.comparePriceOption(entity1, entity2);
        const compareResult2 = service.comparePriceOption(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
