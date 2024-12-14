import { Component, OnInit, OnDestroy, HostListener, ElementRef } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';
import { PlaystationService } from '../../services/playstation.service';
import { Subscription } from 'rxjs';

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
    this.subscription = this.playstationService.ordersListVisible$.subscribe(
      visible => {
        this.isVisible = visible;
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
    this.isVisible = !this.isVisible;
  }

  hide(): void {
    this.isVisible = false;
  }

  show(): void {
    this.isVisible = true;
  }

  close(): void {
    this.playstationService.hideOrdersList();
  }
}
