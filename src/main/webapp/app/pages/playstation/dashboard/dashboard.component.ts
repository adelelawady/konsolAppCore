import { Component, OnInit, OnDestroy } from '@angular/core';
import { PlaystationService } from '../services/playstation.service';
import { Subscription } from 'rxjs';
import { ActivatedRoute } from '@angular/router';
import { PlaystationContainer } from 'app/core/konsolApi';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
  container: PlaystationContainer | null | undefined;
  checkoutMode = false;
  private subscription?: Subscription;

  constructor(private playstationService: PlaystationService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    // First try to get container from resolver data
    this.route.data.subscribe(data => {
      if (data['container']) {
        this.container = data['container'];
        this.checkoutMode = false;
      }
    });

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
