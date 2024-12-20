import { Component, OnInit } from '@angular/core';
import { PlaystationContainerStateService } from '../services/playstation-container.service';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { Observable } from 'rxjs';

@Component({
  selector: 'jhi-example',
  template: `
    <ng-container *ngIf="container$ | async as container">
      <div>Container Name: {{ container.name }}</div>
      <!-- Other container properties -->
    </ng-container>
    
    <div *ngIf="loading$ | async">Loading...</div>
    <div *ngIf="error$ | async as error" class="alert alert-danger">{{ error }}</div>
  `
})
export class ExampleComponent implements OnInit {
  container$: Observable<PlaystationContainer | null>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  constructor(private containerStateService: PlaystationContainerStateService) {
    this.container$ = this.containerStateService.container$;
    this.loading$ = this.containerStateService.loading$;
    this.error$ = this.containerStateService.error$;
  }

  ngOnInit(): void {
    // The container will be automatically loaded by the resolver
    // You can access it through the container$ observable
  }
} 