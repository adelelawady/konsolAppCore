import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { LANGUAGES } from 'app/config/language.constants';
import { IUser, User } from '../user-management.model';
import { UserManagementService, IAuthority } from '../service/user-management.service';

interface RoleGroup {
  name: string;
  translateKey: string;
  authorities: string[];
}

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
  roleGroups: RoleGroup[] = [
    {
      name: 'Playstation Manager',
      translateKey: 'userManagement.roleGroups.playstationManager',
      authorities: [
        'ROLE_PLAYSTATION_VIEW',
        'ROLE_VIEW_PLAYSTATION_SESSION',
        'ROLE_START_PLAYSTATION_SESSION',
        'ROLE_STOP_PLAYSTATION_SESSION',
        'ROLE_UPDATE_PLAYSTATION_SESSION',
        'ROLE_MOVE_PLAYSTATION_DEVICE',
        'ROLE_CHANGE_PLAYSTATION_TYPE',
        'ROLE_MANAGE_PLAYSTATION_ORDERS',
        'ROLE_VIEW_PLAYSTATION_TYPE',
        'ROLE_VIEW_ACTIVE_SHEFT',
        'ROLE_VIEW_SHEFT',
        'ROLE_VIEW_ITEM',
        'ROLE_VIEW_ACCOUNT',
        'ROLE_VIEW_BANK',
        'ROLE_VIEW_STORE',
        'ROLE_VIEW_INVOICE',
        'ROLE_VIEW_PLAYSTATION_DEVICE'
      ]
    },
    {
      name: 'Financial Manager',
      translateKey: 'userManagement.roleGroups.financialManager',
      authorities: [
        'ROLE_CREATE_INVOICE',
        'ROLE_UPDATE_INVOICE',
        'ROLE_VIEW_INVOICE',
        'ROLE_VIEW_PAYMENT',
        'ROLE_VIEW_BANK',
        'ROLE_VIEW_STORE',
        'ROLE_VIEW_ACCOUNT',
        'ROLE_VIEW_ITEM',
        'ROLE_CREATE_PAYMENT',
        'ROLE_CREATE_SALE',
        'ROLE_CREATE_PURCHASE',
        'ROLE_UPDATE_PAYMENT',
        'ROLE_MANAGE_FINANCE',
        'ROLE_VIEW_SHEFT',
        'ROLE_VIEW_ACTIVE_SHEFT',
        'ROLE_SAVE_INVOICE',
        'ROLE_UPDATE_SALE',
        'ROLE_UPDATE_PURCHASE'
      ]
    },
    {
      name: 'SALES MANAGER',
      translateKey: 'userManagement.roleGroups.salesManager',
      authorities: [
        'ROLE_CREATE_SALE',
        'ROLE_CREATE_PURCHASE',
        'ROLE_CREATE_INVOICE',
        'ROLE_UPDATE_INVOICE',
        'ROLE_VIEW_INVOICE',
        'ROLE_VIEW_BANK',
        'ROLE_VIEW_STORE',
        'ROLE_VIEW_ACCOUNT',
        'ROLE_VIEW_ITEM',
         'ROLE_UPDATE_SALE',
        'ROLE_UPDATE_PURCHASE'
      ]
    },
    {
      name: 'Inventory Manager',
      translateKey: 'userManagement.roleGroups.inventoryManager',
      authorities: [
        'ROLE_VIEW_ITEM',
        'ROLE_VIEW_ACCOUNT',
        'ROLE_VIEW_BANK',
        'ROLE_VIEW_STORE',
        'ROLE_VIEW_INVOICE',
        'ROLE_CREATE_ITEM',
        'ROLE_UPDATE_ITEM',
        'ROLE_DELETE_ITEM',
        'ROLE_CREATE_STORE',
        'ROLE_UPDATE_STORE',
        'ROLE_DELETE_STORE',
      ]
    }
  ];

  constructor(
    private userService: UserManagementService,
    private route: ActivatedRoute,
    private translateService: TranslateService,
    fb: FormBuilder
  ) {
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
    // Define category order
    const categoryOrder = [
      'Basic Roles',
      'User Management',
      'Invoice Management',
      'Store Management',
      'Item Management',
      'Account Management',
      'Bank Management',
      'Payment Management',
      'Sales Management',
      'Purchase Management',
      'Finance Management',
      'Playstation Device Management',
      'Playstation Session Management',
      'Playstation Type Management',
      'Playstation Device Operations',
      'Shift Management',
      'Settings Management',
      'Management Roles',
      'OTHER'
    ];

    // Group authorities by category
    const groupedByCategory = authorities.reduce((groups: { [key: string]: IAuthority[] }, auth) => {
      const category = auth.category || 'OTHER';
      if (!groups[category]) {
        groups[category] = [];
      }
      
      // Enhance authority with translated name and description
      const enhancedAuth = {
        ...auth,
        name: this.translateService.instant(`userManagement.authorities.roles.${auth.id}.name`),
        description: this.translateService.instant(`userManagement.authorities.roles.${auth.id}.description`),
      };
      
      groups[category].push(enhancedAuth);
      return groups;
    }, {});

    // Convert to array format and sort by predefined category order
    this.authorityGroups = categoryOrder
      .filter(category => groupedByCategory[category]?.length > 0)
      .map(category => ({
        category: this.translateService.instant(`userManagement.authorities.categories.${category.replace(/\s+/g, '_')}`),
        authorities: groupedByCategory[category],
      }));
  }

  private getCategoryFromRole(role: string): string {
    // Make the role check more precise by checking for exact role prefixes
    if (role.startsWith('ROLE_ADMIN')) return 'ADMIN';
    if (role.startsWith('ROLE_USER')) return 'USER';
    if (role.startsWith('ROLE_INVENTORY')) return 'INVENTORY';
    if (role.startsWith('ROLE_SALES')) return 'SALES';
    if (role.startsWith('ROLE_PURCHASES')) return 'PURCHASES';
    if (role.startsWith('ROLE_ACCOUNTS')) return 'ACCOUNTS';
    if (role.startsWith('ROLE_REPORTS')) return 'REPORTS';
    if (role.startsWith('ROLE_SETTINGS')) return 'SETTINGS';
    return 'OTHER';
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

  applyRoleGroup(group: RoleGroup): void {
    const currentAuthorities = new Set(this.editForm.get('authorities')?.value || []);
    
    currentAuthorities.add('ROLE_USER');
    // Add all authorities from the group
    group.authorities.forEach(auth => currentAuthorities.add(auth));
    
    // Update the form control
    this.editForm.patchValue({
      authorities: Array.from(currentAuthorities)
    });
  }
}
