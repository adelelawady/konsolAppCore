import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationService } from '../services/playstation.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, OnDestroy {
  checkoutMode = false;
  private subscription?: Subscription;

  constructor(private playstationService: PlaystationService) {}

  ngOnInit(): void {
    this.subscription = this.playstationService.checkout$.subscribe((show: boolean) => {
      this.checkoutMode = show;
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
