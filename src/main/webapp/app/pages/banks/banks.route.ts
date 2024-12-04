import { Routes } from '@angular/router';
import { BanksComponent } from './list/banks.component';

export const banksRoute: Routes = [
  {
    path: '',
    component: BanksComponent,
    data: {
      pageTitle: 'banks.title',
    },
  },
];
