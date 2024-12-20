import { Component, OnInit } from '@angular/core';
import { PlaystationContainerStateService } from '../services/playstation-container.service';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { Observable } from 'rxjs';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'jhi-navigation-page',
  templateUrl: './navigation-page.component.html',
  styleUrls: ['./navigation-page.component.scss']
})
export class NavigationPageComponent implements OnInit {
  container$: Observable<PlaystationContainer | null>;
  loading$: Observable<boolean>;
  error$: Observable<string | null>;

  constructor(
    private containerStateService: PlaystationContainerStateService,
    private route: ActivatedRoute
  ) {
    this.container$ = this.containerStateService.container$;
    this.loading$ = this.containerStateService.loading$;
    this.error$ = this.containerStateService.error$;
  }

  ngOnInit(): void {
    // Get containerId from route params
    const containerId = this.route.snapshot.paramMap.get('containerId');
    if (containerId) {
      this.containerStateService.loadContainer(containerId).subscribe();
    }
  }
}
