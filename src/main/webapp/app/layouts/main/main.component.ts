import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd, ActivatedRoute } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';
import { filter, shareReplay, switchMap } from 'rxjs/operators';
import { interval, Observable, of, Subscription } from 'rxjs';
import { map, distinctUntilChanged } from 'rxjs/operators';
import { PlaystationContainerFormService } from 'app/entities/playstation-container/update/playstation-container-form.service';
import { firstValueFrom } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { FindLanguageFromKeyPipe } from 'app/shared/language/find-language-from-key.pipe';
import { IPlaystationContainer } from 'app/entities/playstation-container/playstation-container.model';
import { PlaystationContainerStateService } from 'app/pages/playstation/services/playstation-container.service';
import { PlaystationContainer, PlaystationContainerResourceService, SheftDTO } from 'app/core/konsolApi';
import { ActiveSheftService } from 'app/pages/admin/sheft-component/active-sheft.service';

interface Breadcrumb {
  label: string;
  url: string;
}

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss'],
})
export class MainComponent implements OnInit {
  private renderer: Renderer2;
  breadcrumbs$: Observable<Breadcrumb[]> = of([]);
  containers: PlaystationContainer[] = [];
  loadingContainerId: string | null = null;
  selectedContainerId: string | null = null;
  activeSheft$: Observable<SheftDTO | null> | undefined;
  durationSubscription: Subscription | undefined;
  isPlaystationLayout = false;

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private findLanguageFromKeyPipe: FindLanguageFromKeyPipe,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
    private activatedRoute: ActivatedRoute,
    private activeSheftService: ActiveSheftService,
    private playstationContainerStateService: PlaystationContainerStateService,
    private playstationContainerResourceService: PlaystationContainerResourceService
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);

    // Subscribe to router events to detect routes with hasPlaystationLayout
    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(() => {
      let route = this.activatedRoute.firstChild;
      this.isPlaystationLayout = false;

      // Check all child routes for hasPlaystationLayout data
      while (route) {
        if (route.snapshot.data['hasPlaystationLayout']) {
          this.isPlaystationLayout = true;
          break;
        }
        route = route.firstChild;
      }
    });
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe(() => {
      this.activeSheft$ = of(this.accountService.hasAnyAuthority(['ROLE_VIEW_ACTIVE_SHEFT', 'ROLE_ADMIN'])).pipe(
        switchMap(hasAuthority => hasAuthority ? this.activeSheftService.getActiveSheft() : of(null)),
        shareReplay(1)
      );
    });


   
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });
    this.durationSubscription = interval(1000).subscribe(() => {
      // This will trigger change detection and update the duration display
    });
    this.translateService.onLangChange.subscribe((langChangeEvent: LangChangeEvent) => {
      this.updateTitle();
      dayjs.locale(langChangeEvent.lang);
      this.renderer.setAttribute(document.querySelector('html'), 'lang', langChangeEvent.lang);

      this.updatePageDirection();
    });

    this.breadcrumbs$ = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd),
      distinctUntilChanged(),
      map(event => {
        let route = this.activatedRoute.root;
        const breadcrumbs: Breadcrumb[] = [];

        while (route.children.length) {
          const childRoute = route.children[0];
          if (childRoute.snapshot.data['breadcrumb']) {
            const url = this.router.url
              .split('?')[0]
              .split('/')
              .slice(0, breadcrumbs.length + 2)
              .join('/');
            breadcrumbs.push({
              label: childRoute.snapshot.data['breadcrumb'],
              url: url,
            });
          }
          route = childRoute;
        }
        return breadcrumbs;
      })
    );

    this.loadContainers();

    // Track route changes to highlight selected container
    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe(() => {
      const urlParts = this.router.url.split('/');
      const containerIndex = urlParts.indexOf('container');
      if (containerIndex !== -1 && urlParts[containerIndex + 1]) {
        this.selectedContainerId = urlParts[containerIndex + 1];
      } else {
        this.selectedContainerId = null;
      }
    });
  }
  getDuration(startTime: any): string {
    if (!startTime) return '00h:00m';

    const start = new Date(startTime).getTime();
    const now = new Date().getTime();
    const diff = Math.floor((now - start) / 1000); // difference in seconds

    const hours = Math.floor(diff / 3600);
    const minutes = Math.floor((diff % 3600) / 60);

    return `${hours.toString().padStart(2, '0')}h:${minutes.toString().padStart(2, '0')}m`;
  }
  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'global.title';
    }
    this.translateService.get(pageTitle).subscribe(title => this.titleService.setTitle(title));
  }

  private updatePageDirection(): void {
    this.renderer.setAttribute(
      document.querySelector('html'),
      'dir',
      this.findLanguageFromKeyPipe.isRTL(this.translateService.currentLang) ? 'rtl' : 'ltr'
    );
  }

  private loadContainers(): void {
    this.playstationContainerResourceService.getPlaystationContainers().subscribe(response => {
      this.containers = response ?? [];
    });
  }

  async onContainerClick(container: PlaystationContainer, event: Event): Promise<void> {
    event.preventDefault();

    try {
      this.loadingContainerId = String(container.id);

      // Load container from resource first

      // Then authenticate it using the state service
      await firstValueFrom(this.playstationContainerStateService.loadContainer(String(container.id)));

      // Navigate to the container dashboard
      await this.router.navigate(['/container', String(container.id), 'navigation', 'dashboard']);
    } catch (error) {
      console.error('Error loading container:', error);
    } finally {
      this.loadingContainerId = null;
    }
  }

  protected toString(value: any): string {
    return String(value);
  }
  hasAnyAuthority(authorities: string[] | string): boolean {
    return this.accountService.hasAnyAuthority(authorities);
  }
}
