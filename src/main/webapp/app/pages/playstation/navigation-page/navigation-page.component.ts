import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationContainerStateService } from '../services/playstation-container.service';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { Observable, Subscription } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'jhi-navigation-page',
  templateUrl: './navigation-page.component.html',
  styleUrls: ['./navigation-page.component.scss']
})
export class NavigationPageComponent implements OnInit, OnDestroy {
  container$: Observable<PlaystationContainer | null>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;
  private subscription: Subscription | null = null;

  constructor(
    private containerStateService: PlaystationContainerStateService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.container$ = this.containerStateService.container$;
    this.loading$ = this.containerStateService.loading$;
    this.error$ = this.containerStateService.error$;
  }

  ngOnInit(): void {
    // First try to get container from resolver data
    this.route.data.subscribe(data => {
      if (data['container']) {
        console.log('container from resolver',this.containerStateService.getCurrentContainer());
        console.log(data['container']);
        return;
      }
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  getCurrentContainer(): PlaystationContainer | null {
    return this.containerStateService.getCurrentContainer();
  }
}
