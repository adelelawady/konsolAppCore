import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  private readonly destroy$ = new Subject<void>();

  mainMenuItems = [
    {
      title: 'الفواتير',
      route: '/invoices',
      icon: this.getSvgIcon('receipt'),
      color: 'primary',
      translationKey: 'invoices',
      description: 'Manage invoices and billing',
    },
    {
      title: 'الاصناف',
      route: '/categories',
      icon: this.getSvgIcon('category'),
      color: 'indigo',
      translationKey: 'categories',
      description: 'Manage product categories',
    },
    {
      title: 'الحسابات',
      route: '/accounts',
      icon: this.getSvgIcon('wallet'),
      color: 'orange',
      translationKey: 'accounts',
      description: 'Financial accounts management',
    },
    {
      title: 'المخزن',
      route: '/inventory',
      icon: this.getSvgIcon('box'),
      color: 'yellow',
      translationKey: 'inventory',
      description: 'Inventory management',
    },
    {
      title: 'شراء',
      route: '/purchase',
      icon: this.getSvgIcon('shopping-cart'),
      color: 'teal',
      translationKey: 'purchase',
      description: 'Purchase management',
    },
    {
      title: 'بيع',
      route: '/sales',
      icon: this.getSvgIcon('cash'),
      color: 'cyan',
      translationKey: 'sales',
      description: 'Sales management',
    },
  ];

  subMenuItems = [
    {
      title: 'صنف جديد',
      route: '/categories/new',
      icon: this.getSvgIcon('plus'),
      color: 'blue',
      translationKey: 'newCategory',
      description: 'Add new category',
    },
    {
      title: 'حركة الاموال',
      route: '/accounts/money-movement',
      icon: this.getSvgIcon('arrows-left-right'),
      color: 'slate',
      translationKey: 'moneyMovement',
      description: 'Money movement tracking',
    },
    {
      title: 'صرف',
      route: '/accounts/exchange',
      icon: this.getSvgIcon('coin'),
      color: 'gray',
      translationKey: 'exchange',
      description: 'Exchange operations',
    },
    {
      title: 'قبض',
      route: '/accounts/receipt',
      icon: this.getSvgIcon('receipt-2'),
      color: 'zinc',
      translationKey: 'receipt',
      description: 'Receipt management',
    },
  ];

  constructor(private accountService: AccountService, private router: Router, private sanitizer: DomSanitizer) {}

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
    };

    return this.sanitizer.bypassSecurityTrustHtml(icons[name] || '');
  }
}
