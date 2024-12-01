import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';
import { finalize } from 'rxjs';
import { ItemViewDTO } from 'app/core/konsolApi/model/itemViewDTO';

@Component({
  selector: 'app-add-product',
  templateUrl: './add-product.component.html',
  styleUrls: ['./add-product.component.scss'],
})
export class AddProductComponent implements OnInit {
  @Input() show = false;
  @Input() item?: ItemViewDTO;
  @Output() showChange = new EventEmitter<boolean>();
  @Output() saved = new EventEmitter<void>();

  loading = false;
  isEditMode = false;

  constructor(private itemService: ItemResourceService, private toastr: ToastrService, private translateService: TranslateService) {}

  ngOnInit(): void {
    this.isEditMode = !!this.item?.id;
    if (!this.item) {
      this.item = {
        name: '',
        barcode: '',
        category: '',
        price1: 0,
        cost: 0,
        qty: 0,
        checkQty: false,
      } as any;
    }
  }

  close(): void {
    this.show = false;
    this.showChange.emit(false);
  }

  saveItem(): void {
    if (!this.item || this.loading) return;

    this.loading = true;
    const operation = this.item.id ? this.itemService.updateItem(this.item, this.item.id) : this.itemService.createItem(this.item);

    operation
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe({
        next: () => {
          this.toastr.success(
            this.translateService.instant(this.isEditMode ? 'products.update.success' : 'products.create.success'),
            this.translateService.instant('success.title')
          );
          this.saved.emit();
          this.close();
        },
        error: (error: any) => {
          console.error('Error saving item:', error);
          this.toastr.error(
            this.translateService.instant(this.isEditMode ? 'products.update.error' : 'products.create.error'),
            this.translateService.instant('error.title')
          );
        },
      });
  }
}
