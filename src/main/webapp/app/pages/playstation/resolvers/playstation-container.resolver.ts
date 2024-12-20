import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Observable, of } from 'rxjs';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { PlaystationContainerStateService } from '../services/playstation-container.service';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PlaystationContainerResolver implements Resolve<PlaystationContainer | null> {
  constructor(private containerStateService: PlaystationContainerStateService) {}

  resolve(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<PlaystationContainer | null> {
    const containerId = route.paramMap.get('containerId');
    
    if (!containerId) {
      return of(null);
    }

    return this.containerStateService.loadContainer(containerId).pipe(
      catchError(() => of(null))
    );
  }
} 