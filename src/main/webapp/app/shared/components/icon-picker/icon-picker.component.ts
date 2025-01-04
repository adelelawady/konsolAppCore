import { Component, Input, Output, EventEmitter, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-icon-picker',
  template: `
    <div class="icon-picker-wrapper">
      <div class="input-group">
        <input type="text" class="form-control" [value]="value" (input)="onInputChange($event)" [placeholder]="placeholder" />
        <button class="btn btn-outline-secondary" type="button" (click)="togglePicker()">
          <i class="ti ti-icons"></i>
        </button>
      </div>

      <div class="icon-picker-dropdown card" *ngIf="isOpen">
        <div class="card-body">
          <input type="text" class="form-control mb-3" [(ngModel)]="searchTerm" placeholder="Search icons..." />
          <div class="icon-grid">
            <button *ngFor="let icon of filteredIcons" class="btn btn-outline-secondary btn-icon" (click)="selectIcon(icon)" [title]="icon">
              <i [class]="icon"></i>
            </button>
          </div>
          <div *ngIf="!searchTerm && displayIcons.length < commonIcons.length" class="text-center mt-2">
            <button class="btn btn-link" (click)="loadMore()">Load More Icons</button>
          </div>
          <div class="mt-3 text-muted small">
            <i class="fa fa-info-circle"></i> Find more free icons at
            <a href="https://fontawesome.com/v6/search?o=r&m=free" target="_blank">FontAwesome</a>
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [
    `
      .icon-picker-wrapper {
        position: relative;
      }
      .icon-picker-dropdown {
        position: absolute;
        top: 100%;
        left: 0;
        right: 0;
        z-index: 1000;
        margin-top: 0.5rem;
      }
      .icon-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(40px, 1fr));
        gap: 0.5rem;
        max-height: 200px;
        overflow-y: auto;
      }
      .btn-icon {
        width: 40px;
        height: 40px;
        padding: 0;
        display: flex;
        align-items: center;
        justify-content: center;
      }
    `,
  ],
})
export class IconPickerComponent implements OnInit {
  @Input() value: string = '';
  @Input() placeholder: string = '';
  @Output() onIconChange = new EventEmitter<string>();
  @Output() onChange = new EventEmitter<string>();
  isOpen = false;
  searchTerm = '';
  commonIcons: string[] = [];

  // Keep a cached subset of icons for initial display
  displayIcons: string[] = [];
  private readonly INITIAL_ICONS_COUNT = 100;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // Load icons asynchronously
    this.loadIcons();
  }

  private async loadIcons() {
    try {
      const data: any = await this.http.get('content/icons/fontawesome-v6.4.2-free.json').toPromise();
      // Combine all icon types with their prefixes
      this.commonIcons = [
        ...data.solid.map((icon: string) => `fa ${icon}`),
        ...data.regular.map((icon: string) => `fa-regular ${icon}`),
        ...data.brands.map((icon: string) => `fa-brands ${icon}`),
      ];

      // Initialize display icons with a subset
      this.displayIcons = this.commonIcons.slice(0, this.INITIAL_ICONS_COUNT);
    } catch (error) {
      console.error('Error loading icons:', error);
      // Fallback to a few basic icons in case of error
      this.commonIcons = ['fa fa-user', 'fa fa-home', 'fa fa-cog'];
      this.displayIcons = this.commonIcons;
    }
  }

  get filteredIcons() {
    if (this.searchTerm) {
      // When searching, search through all icons
      return this.commonIcons.filter(icon => icon.toLowerCase().includes(this.searchTerm.toLowerCase()));
    }
    // Otherwise return the smaller display set
    return this.displayIcons;
  }

  togglePicker() {
    this.isOpen = !this.isOpen;
  }

  selectIcon(icon: string): void {
    this.value = icon;
    this.onIconChange.emit(icon);
    this.isOpen = false;
  }

  onInputChange(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.value = value;
    this.onChange.emit(value);
  }

  loadMore() {
    const currentLength = this.displayIcons.length;
    const nextBatch = this.commonIcons.slice(currentLength, currentLength + this.INITIAL_ICONS_COUNT);
    this.displayIcons = [...this.displayIcons, ...nextBatch];
  }
}
