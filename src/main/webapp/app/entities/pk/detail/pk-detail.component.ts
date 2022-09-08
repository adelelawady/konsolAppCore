import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPk } from '../pk.model';

@Component({
  selector: 'jhi-pk-detail',
  templateUrl: './pk-detail.component.html',
})
export class PkDetailComponent implements OnInit {
  pk: IPk | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pk }) => {
      this.pk = pk;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
