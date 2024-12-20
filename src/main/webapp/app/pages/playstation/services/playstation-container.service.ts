import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, ReplaySubject, of } from 'rxjs';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { shareReplay, tap, catchError } from 'rxjs/operators';
import { SessionStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root'
})
export class PlaystationContainerStateService {
  private currentContainerId: string | null = null;
  private containerIdentity: PlaystationContainer | null = null;
  private containerState = new ReplaySubject<PlaystationContainer | null>(1);
  private containerCache$?: Observable<PlaystationContainer> | null;
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private errorSubject = new BehaviorSubject<string | null>(null);

  // Observable streams
  loading$ = this.loadingSubject.asObservable();
  error$ = this.errorSubject.asObservable();
  container$ = this.containerState.asObservable();

  constructor(
    private playstationContainerResource: PlaystationContainerResourceService,
    private sessionStorage: SessionStorageService
  ) {}

  authenticate(container: PlaystationContainer | null): void {
    this.containerIdentity = container;
    this.containerState.next(this.containerIdentity);
    if (!container) {
      this.containerCache$ = null;
    }
    // Store in session storage
    if (container) {
      this.sessionStorage.store('playstation-container', container);
    } else {
      this.sessionStorage.clear('playstation-container');
    }
  }

  loadContainer(containerId: string, force?: boolean): Observable<PlaystationContainer | null> {
    this.currentContainerId = containerId;
    
    if (!this.containerCache$ || force) {
      this.loadingSubject.next(true);
      this.errorSubject.next(null);

      this.containerCache$ = this.playstationContainerResource.getPlaystationContainer(containerId).pipe(
        tap(container => {
          this.authenticate(container);
          this.loadingSubject.next(false);
        }),
        shareReplay()
      );
    }
    
    return this.containerCache$.pipe(
      catchError(error => {
        this.loadingSubject.next(false);
        this.errorSubject.next(error.message || 'Error loading container');
        return of(null);
      })
    );
  }

  getCurrentContainer(): PlaystationContainer | null {
    // First try to get from memory
    if (this.containerIdentity) {
      return this.containerIdentity;
    }
    // Then try to get from session storage
    const storedContainer = this.sessionStorage.retrieve('playstation-container');
    if (storedContainer) {
      this.authenticate(storedContainer);
      return storedContainer;
    }
    return null;
  }

  getCurrentContainerId(): string | null {
    return this.currentContainerId;
  }

  clearContainer(): void {
    this.currentContainerId = null;
    this.containerCache$ = null;
    this.authenticate(null);
    this.errorSubject.next(null);
  }

  hasContainer(): boolean {
    return this.getCurrentContainer() !== null;
  }

  getContainerState(): Observable<PlaystationContainer | null> {
    return this.containerState.asObservable();
  }

  reloadCurrentContainer(): Observable<PlaystationContainer | null> {
    if (this.currentContainerId) {
      return this.loadContainer(this.currentContainerId, true);
    }
    return of(null);
  }
} 