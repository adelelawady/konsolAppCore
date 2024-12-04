import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { LANGUAGES } from 'app/config/language.constants';
import { IUser, User } from '../user-management.model';
import { UserManagementService, IAuthority } from '../service/user-management.service';

@Component({
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
  styleUrls: ['./user-management-update.component.scss'],
})
export class UserManagementUpdateComponent implements OnInit {
  user: IUser | null = null;
  authorities: IAuthority[] = [];
  isSaving = false;
  languages = LANGUAGES;
  private fb!: FormBuilder;
  editForm!: FormGroup;
  authorityGroups: { category: string; authorities: IAuthority[] }[] = [];

  constructor(private userService: UserManagementService, private route: ActivatedRoute, fb: FormBuilder) {
    this.fb = fb;
    this.editForm = this.fb.group({
      id: [null as string | null],
      login: [
        '',
        [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(50),
          Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
        ],
      ],
      firstName: [null as string | null, [Validators.maxLength(50)]],
      lastName: [null as string | null, [Validators.maxLength(50)]],
      email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
      activated: [true],
      langKey: ['en'],
      authorities: [[] as string[]],
    });
  }

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      if (user) {
        this.user = new User(
          user.id,
          user.login,
          user.firstName,
          user.lastName,
          user.email,
          user.activated ?? true,
          user.langKey,
          user.authorities,
          user.createdBy,
          user.createdDate,
          user.lastModifiedBy,
          user.lastModifiedDate
        );
        this.updateForm(this.user);
      }
    });

    this.userService.authorities().subscribe(authorities => {
      this.authorities = authorities;
      this.organizeAuthorities(authorities);
    });
  }

  private organizeAuthorities(authorities: IAuthority[]): void {
    // Group authorities by category
    const groupedByCategory = authorities.reduce((groups: { [key: string]: IAuthority[] }, auth) => {
      const category = auth.category || 'Other';
      if (!groups[category]) {
        groups[category] = [];
      }
      groups[category].push(auth);
      return groups;
    }, {});

    // Convert to array format
    this.authorityGroups = Object.entries(groupedByCategory).map(([category, auths]) => ({
      category,
      authorities: auths,
    }));
  }

  updateForm(user: IUser): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      activated: user.activated,
      langKey: user.langKey,
      authorities: user.authorities,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const user = this.updateUser();
    if (user.id !== null) {
      this.userService.update(user).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    } else {
      this.userService.create(user).subscribe({
        next: () => this.onSaveSuccess(),
        error: () => this.onSaveError(),
      });
    }
  }

  private updateUser(): IUser {
    const formValue = this.editForm.value;
    const user = {
      id: formValue.id,
      login: formValue.login,
      firstName: formValue.firstName,
      lastName: formValue.lastName,
      email: formValue.email,
      activated: formValue.activated,
      langKey: formValue.langKey,
      authorities: formValue.authorities,
    };
    return user;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }

  updateAuthorities(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    const authority = checkbox.value;
    const authorities = this.editForm.get('authorities')!.value as string[];

    if (checkbox.checked && !authorities.includes(authority)) {
      authorities.push(authority);
    } else if (!checkbox.checked && authorities.includes(authority)) {
      authorities.splice(authorities.indexOf(authority), 1);
    }

    this.editForm.patchValue({ authorities });
  }
}
