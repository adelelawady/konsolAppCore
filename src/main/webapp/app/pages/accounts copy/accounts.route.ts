import { Routes } from '@angular/router';
import { AccountsComponent } from './accounts.component';

export const accountsRoute: Routes = [
  {
    path: '',
    component: AccountsComponent,
    data: {
      pageTitle: 'accounts.title',
    },
  },
];
