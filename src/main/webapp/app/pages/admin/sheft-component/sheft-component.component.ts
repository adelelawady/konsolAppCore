import { Component, OnInit } from '@angular/core';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';
import { SheftDTO } from 'app/core/konsolApi/model/sheftDTO';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sheft-component',
  templateUrl: './sheft-component.component.html',
  styleUrls: ['./sheft-component.component.css'],
})
export class SheftComponentComponent implements OnInit {
  shefts: SheftDTO[] = [];
  loading = false;
  page = 0;
  size = 10;
  totalItems = 0;
  totalPages = 0;
  pageNumbers: number[] = [];
  protected readonly Math = Math;

  constructor(private sheftService: SheftResourceService, private router: Router) {}

  ngOnInit(): void {
    this.loadShefts();
  }

  loadShefts(): void {
    this.loading = true;
    this.sheftService.getAllShefts(this.page, this.size, 'response').subscribe({
      next: response => {
        if (response.body) {
          this.shefts = response.body;
        }

        const totalCountHeader = response.headers.get('X-Total-Count');
        this.totalItems = totalCountHeader ? parseInt(totalCountHeader, 10) : 0;

        this.totalPages = Math.ceil(this.totalItems / this.size);
        this.generatePageNumbers();
        this.loading = false;
      },
      error: error => {
        console.error('Error loading shefts:', error);
        this.loading = false;
      },
    });
  }

  generatePageNumbers(): void {
    this.pageNumbers = Array.from({ length: this.totalPages }, (_, i) => i);
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages && page !== this.page) {
      this.page = page;
      this.loadShefts();
    }
  }

  nextPage(): void {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.loadShefts();
    }
  }

  previousPage(): void {
    if (this.page > 0) {
      this.page--;
      this.loadShefts();
    }
  }

  deleteSheft(id: string): void {
    if (confirm('Are you sure you want to delete this sheft?')) {
      this.loading = true;
      this.sheftService.deleteSheft(id).subscribe({
        next: () => {
          this.loadShefts();
          this.loading = false;
        },
        error: error => {
          console.error('Error deleting sheft:', error);
          this.loading = false;
        },
      });
    }
  }

  viewSheft(id: string): void {
    this.router.navigate(['/shefts', id]);
  }

  editSheft(sheft: SheftDTO): void {
    // This will be implemented later with a dialog/modal
    console.log('Edit sheft:', sheft);
  }

  createNewSheft(): void {
    // This will be implemented later with a dialog/modal
    console.log('Create new sheft');
  }
}
