import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { faSpinner, faClock } from '@fortawesome/free-solid-svg-icons';

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
  faClock = faClock;

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

  calculateItemsTotal(): number {
    return !this.session?.invoice?.netPrice ? 0 : this.session.invoice.netPrice;
  }

  hasInvoiceItems(): boolean {
    return this.session?.invoice?.invoiceItems !== undefined;
  }

  getInvoiceItemsArray(): any{
    return this.session?.invoice?.invoiceItems ;
  }

  back(): void {
    this.router.navigate(['/','playstation','sessions']);
  }
} 