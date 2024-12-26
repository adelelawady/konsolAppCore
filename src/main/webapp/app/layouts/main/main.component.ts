import { Component, OnInit, RendererFactory2, Renderer2 } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd, ActivatedRoute } from '@angular/router';
import { TranslateService, LangChangeEvent } from '@ngx-translate/core';
import dayjs from 'dayjs/esm';
import { filter } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { map, distinctUntilChanged } from 'rxjs/operators';
import { PlaystationContainerFormService } from 'app/entities/playstation-container/update/playstation-container-form.service';
import { firstValueFrom } from 'rxjs';

import { AccountService } from 'app/core/auth/account.service';
import { FindLanguageFromKeyPipe } from 'app/shared/language/find-language-from-key.pipe';
import { IPlaystationContainer } from 'app/entities/playstation-container/playstation-container.model';
import { PlaystationContainerStateService } from 'app/pages/playstation/services/playstation-container.service';
import { PlaystationContainer, PlaystationContainerResourceService } from 'app/core/konsolApi';

interface Breadcrumb {
  label: string;
  url: string;
}

@Component({
  selector: 'jhi-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  private renderer: Renderer2;
  breadcrumbs$: Observable<Breadcrumb[]> = of([]);
  containers: PlaystationContainer[] = [];
  loadingContainerId: string | null = null;

  constructor(
    private accountService: AccountService,
    private titleService: Title,
    private router: Router,
    private findLanguageFromKeyPipe: FindLanguageFromKeyPipe,
    private translateService: TranslateService,
    rootRenderer: RendererFactory2,
    private activatedRoute: ActivatedRoute,
    private playstationContainerStateService: PlaystationContainerStateService,
    private playstationContainerResourceService: PlaystationContainerResourceService
  ) {
    this.renderer = rootRenderer.createRenderer(document.querySelector('html'), null);
  }

  ngOnInit(): void {
    // try to log in automatically
    this.accountService.identity().subscribe();

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
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
            const url = this.router.url.split('?')[0].split('/').slice(0, breadcrumbs.length + 2).join('/');
            breadcrumbs.push({
              label: childRoute.snapshot.data['breadcrumb'],
              url: url
            });
          }
          route = childRoute;
        }
        return breadcrumbs;
      })
    );

    this.loadContainers();
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
      await firstValueFrom(this.playstationContainerStateService.loadContainer( String(container.id)));
      
      // Navigate to the container dashboard
      await this.router.navigate(['/container',  String(container.id), 'navigation', 'dashboard']);
    } catch (error) {
      console.error('Error loading container:', error);
    } finally {
      this.loadingContainerId = null;
    }
  }
}
