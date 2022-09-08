import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { AccountUserFormService, AccountUserFormGroup } from './account-user-form.service';
import { IAccountUser } from '../account-user.model';
import { AccountUserService } from '../service/account-user.service';
import { AccountKind } from 'app/entities/enumerations/account-kind.model';

@Component({
  selector: 'jhi-account-user-update',
  templateUrl: './account-user-update.component.html',
})
export class AccountUserUpdateComponent implements OnInit {
  isSaving = false;
  accountUser: IAccountUser | null = null;
  accountKindValues = Object.keys(AccountKind);

  editForm: AccountUserFormGroup = this.accountUserFormService.createAccountUserFormGroup();

  constructor(
    protected accountUserService: AccountUserService,
    protected accountUserFormService: AccountUserFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountUser }) => {
      this.accountUser = accountUser;
      if (accountUser) {
        this.updateForm(accountUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountUser = this.accountUserFormService.getAccountUser(this.editForm);
    if (accountUser.id !== null) {
      this.subscribeToSaveResponse(this.accountUserService.update(accountUser));
    } else {
      this.subscribeToSaveResponse(this.accountUserService.create(accountUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(accountUser: IAccountUser): void {
    this.accountUser = accountUser;
    this.accountUserFormService.resetForm(this.editForm, accountUser);
  }
}
