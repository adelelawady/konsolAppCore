import { Component, OnInit } from '@angular/core';
import { VERSION } from '../../app.constants';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {
  version: string;
  currentYear: number;

  constructor() {
    this.version = VERSION;
    this.currentYear = new Date().getFullYear();
  }

  ngOnInit(): void {
    // Any initialization logic if needed
  }
}
