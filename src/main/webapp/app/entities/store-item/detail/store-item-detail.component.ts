import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStoreItem } from '../store-item.model';

@Component({
  selector: 'jhi-store-item-detail',
  templateUrl: './store-item-detail.component.html',
})
export class StoreItemDetailComponent implements OnInit {
  storeItem: IStoreItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ storeItem }) => {
      this.storeItem = storeItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
