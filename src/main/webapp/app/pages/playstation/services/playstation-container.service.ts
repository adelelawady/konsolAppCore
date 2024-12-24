import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, ReplaySubject, of } from 'rxjs';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { catchError, shareReplay, tap } from 'rxjs/operators';
import { SessionStorageService } from 'ngx-webstorage';

@Injectable({
  providedIn: 'root',
})
export class PlaystationContainerStateService {
  currentContainerId: string | null = null;
  containerIdentity: PlaystationContainer | null = null;
  containerState$ = new ReplaySubject<PlaystationContainer | null>(1);
  containerCache$?: Observable<PlaystationContainer> | null;
  loadingSubject$ = new BehaviorSubject<boolean>(false);
  errorSubject$ = new BehaviorSubject<string | null>(null);

  // Public observables
  public loading$ = this.loadingSubject$.asObservable();
  public error$ = this.errorSubject$.asObservable();
  public container$ = this.containerState$.asObservable();

  constructor(private playstationContainerResource: PlaystationContainerResourceService, private sessionStorage: SessionStorageService) {}

  /**
   * Authenticate and store the container in memory and session storage.
   * @param container The PlaystationContainer to authenticate.
   */
  authenticate(container: PlaystationContainer | null): void {
    this.containerIdentity = container;
    this.containerState$.next(this.containerIdentity);

    if (container) {
      // Save the container in session storage using its ID as the key
      this.sessionStorage.store(`playstation-container-${container.id}`, container);
    } else if (this.currentContainerId) {
      // Clear the container from session storage using the current ID
      this.sessionStorage.clear(`playstation-container-${this.currentContainerId}`);
    }
  }

  /**
   * Load a container by its ID, optionally forcing a refresh.
   * @param containerId The ID of the container to load.
   * @param force Whether to force a reload.
   * @returns An observable emitting the loaded container.
   */
  loadContainer(containerId: string, force = false): Observable<PlaystationContainer | null> {
    if (this.currentContainerId !== containerId || force) {
      this.currentContainerId = containerId;
      this.loadingSubject$.next(true);
      this.errorSubject$.next(null);

      this.containerCache$ = this.playstationContainerResource.getPlaystationContainer(containerId).pipe(
        tap(container => {
          this.authenticate(container);
          this.loadingSubject$.next(false);
        }),
        shareReplay(1)
      );
    }

    if (this.containerCache$) {
      return this.containerCache$.pipe(
        catchError(error => {
          this.loadingSubject$.next(false);
          this.errorSubject$.next(error.message || 'Error loading container');
          return of(null);
        })
      );
    } else {
      return of(null);
    }
  }

  /**
   * Get the currently authenticated container.
   * @returns The current PlaystationContainer or null if not authenticated.
   */
  getCurrentContainer(): PlaystationContainer | null {
    if (this.containerIdentity) {
      return this.containerIdentity;
    }

    if (this.currentContainerId) {
      const storedContainer = this.sessionStorage.retrieve(`playstation-container-${this.currentContainerId}`);
      if (storedContainer) {
        this.authenticate(storedContainer);
        // eslint-disable-next-line @typescript-eslint/no-unsafe-return
        return storedContainer;
      }
    }

    return null;
  }

  /**
   * Get the current container ID.
   * @returns The current container ID or null if not set.
   */
  getCurrentContainerId(): string | null {
    return this.currentContainerId;
  }

  /**
   * Clear the current container and its state.
   */
  clearContainer(): void {
    if (this.currentContainerId) {
      this.sessionStorage.clear(`playstation-container-${this.currentContainerId}`);
    }
    this.currentContainerId = null;
    this.containerCache$ = null;
    this.authenticate(null);
    this.errorSubject$.next(null);
  }

  /**
   * Check if a container is currently authenticated.
   * @returns True if a container is authenticated, false otherwise.
   */
  hasContainer(): boolean {
    return this.getCurrentContainer() !== null;
  }

  /**
   * Get the container state as an observable.
   * @returns An observable emitting the current container state.
   */
  getContainerState(): Observable<PlaystationContainer | null> {
    return this.containerState$.asObservable();
  }

  /**
   * Reload the current container, if one is set.
   * @returns An observable emitting the reloaded container or null if no ID is set.
   */
  reloadCurrentContainer(): Observable<PlaystationContainer | null> {
    if (this.currentContainerId) {
      return this.loadContainer(this.currentContainerId, true);
    }

    return of(null);
  }
}
