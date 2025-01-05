import { Component, OnInit } from '@angular/core';
import { VERSION } from '../../app.constants';
import { PublicResourceService } from 'app/core/konsolApi/api/publicResource.service';
import { LicenseDTO } from 'app/core/konsolApi/model/licenseDTO';

@Component({
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss'],
})
export class FooterComponent implements OnInit {
  version: string;
  currentYear: number;

  license: LicenseDTO | undefined;
  constructor(private publicResourceService: PublicResourceService) {
    this.version = VERSION;
    this.currentYear = new Date().getFullYear();
  }

  ngOnInit(): void {
    this.getUserLicense();
  }

  getUserLicense(): void {
    this.publicResourceService.getUserLicense().subscribe(license => {
      this.license = license;
    });
  }
}
