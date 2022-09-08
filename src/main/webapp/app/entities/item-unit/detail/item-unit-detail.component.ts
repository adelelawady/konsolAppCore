import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IItemUnit } from '../item-unit.model';

@Component({
  selector: 'jhi-item-unit-detail',
  templateUrl: './item-unit-detail.component.html',
})
export class ItemUnitDetailComponent implements OnInit {
  itemUnit: IItemUnit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ itemUnit }) => {
      this.itemUnit = itemUnit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
