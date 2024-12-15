import { Component, OnInit, OnDestroy, HostListener, ElementRef } from '@angular/core';
import { PlaystationService } from '../../services/playstation.service';
import { Subscription } from 'rxjs';
import { trigger } from '@angular/animations';
import { state, style } from '@angular/animations';
import { animate, transition } from '@angular/animations';

@Component({
  selector: 'jhi-orders-slider',
  templateUrl: './orders-slider.component.html',
  styleUrls: ['./orders-slider.component.scss'],
  animations: [
    trigger('slideInOut', [
      state('out', style({
        transform: 'translateY(100%)',
        visibility: 'hidden'
      })),
      state('in', style({
        transform: 'translateY(0)',
        visibility: 'visible'
      })),
      transition('out => in', [
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')
      ]),
      transition('in => out', [
        animate('300ms cubic-bezier(0.4, 0, 0.2, 1)')
      ])
    ])
  ]
})
export class OrdersSliderComponent implements OnInit, OnDestroy {
  isVisible = false;
  private subscription?: Subscription;

  constructor(private playstationService: PlaystationService, private elementRef: ElementRef) {}

  ngOnInit(): void {
    this.subscription = this.playstationService.showOrders$.subscribe(
      (show: boolean) => {
        this.isVisible = show;
      }
    );
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event): void {
    if (!this.elementRef.nativeElement.contains(event.target) && this.isVisible) {
      this.close();
    }
  }

  toggle(): void {
    if (this.isVisible) {
      this.playstationService.hideOrders();
    } else {
      this.playstationService.showOrdersList();
    }
  }

  close(): void {
    this.playstationService.hideOrders();
  }
}
