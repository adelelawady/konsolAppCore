import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { finalize } from 'rxjs/operators';
import { ItemResourceService } from '../../../core/konsolApi/api/itemResource.service';
import { ItemDTO, ItemAnalysisDTO, ItemAnalysisSearchDTO } from '../../../core/konsolApi';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
})
export class ProductDetailsComponent implements OnInit {
  product?: ItemDTO;
  productAnalysis?: ItemAnalysisDTO;
  isLoading = false;

  constructor(
    private route: ActivatedRoute,
    private itemService: ItemResourceService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.product = data['item'];
      if (this.product?.id) {
        this.loadProductAnalysis(this.product.id);
      }
    });
  }

  private loadProductAnalysis(id: string): void {
    this.isLoading = true;
    
    const searchDTO: ItemAnalysisSearchDTO = {
      itemId: id,
      // You can optionally add these parameters
      // storeId: undefined,
      // startDate: undefined,
      // endDate: undefined
    };

    this.itemService.getItemsAnalysis(id,searchDTO)
      .pipe(finalize(() => this.isLoading = false))
      .subscribe({
        next: (analysis: ItemAnalysisDTO) => {
          this.productAnalysis = analysis;
        },
        error: (error: any) => {
          console.error('Error loading product analysis:', error);
        }
      });
  }
} 