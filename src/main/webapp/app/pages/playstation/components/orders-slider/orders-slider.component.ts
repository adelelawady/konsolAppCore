import { Component, OnInit, HostListener, ElementRef } from '@angular/core';
import { trigger, state, style, transition, animate } from '@angular/animations';

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
export class OrdersSliderComponent implements OnInit {
  isVisible = false;

  constructor(private elementRef: ElementRef) {}

  ngOnInit(): void {}

  @HostListener('document:click', ['$event'])
  clickOutside(event: Event): void {
    if (!this.elementRef.nativeElement.contains(event.target) && this.isVisible) {
      this.hide();
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
}
