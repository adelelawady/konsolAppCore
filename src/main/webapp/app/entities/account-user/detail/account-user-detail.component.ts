import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAccountUser } from '../account-user.model';

@Component({
  selector: 'jhi-account-user-detail',
  templateUrl: './account-user-detail.component.html',
})
export class AccountUserDetailComponent implements OnInit {
  accountUser: IAccountUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountUser }) => {
      this.accountUser = accountUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
