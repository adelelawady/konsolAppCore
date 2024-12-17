import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { InvoiceItemDTO } from 'app/core/konsolApi/model/invoiceItemDTO';
import { faSpinner, faArrowLeft, faBox } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'jhi-session-invoice-view',
  templateUrl: './session-invoice-view.component.html',
  styleUrls: ['./session-invoice-view.component.scss']
})
export class SessionInvoiceViewComponent implements OnInit {
  session?: PsSessionDTO;
  isLoading = false;

  // Icons
  faSpinner = faSpinner;
  faArrowLeft = faArrowLeft;
  faBox = faBox;

  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      if (data['session']) {
        this.session = data['session'];
      } else {
        this.back();
      }
    });
  }

  hasInvoiceItems(): boolean {
    return this.session?.invoice?.invoiceItems !== undefined;
  }

  getInvoiceItemsArray(): InvoiceItemDTO[] {
    return this.session?.invoice?.invoiceItems ? Array.from(this.session.invoice.invoiceItems) : [];
  }

  getInvoiceItemsCount(): number {
    return this.getInvoiceItemsArray().length;
  }

  calculateItemsSubtotal(): number {
    return this.getInvoiceItemsArray().reduce((total, item) => {
      return total + (item.netPrice || 0);
    }, 0);
  }

  back(): void {
    this.router.navigate(['/','playstation','sessions']);
  }
} 