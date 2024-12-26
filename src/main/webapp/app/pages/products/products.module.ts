import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ProductsRoutingModule } from './products-routing.module';
import { ProductsComponent } from './products.component';
import { ProductDetailsComponent } from './details/product-details.component';
import { AddProductComponent } from './add-product/add-product.component';
import { Chart } from 'chart.js/auto';


@NgModule({
  imports: [SharedModule, ProductsRoutingModule],
  declarations: [ProductsComponent, AddProductComponent, ProductDetailsComponent],
  exports: [ProductsComponent, AddProductComponent],
})
export class ProductsModule {}
