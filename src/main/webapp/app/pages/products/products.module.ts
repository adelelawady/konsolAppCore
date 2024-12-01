import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { SharedModule } from 'app/shared/shared.module';
import { ProductsComponent } from './products.component';
import { AddProductComponent } from './add-product/add-product.component';

@NgModule({
  declarations: [ProductsComponent, AddProductComponent],
  imports: [CommonModule, FormsModule, TranslateModule, SharedModule],
})
export class ProductsModule {}
