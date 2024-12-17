import { Component, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirmation-modal',
  template: `
    <div class="modal-header">
      <h4 class="modal-title">Confirm Action</h4>
      <button type="button" class="btn-close" aria-label="Close" (click)="modal.dismiss()"></button>
    </div>
    <div class="modal-body">
      <p>{{ message }}</p>
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" (click)="modal.dismiss()">Cancel</button>
      <button type="button" class="btn btn-primary" (click)="modal.close('confirm')">Confirm</button>
    </div>
  `
})
export class ConfirmationModalComponent {
  @Input() message: string = '';
  
  constructor(public modal: NgbActiveModal) {}
} 