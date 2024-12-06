import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ItemResourceService } from 'app/core/konsolApi/api/itemResource.service';
import { ItemAnalysisDTO } from 'app/core/konsolApi/model/itemAnalysisDTO';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css'],
})
export class ProductDetailsComponent implements OnInit {
  productAnalysis: ItemAnalysisDTO | null = null;

  constructor(private route: ActivatedRoute, private itemResourceService: ItemResourceService) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.itemResourceService.getItemsAnalysis(productId).subscribe(
        response => {
          this.productAnalysis = response.body;
        },
        error => {
          console.error('Error fetching product analysis', error);
        }
      );
    }
  }
}
