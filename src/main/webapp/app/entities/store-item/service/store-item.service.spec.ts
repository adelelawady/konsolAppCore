import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStoreItem } from '../store-item.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../store-item.test-samples';

import { StoreItemService } from './store-item.service';

const requireRestSample: IStoreItem = {
  ...sampleWithRequiredData,
};

describe('StoreItem Service', () => {
  let service: StoreItemService;
  let httpMock: HttpTestingController;
  let expectedResult: IStoreItem | IStoreItem[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StoreItemService);
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

    it('should create a StoreItem', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const storeItem = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(storeItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StoreItem', () => {
      const storeItem = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(storeItem).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StoreItem', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StoreItem', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StoreItem', () => {
      const expected = true;

      service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStoreItemToCollectionIfMissing', () => {
      it('should add a StoreItem to an empty array', () => {
        const storeItem: IStoreItem = sampleWithRequiredData;
        expectedResult = service.addStoreItemToCollectionIfMissing([], storeItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(storeItem);
      });

      it('should not add a StoreItem to an array that contains it', () => {
        const storeItem: IStoreItem = sampleWithRequiredData;
        const storeItemCollection: IStoreItem[] = [
          {
            ...storeItem,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStoreItemToCollectionIfMissing(storeItemCollection, storeItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StoreItem to an array that doesn't contain it", () => {
        const storeItem: IStoreItem = sampleWithRequiredData;
        const storeItemCollection: IStoreItem[] = [sampleWithPartialData];
        expectedResult = service.addStoreItemToCollectionIfMissing(storeItemCollection, storeItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(storeItem);
      });

      it('should add only unique StoreItem to an array', () => {
        const storeItemArray: IStoreItem[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const storeItemCollection: IStoreItem[] = [sampleWithRequiredData];
        expectedResult = service.addStoreItemToCollectionIfMissing(storeItemCollection, ...storeItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const storeItem: IStoreItem = sampleWithRequiredData;
        const storeItem2: IStoreItem = sampleWithPartialData;
        expectedResult = service.addStoreItemToCollectionIfMissing([], storeItem, storeItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(storeItem);
        expect(expectedResult).toContain(storeItem2);
      });

      it('should accept null and undefined values', () => {
        const storeItem: IStoreItem = sampleWithRequiredData;
        expectedResult = service.addStoreItemToCollectionIfMissing([], null, storeItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(storeItem);
      });

      it('should return initial array if no StoreItem is added', () => {
        const storeItemCollection: IStoreItem[] = [sampleWithRequiredData];
        expectedResult = service.addStoreItemToCollectionIfMissing(storeItemCollection, undefined, null);
        expect(expectedResult).toEqual(storeItemCollection);
      });
    });

    describe('compareStoreItem', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStoreItem(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = null;

        const compareResult1 = service.compareStoreItem(entity1, entity2);
        const compareResult2 = service.compareStoreItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'CBA' };

        const compareResult1 = service.compareStoreItem(entity1, entity2);
        const compareResult2 = service.compareStoreItem(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 'ABC' };
        const entity2 = { id: 'ABC' };

        const compareResult1 = service.compareStoreItem(entity1, entity2);
        const compareResult2 = service.compareStoreItem(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
