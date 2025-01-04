import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { PlaystationContainerStateService } from '../services/playstation-container.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class PlaystationContainerResolver implements Resolve<PlaystationContainer | null> {
  constructor(private containerStateService: PlaystationContainerStateService) {}

  resolve(route: ActivatedRouteSnapshot): Observable<PlaystationContainer | null> {
    // First try to get containerId from the current route
    const containerId = route.paramMap.get('containerId') ?? route.parent?.paramMap.get('containerId');

    // If neither containerId exists, return null
    if (!containerId) {
      return of(null);
    }
    // If containerId is found, call the service to load the container
    return this.containerStateService.loadContainer(containerId).pipe(
      catchError(() => of(null)) // Catch any error and return null
    );
  }
}
