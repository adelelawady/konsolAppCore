import { Component } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'jhi-playstation-layout',
  templateUrl: './playstation-layout.component.html',
  styleUrls: ['./playstation-layout.component.scss']
})
export class PlaystationLayoutComponent {
  isDashboard = false;

  constructor(private router: Router) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      this.isDashboard = event.url.includes('/playstation/dashboard');
    });
  }
}
