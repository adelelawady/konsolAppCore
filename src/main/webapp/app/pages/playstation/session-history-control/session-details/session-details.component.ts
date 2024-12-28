import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { faClock, faArrowLeft, faGamepad, faMoneyBill, faShoppingCart, faCalendar } from '@fortawesome/free-solid-svg-icons';

import { PsSessionDTO } from 'app/core/konsolApi/model/psSessionDTO';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { PlaystationResourceService } from 'app/core/konsolApi';

@Component({
  selector: 'jhi-session-details',
  templateUrl: './session-details.component.html',
  styleUrls: ['./session-details.component.scss'],
})
export class SessionDetailsComponent implements OnInit {
  session: PsSessionDTO | null = null;
  isLoading = false;
  faClock = faClock;
  faArrowLeft = faArrowLeft;
  faGamepad = faGamepad;
  faMoneyBill = faMoneyBill;
  faShoppingCart = faShoppingCart;
  faCalendar = faCalendar;

  constructor(
    private route: ActivatedRoute,
    private location: Location,
    private playstationResourceService: PlaystationResourceService,
    private toastr: ToastrService,
    private translateService: TranslateService
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.session = data['session'];
    });
  }

  getDuration(session: PsSessionDTO): string {
    if (!session.startTime || !session.endTime) {
      return '0h 0m';
    }
    const start = new Date(session.startTime);
    const end = new Date(session.endTime);
    const durationMs = end.getTime() - start.getTime();
    const hours = Math.floor(durationMs / (1000 * 60 * 60));
    const minutes = Math.floor((durationMs % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours}h ${minutes}m`;
  }

  goBack(): void {
    this.location.back();
  }
}
