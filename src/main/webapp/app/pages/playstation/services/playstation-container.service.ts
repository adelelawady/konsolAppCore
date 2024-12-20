import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { map, tap, catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PlaystationContainerStateService {
  private containerSubject = new BehaviorSubject<PlaystationContainer | null>(null);
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private errorSubject = new BehaviorSubject<string | null>(null);

  // Observable streams
  container$ = this.containerSubject.asObservable();
  loading$ = this.loadingSubject.asObservable();
  error$ = this.errorSubject.asObservable();

  constructor(private playstationContainerResource: PlaystationContainerResourceService) {}

  loadContainer(containerId: string): Observable<PlaystationContainer> {
    this.loadingSubject.next(true);
    this.errorSubject.next(null);

    return this.playstationContainerResource.getPlaystationContainer(containerId).pipe(
      tap(container => {
        this.containerSubject.next(container);
        this.loadingSubject.next(false);
      }),
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Error loading container');
        throw error;
      })
    );
  }

  getCurrentContainer(): PlaystationContainer | null {
    return this.containerSubject.getValue();
  }

  clearContainer(): void {
    this.containerSubject.next(null);
    this.errorSubject.next(null);
  }
} 