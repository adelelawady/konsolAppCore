import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';
import {
  faGamepad,
  faCompass,
  faClock,
  faTachometerAlt,
  faList,
  faBox,
  faUsersCog,
  faUsers,
  faHeart,
  faCogs,
  faTasks,
  faBook,
  faFlag,
  faFlagUsa,
  faUser,
  faWrench,
  faLock,
  faSignOutAlt,
  faSignInAlt,
  faUserPlus,
  faAsterisk,
  faHistory,
} from '@fortawesome/free-solid-svg-icons';

import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { Authority } from 'app/config/authority.constants';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: any[] = [];

  // Icons
  faGamepad = faGamepad;
  faCompass = faCompass;
  faClock = faClock;
  faTachometerAlt = faTachometerAlt;
  faList = faList;
  faBox = faBox;
  faUsersCog = faUsersCog;
  faUsers = faUsers;
  faHeart = faHeart;
  faCogs = faCogs;
  faTasks = faTasks;
  faBook = faBook;
  faFlag = faFlag;
  faFlagUsa = faFlagUsa;
  faUser = faUser;
  faWrench = faWrench;
  faLock = faLock;
  faSignOutAlt = faSignOutAlt;
  faSignInAlt = faSignInAlt;
  faUserPlus = faUserPlus;
  faAsterisk = faAsterisk;
  faHistory = faHistory;

  containers: PlaystationContainer[] = [];

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
    private playstationContainerResourceService: PlaystationContainerResourceService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
    });
    this.loadContainers();
  }
  private loadContainers(): void {
    this.playstationContainerResourceService.getPlaystationContainers().subscribe(response => {
      this.containers = response ?? [];
    });
  }
  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    return this.accountService.hasAnyAuthority(authorities);
  }
}
