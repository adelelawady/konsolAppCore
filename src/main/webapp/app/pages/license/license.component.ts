import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PublicResourceService } from 'app/core/konsolApi/api/publicResource.service';
import { ProcessSuperAdminLicenseRequest } from 'app/core/konsolApi/model/processSuperAdminLicenseRequest';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-license',
  templateUrl: './license.component.html',
  styleUrls: ['./license.component.scss']
})
export class LicenseComponent implements OnInit {
  licenseKey: string = '';
  loading = false;
  errorMessage: string | null = null;

  constructor(
    private publicResourceService: PublicResourceService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.validateExistingLicense();
  }

  private validateExistingLicense(): void {
    this.loading = true;
    this.publicResourceService.validateLicensePublic()
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (isValid) => {
          if (isValid) {
            // If license is valid, redirect to login page
            this.router.navigate(['/login']);
          }
        },
        error: (error) => {
          console.error('License validation error:', error);
          // Don't show error message on initial load as this is expected
          // when license is not valid
        }
      });
  }

  onSubmit(): void {
    if (!this.licenseKey.trim()) {
      this.errorMessage = 'license.error.required';
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    const request: ProcessSuperAdminLicenseRequest = {
      encryptedKey: this.licenseKey
    };

    this.publicResourceService.processLicensePublic(request)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (response) => {
          // License successfully activated
         this.validateExistingLicense();
        },
        error: (error) => {
          console.error('License activation error:', error);
          if (error.status === 401) {
            this.errorMessage = 'license.error.invalid';
          } else if (error.error?.detail) {
            this.errorMessage = error.error.detail;
          } else {
            this.errorMessage = 'license.error.generic';
          }
        }
      });
  }
} 