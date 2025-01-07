import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { SharedModule } from '../../shared/shared.module';
import { Authority } from 'app/config/authority.constants';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';

interface MenuItem {
  title: string;
  route: string;
  icon: SafeHtml;
  color: string;
  translationKey: string;
  description: string;
  authorities: string[];
}

@Component({
  selector: 'jhi-navigation',
  standalone: true,
  imports: [SharedModule, RouterModule],
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  private readonly destroy$ = new Subject<void>();

  mainMenuItems: MenuItem[] = [
    {
      title: 'global.navigation.pages.invoices.title',
      route: '/invoices',
      icon: this.getSvgIcon('receipt'),
      color: 'primary',
      translationKey: 'invoices',
      description: 'global.navigation.pages.invoices.description',
      authorities: [Authority.ADMIN, 'ROLE_MANAGE_INVOICE'],
    },
    {
      title: 'global.navigation.pages.categories.title',
      route: '/products',
      icon: this.getSvgIcon('category'),
      color: 'indigo',
      translationKey: 'categories',
      description: 'global.navigation.pages.categories.description',
      authorities: [Authority.ADMIN, 'ROLE_MANAGE_ITEM'],
    },
    {
      title: 'global.navigation.pages.money.title',
      route: '/money',
      icon: this.getSvgIcon('cash'),
      color: 'green',
      translationKey: 'money',
      description: 'global.navigation.pages.money.description',
      authorities: [Authority.ADMIN, 'ROLE_MANAGE_BANK'],
    },
    {
      title: 'global.navigation.pages.accounts.title',
      route: '/accounts',
      icon: this.getSvgIcon('wallet'),
      color: 'orange',
      translationKey: 'accounts',
      description: 'global.navigation.pages.accounts.description',
      authorities: [Authority.ADMIN, 'ROLE_MANAGE_ACCOUNT'],
    },
    {
      title: 'global.navigation.pages.financial-reports.title',
      route: '/financial-reports',
      icon: this.getSvgIcon('report'),
      color: 'blue',
      translationKey: 'financial-reports',
      description: 'global.navigation.pages.financial-reports.description',
      authorities: [Authority.ADMIN, 'ROLE_MANAGE_FINANCE'],
    },
    {
      title: 'global.navigation.pages.inventory.title',
      route: '/inventory',
      icon: this.getSvgIcon('box'),
      color: 'yellow',
      translationKey: 'inventory',
      description: 'global.navigation.pages.inventory.description',
      authorities: [Authority.ADMIN, 'ROLE_MANAGE_STORE'],
    },
    {
      title: 'global.navigation.pages.purchase.title',
      route: '/purchase',
      icon: this.getSvgIcon('shopping-cart'),
      color: 'teal',
      translationKey: 'purchase',
      description: 'global.navigation.pages.purchase.description',
      authorities: [Authority.ADMIN, 'ROLE_VIEW_PURCHASE'],
    },
    {
      title: 'global.navigation.pages.sales.title',
      route: '/sales',
      icon: this.getSvgIcon('cash'),
      color: 'cyan',
      translationKey: 'sales',
      description: 'global.navigation.pages.sales.description',
      authorities: [Authority.ADMIN, 'ROLE_VIEW_SALES'],
    },
  ];

  subMenuItems: MenuItem[] = [  
    {
      title: 'playstation.lastSessions.title',
      route: '/last-sessions',
      icon: this.getSvgIcon('history'),
      color: 'purple',
      translationKey: 'playstation.lastSessions.title',
      description: 'playstation.lastSessions.title',
      authorities: [Authority.ADMIN, 'ROLE_VIEW_PLAYSTATION_SESSION'],
    }
  ];
  containers: PlaystationContainer[] = [];  

  constructor(
    private accountService: AccountService, 
    private router: Router, 
    private sanitizer: DomSanitizer,
    private playstationContainerResourceService: PlaystationContainerResourceService
  ) {}

  ngOnInit(): void {
    // Check authentication state
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        if (!account) {
          this.router.navigate(['/']);
        }
      });
    this.loadContainers();
  }

  loadContainers(): void {
    this.playstationContainerResourceService.getPlaystationContainers().subscribe(response => {
      this.containers = response ?? [];
      if (this.containers.length > 0 && this.containers[0] && this.containers[0].id) {
        this.subMenuItems.push({
          title: 'playstation.navigation.title',
          route: '/container/'+this.containers[0].id+'/navigation/dashboard',
          icon: this.getSvgIcon('dashboard'),
          color: 'green',
          translationKey: 'playstation.navigation.title', 
          description: 'playstation.navigation.title',
          authorities: [Authority.ADMIN, 'ROLE_VIEW_PLAYSTATION_DEVICE'],
        });
      }
    });
  }
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private getSvgIcon(name: string): SafeHtml {
    const icons: { [key: string]: string } = {
      receipt: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-receipt" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M5 21v-16a2 2 0 0 1 2 -2h10a2 2 0 0 1 2 2v16l-3 -2l-2 2l-2 -2l-2 2l-2 -2l-3 2"></path>
        <path d="M14 8h-2.5a1.5 1.5 0 0 0 0 3h1a1.5 1.5 0 0 1 0 3h-2.5m2 0v1.5m0 -9v1.5"></path>
      </svg>`,
      category: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-category" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M4 4h6v6h-6z"></path>
        <path d="M14 4h6v6h-6z"></path>
        <path d="M4 14h6v6h-6z"></path>
        <path d="M14 14h6v6h-6z"></path>
      </svg>`,
      wallet: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-wallet" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M17 8v-3a1 1 0 0 0 -1 -1h-10a2 2 0 0 0 0 4h12a1 1 0 0 1 1 1v3m0 4v3a1 1 0 0 1 -1 1h-12a2 2 0 0 1 -2 -2v-12"></path>
        <path d="M20 12v4h-4a2 2 0 0 1 0 -4h4"></path>
      </svg>`,
      box: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-box" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M12 3l8 4.5l0 9l-8 4.5l-8 -4.5l0 -9l8 -4.5"></path>
        <path d="M12 12l8 -4.5"></path>
        <path d="M12 12l0 9"></path>
        <path d="M12 12l-8 -4.5"></path>
      </svg>`,
      'shopping-cart': `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-shopping-cart" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M6 19m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0"></path>
        <path d="M17 19m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0"></path>
        <path d="M17 17h-11v-14h-2"></path>
        <path d="M6 5l14 1l-1 7h-13"></path>
      </svg>`,
      cash: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-cash" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M7 9m0 2a2 2 0 0 1 2 -2h10a2 2 0 0 1 2 2v6a2 2 0 0 1 -2 2h-10a2 2 0 0 1 -2 -2z"></path>
        <path d="M14 14m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0"></path>
        <path d="M17 9v-2a2 2 0 0 0 -2 -2h-10a2 2 0 0 0 -2 2v6a2 2 0 0 0 2 2h2"></path>
      </svg>`,
      plus: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-plus" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M12 5l0 14"></path>
        <path d="M5 12l14 0"></path>
      </svg>`,
      'arrows-left-right': `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-arrows-left-right" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M21 17l-18 0"></path>
        <path d="M6 10l-3 -3l3 -3"></path>
        <path d="M3 7l18 0"></path>
        <path d="M18 20l3 -3l-3 -3"></path>
      </svg>`,
      coin: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-coin" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M12 12m-9 0a9 9 0 1 0 18 0a9 9 0 1 0 -18 0"></path>
        <path d="M14.8 9a2 2 0 0 0 -1.8 -1h-2a2 2 0 1 0 0 4h2a2 2 0 1 1 0 4h-2a2 2 0 0 1 -1.8 -1"></path>
        <path d="M12 7v2m0 6v2"></path>
      </svg>`,
      'receipt-2': `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-receipt-2" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M5 21v-16a2 2 0 0 1 2 -2h10a2 2 0 0 1 2 2v16l-3 -2l-2 2l-2 -2l-2 2l-2 -2l-3 2"></path>
        <path d="M14 8h-2.5a1.5 1.5 0 0 0 0 3h1a1.5 1.5 0 0 1 0 3h-2.5m2 0v1.5m0 -9v1.5"></path>
      </svg>`,
      report: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-report" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"></path>
        <path d="M12 3v1m0 16v1m9-9h-1M4 12H3m15.364 6.364l-.707-.707M6.343 6.343l-.707-.707m12.728 12.728l-.707.707"></path>
        <path d="M11 10l-1 1m0 0l1 1m-1-1v-4"></path>
      </svg>`,
      dashboard: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-dashboard" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
        <path d="M12 13m-2 0a2 2 0 1 0 4 0a2 2 0 1 0 -4 0" />
        <path d="M13.45 11.55l2.05 -2.05" />
        <path d="M6.4 20a9 9 0 1 1 11.2 0z" />
      </svg>`,
      history: `<svg xmlns="http://www.w3.org/2000/svg" class="icon icon-tabler icon-tabler-history" width="24" height="24" viewBox="0 0 24 24" stroke-width="2" stroke="currentColor" fill="none" stroke-linecap="round" stroke-linejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
        <path d="M12 8l0 4l2 2" />
        <path d="M3.05 11a9 9 0 1 1 .5 4m-.5 5v-5h5" />
      </svg>`,
    };

    return this.sanitizer.bypassSecurityTrustHtml(icons[name] || '');
  }

  hasAnyAuthority(authorities: string[] | undefined): boolean {
    if (!authorities || authorities.length === 0) {
      return true; // If no authorities specified, show to all
    }
    return this.accountService.hasAnyAuthority(authorities);
  }
}
