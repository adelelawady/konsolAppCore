import { Injectable } from '@angular/core';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';
import { SheftDTO } from 'app/core/konsolApi/model/sheftDTO';
import { BehaviorSubject, Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ActiveSheftService {
  private activeSheftSubject = new BehaviorSubject<SheftDTO | null>(null);
  private lastFetchTime: number = 0;
  private readonly CACHE_DURATION = 30000; // 30 seconds cache

  constructor(private sheftResource: SheftResourceService) {}

  getActiveSheft(forceRefresh: boolean = false): Observable<SheftDTO | null> {
    const now = Date.now();
    if (forceRefresh || !this.activeSheftSubject.value || now - this.lastFetchTime > this.CACHE_DURATION) {
      this.fetchActiveSheft();
    }
    return this.activeSheftSubject.asObservable();
  }

  private fetchActiveSheft(): void {
    this.sheftResource.getActiveSheft().subscribe({
      next: sheft => {
        this.lastFetchTime = Date.now();
        this.activeSheftSubject.next(sheft);
      },
      error: error => {
        console.error('Error fetching active sheft:', error);
        this.activeSheftSubject.next(null);
      },
    });
  }

  clearCache(): void {
    this.lastFetchTime = 0;
    this.activeSheftSubject.next(null);
  }

  isSheftActive(sheft: SheftDTO): boolean {
    const activeSheft = this.activeSheftSubject.value;
    return activeSheft?.id === sheft.id;
  }
}
