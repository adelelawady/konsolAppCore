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
  @Input() set item(value: ItemViewDTO | undefined) {
    if (value) {
      this._item = { ...value };
      this.isEditMode = !!value.id;
    } else {
      this._item = this.getDefaultItem();
      this.isEditMode = false;
    }
  }
  get item(): ItemViewDTO {
    return this._item;
  }

  @Output() showChange = new EventEmitter<boolean>();
  @Output() saved = new EventEmitter<void>();

  private _item: ItemViewDTO = this.getDefaultItem();
  loading = false;
  isEditMode = false;

  constructor(private itemService: ItemResourceService, private toastr: ToastrService, private translateService: TranslateService) {}

  ngOnInit(): void {}

  private getDefaultItem(): ItemViewDTO {
    return {
      name: '',
      barcode: '',
      category: '',
      price1: '0',
      cost: 0,
      qty: 0,
      checkQty: false,
    };
  }

  close(): void {
    this.show = false;
    this.showChange.emit(false);
  }

  saveItem(): void {
    if (this.loading) return;

    this.loading = true;
    const operation =
      this.isEditMode && this.item.id ? this.itemService.updateItem(this.item, this.item.id) : this.itemService.createItem(this.item);

    operation
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe({
        next: () => {
          this.toastr.success(
            this.translateService.instant(this.isEditMode ? 'products.messages.updateSuccess' : 'products.messages.createSuccess'),
            this.translateService.instant('global.messages.success')
          );
          this.saved.emit();
          this.close();
        },
        error: (error: any) => {
          console.error('Error saving item:', error);
          this.toastr.error(
            this.translateService.instant(this.isEditMode ? 'products.messages.updateError' : 'products.messages.createError'),
            this.translateService.instant('global.messages.error')
          );
        },
      });
  }
}
