import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ControlValueAccessor } from '@angular/forms';

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
export class IconPickerComponent implements ControlValueAccessor {
  @Input() value: string = '';
  @Input() placeholder: string = '';
  @Output() onIconChange = new EventEmitter<string>();

  isOpen = false;
  searchTerm = '';
  disabled = false;

  onChange = (value: string) => {
    this.onIconChange.emit(value);
  };
  onTouched = () => {};

  writeValue(value: string): void {
    this.value = value;
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

  // This is a sample list of FontAwesome icons - you should expand this list
  commonIcons = [
    // General UI icons
    // Solid icons
    'fa fa-tv',
    'fa fa-gamepad',
    'fa fa-desktop',
    'fa fa-laptop',
    'fa fa-mobile',
    'fa fa-tablet',
    'fa fa-clock',
    'fa fa-users',
    'fa fa-user',
    'fa fa-cog',
    'fa fa-home',
    'fa fa-camera',
    'fa fa-bell',
    'fa fa-heart',
    'fa fa-envelope',
    'fa fa-search',
    'fa fa-star',
    'fa fa-trash',
    'fa fa-shopping-cart',
    'fa fa-comments',
    'fa fa-music',
    'fa fa-cloud',
    'fa fa-pen',

    'fa fa-envelope',
    'fa fa-search',
    'fa fa-key',
    'fa fa-lock',
    'fa fa-unlock',
    'fa fa-trash',
    'fa fa-edit',
    'fa fa-save',
    'fa fa-file',
    'fa fa-folder',
    'fa fa-folder-open',
    'fa fa-upload',
    'fa fa-download',
    'fa fa-print',
    'fa fa-clipboard',
    'fa fa-calendar',
    'fa fa-clock',
    'fa fa-check',
    'fa fa-times',
    'fa fa-plus',
    'fa fa-minus',
    'fa fa-bars',

    // Media and devices
    'fa fa-camera',
    'fa fa-video',
    'fa fa-music',
    'fa fa-headphones',
    'fa fa-tv',
    'fa fa-desktop',
    'fa fa-laptop',
    'fa fa-tablet',
    'fa fa-mobile',
    'fa fa-gamepad',
    'fa-brands fa-playstation',
    'fa-brands fa-xbox',
    'fa-brands fa-steam',

    // E-commerce and finance
    'fa fa-shopping-cart',
    'fa fa-credit-card',
    'fa fa-wallet',
    'fa fa-dollar-sign',
    'fa fa-euro-sign',
    'fa fa-gift',
    'fa fa-tags',
    'fa fa-receipt',
    'fa fa-chart-line',

    // Social and communication
    'fa fa-comments',
    'fa fa-comment',
    'fa fa-phone',
    'fa fa-address-book',
    'fa fa-paper-plane',
    'fa fa-at',

    // Health and fitness
    'fa fa-heart',
    'fa fa-stethoscope',
    'fa fa-hospital',
    'fa fa-pills',
    'fa fa-running',
    'fa fa-dumbbell',

    // Travel and transportation
    'fa fa-car',
    'fa fa-bus',
    'fa fa-plane',
    'fa fa-ship',
    'fa fa-map',
    'fa fa-route',
    'fa fa-compass',
    'fa fa-suitcase',

    // Nature and weather
    'fa fa-sun',
    'fa fa-cloud',
    'fa fa-rainbow',
    'fa fa-snowflake',
    'fa fa-leaf',
    'fa fa-tree',
    'fa fa-water',

    // Education and work
    'fa fa-book',
    'fa fa-graduation-cap',
    'fa fa-chalkboard',
    'fa fa-briefcase',
    'fa fa-lightbulb',
    'fa fa-tools',
    'fa fa-project-diagram',

    // Brand icons
    'fa-brands fa-facebook',
    'fa-brands fa-twitter',
    'fa-brands fa-instagram',
    'fa-brands fa-linkedin',
    'fa-brands fa-youtube',
    'fa-brands fa-github',
    'fa-brands fa-whatsapp',
    'fa-brands fa-snapchat',
    'fa-brands fa-tiktok',
    'fa-brands fa-google',
    'fa-brands fa-amazon',
    'fa-brands fa-reddit',
    'fa-brands fa-pinterest',
    'fa-brands fa-tumblr',
    'fa-brands fa-apple',
    'fa-brands fa-windows',
    'fa-brands fa-android',
    'fa-brands fa-spotify',
    'fa-brands fa-discord',
    'fa-brands fa-slack',
    'fa-brands fa-dropbox',
    'fa-brands fa-dribbble',
    'fa-brands fa-behance',
    'fa-brands fa-medium',
    'fa-brands fa-vimeo',
    // Add more icons as needed

    // Additional UI/UX icons
    'fa fa-sliders',
    'fa fa-toggle-on',
    'fa fa-toggle-off',
    'fa fa-spinner',
    'fa fa-circle-notch',
    'fa fa-sync',
    'fa fa-undo',
    'fa fa-redo',
    'fa fa-expand',
    'fa fa-compress',
    'fa fa-arrows-alt',
    'fa fa-link',
    'fa fa-unlink',
    'fa fa-external-link-alt',

    // Content/Text
    'fa fa-paragraph',
    'fa fa-heading',
    'fa fa-text-height',
    'fa fa-text-width',
    'fa fa-bold',
    'fa fa-italic',
    'fa fa-underline',
    'fa fa-strikethrough',
    'fa fa-list-ul',
    'fa fa-list-ol',
    'fa fa-quote-left',
    'fa fa-quote-right',

    // Files and Data
    'fa fa-database',
    'fa fa-server',
    'fa fa-hdd',
    'fa fa-sd-card',
    'fa fa-memory',
    'fa fa-microchip',
    'fa fa-code',
    'fa fa-terminal',
    'fa fa-bug',
    'fa fa-qrcode',
    'fa fa-barcode',

    // Additional Brand Icons
    'fa-brands fa-chrome',
    'fa-brands fa-firefox',
    'fa-brands fa-safari',
    'fa-brands fa-opera',
    'fa-brands fa-edge',
    'fa-brands fa-wordpress',
    'fa-brands fa-joomla',
    'fa-brands fa-drupal',
    'fa-brands fa-npm',
    'fa-brands fa-yarn',
    'fa-brands fa-docker',
    'fa-brands fa-aws',
    'fa-brands fa-angular',
    'fa-brands fa-react',
    'fa-brands fa-vuejs',
    'fa-brands fa-node',
    'fa-brands fa-php',
    'fa-brands fa-python',
    'fa-brands fa-java',

    // Charts and Analytics
    'fa fa-chart-bar',
    'fa fa-chart-pie',
    'fa fa-chart-area',
    'fa fa-analytics',
    'fa fa-percentage',
    'fa fa-poll',
    'fa fa-funnel-dollar',

    // Security
    'fa fa-shield-alt',
    'fa fa-user-shield',
    'fa fa-fingerprint',
    'fa fa-id-card',
    'fa fa-id-badge',
    'fa fa-user-secret',
    'fa fa-mask',

    // Additional Social
    'fa-brands fa-twitch',
    'fa-brands fa-telegram',
    'fa-brands fa-skype',
    'fa-brands fa-viber',
    'fa-brands fa-wechat',
    'fa-brands fa-line',
    'fa-brands fa-meetup',
    'fa-brands fa-stack-overflow',
    'fa-brands fa-dev',
    'fa-brands fa-gitlab',
    'fa-brands fa-bitbucket',
  ];

  get filteredIcons() {
    return this.commonIcons.filter(icon => icon.toLowerCase().includes(this.searchTerm.toLowerCase()));
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
    this.onChange(value);
  }
}
