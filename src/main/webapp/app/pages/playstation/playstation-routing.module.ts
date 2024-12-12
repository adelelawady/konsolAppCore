import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';

const routes: Routes = [
  {
    path: '',
    component: NavigationPageComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PlaystationRoutingModule {}
