import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsComponent } from './products.component';
import { ProductDetailsComponent } from './details/product-details.component';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot } from '@angular/router';
import { ItemResourceService } from '../../core/konsolApi/api/api';

const routes: Routes = [
  {
    path: '',
    component: ProductsComponent,
  },
  {
    path: ':id/details',
    component: ProductDetailsComponent,
    resolve: {
      item: (route: ActivatedRouteSnapshot) => {
        const id = route.paramMap.get('id');
        if (!id) {
          throw new Error('No ID provided');
        }
        return inject(ItemResourceService).getItem(id);
      },
    },
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class ProductsRoutingModule {}
