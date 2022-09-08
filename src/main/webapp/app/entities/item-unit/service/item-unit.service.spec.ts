import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IItemUnit } from '../item-unit.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../item-unit.test-samples';

import { ItemUnitService } from './item-unit.service';

const requireRestSample: IItemUnit = {
  ...sampleWithRequiredData,
};

describe('ItemUnit Service', () => {
  let service: ItemUnitService;
  let httpMock: HttpTestingController;
  let expectedResult: IItemUnit | IItemUnit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ItemUnitService);
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

    it('should create a ItemUnit', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const itemUnit = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(itemUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ItemUnit', () => {
      const itemUnit = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(itemUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ItemUnit', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ItemUnit', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ItemUnit', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addItemUnitToCollectionIfMissing', () => {
      it('should add a ItemUnit to an empty array', () => {
        const itemUnit: IItemUnit = sampleWithRequiredData;
        expectedResult = service.addItemUnitToCollectionIfMissing([], itemUnit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemUnit);
      });

      it('should not add a ItemUnit to an array that contains it', () => {
        const itemUnit: IItemUnit = sampleWithRequiredData;
        const itemUnitCollection: IItemUnit[] = [
          {
            ...itemUnit,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addItemUnitToCollectionIfMissing(itemUnitCollection, itemUnit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ItemUnit to an array that doesn't contain it", () => {
        const itemUnit: IItemUnit = sampleWithRequiredData;
        const itemUnitCollection: IItemUnit[] = [sampleWithPartialData];
        expectedResult = service.addItemUnitToCollectionIfMissing(itemUnitCollection, itemUnit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemUnit);
      });

      it('should add only unique ItemUnit to an array', () => {
        const itemUnitArray: IItemUnit[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const itemUnitCollection: IItemUnit[] = [sampleWithRequiredData];
        expectedResult = service.addItemUnitToCollectionIfMissing(itemUnitCollection, ...itemUnitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const itemUnit: IItemUnit = sampleWithRequiredData;
        const itemUnit2: IItemUnit = sampleWithPartialData;
        expectedResult = service.addItemUnitToCollectionIfMissing([], itemUnit, itemUnit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(itemUnit);
        expect(expectedResult).toContain(itemUnit2);
      });

      it('should accept null and undefined values', () => {
        const itemUnit: IItemUnit = sampleWithRequiredData;
        expectedResult = service.addItemUnitToCollectionIfMissing([], null, itemUnit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(itemUnit);
      });

      it('should return initial array if no ItemUnit is added', () => {
        const itemUnitCollection: IItemUnit[] = [sampleWithRequiredData];
        expectedResult = service.addItemUnitToCollectionIfMissing(itemUnitCollection, undefined, null);
        expect(expectedResult).toEqual(itemUnitCollection);
      });
    });

    describe('compareItemUnit', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareItemUnit(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareItemUnit(entity1, entity2);
        const compareResult2 = service.compareItemUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareItemUnit(entity1, entity2);
        const compareResult2 = service.compareItemUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareItemUnit(entity1, entity2);
        const compareResult2 = service.compareItemUnit(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
