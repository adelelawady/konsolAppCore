import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IPlayStationSession } from '../play-station-session.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../play-station-session.test-samples';

import { PlayStationSessionService, RestPlayStationSession } from './play-station-session.service';

const requireRestSample: RestPlayStationSession = {
  ...sampleWithRequiredData,
  startTime: sampleWithRequiredData.startTime?.toJSON(),
  endTime: sampleWithRequiredData.endTime?.toJSON(),
};

describe('PlayStationSession Service', () => {
  let service: PlayStationSessionService;
  let httpMock: HttpTestingController;
  let expectedResult: IPlayStationSession | IPlayStationSession[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(PlayStationSessionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a PlayStationSession', () => {
      const playStationSession = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(playStationSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PlayStationSession', () => {
      const playStationSession = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(playStationSession).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PlayStationSession', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PlayStationSession', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PlayStationSession', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPlayStationSessionToCollectionIfMissing', () => {
      it('should add a PlayStationSession to an empty array', () => {
        const playStationSession: IPlayStationSession = sampleWithRequiredData;
        expectedResult = service.addPlayStationSessionToCollectionIfMissing([], playStationSession);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playStationSession);
      });

      it('should not add a PlayStationSession to an array that contains it', () => {
        const playStationSession: IPlayStationSession = sampleWithRequiredData;
        const playStationSessionCollection: IPlayStationSession[] = [
          {
            ...playStationSession,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPlayStationSessionToCollectionIfMissing(playStationSessionCollection, playStationSession);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PlayStationSession to an array that doesn't contain it", () => {
        const playStationSession: IPlayStationSession = sampleWithRequiredData;
        const playStationSessionCollection: IPlayStationSession[] = [sampleWithPartialData];
        expectedResult = service.addPlayStationSessionToCollectionIfMissing(playStationSessionCollection, playStationSession);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playStationSession);
      });

      it('should add only unique PlayStationSession to an array', () => {
        const playStationSessionArray: IPlayStationSession[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const playStationSessionCollection: IPlayStationSession[] = [sampleWithRequiredData];
        expectedResult = service.addPlayStationSessionToCollectionIfMissing(playStationSessionCollection, ...playStationSessionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const playStationSession: IPlayStationSession = sampleWithRequiredData;
        const playStationSession2: IPlayStationSession = sampleWithPartialData;
        expectedResult = service.addPlayStationSessionToCollectionIfMissing([], playStationSession, playStationSession2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(playStationSession);
        expect(expectedResult).toContain(playStationSession2);
      });

      it('should accept null and undefined values', () => {
        const playStationSession: IPlayStationSession = sampleWithRequiredData;
        expectedResult = service.addPlayStationSessionToCollectionIfMissing([], null, playStationSession, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(playStationSession);
      });

      it('should return initial array if no PlayStationSession is added', () => {
        const playStationSessionCollection: IPlayStationSession[] = [sampleWithRequiredData];
        expectedResult = service.addPlayStationSessionToCollectionIfMissing(playStationSessionCollection, undefined, null);
        expect(expectedResult).toEqual(playStationSessionCollection);
      });
    });

    describe('comparePlayStationSession', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePlayStationSession(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePlayStationSession(entity1, entity2);
        const compareResult2 = service.comparePlayStationSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePlayStationSession(entity1, entity2);
        const compareResult2 = service.comparePlayStationSession(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePlayStationSession(entity1, entity2);
        const compareResult2 = service.comparePlayStationSession(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
