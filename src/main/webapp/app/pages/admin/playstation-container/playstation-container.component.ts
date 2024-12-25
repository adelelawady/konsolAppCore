import { Component, OnInit, inject } from '@angular/core';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-admin-playstation-container',
  templateUrl: './playstation-container.component.html',
  styleUrls: ['./playstation-container.component.scss'],
})
export class AdminPlaystationContainerComponent implements OnInit {
  containers: PlaystationContainer[] = [];
  isLoading = false;
  editForm: FormGroup;
  selectedContainer: any | null = null;
  isEditMode = false;

  private playstationContainerService = inject(PlaystationContainerResourceService);
  private modalService = inject(NgbModal);
  private fb = inject(FormBuilder);

  constructor() {
    this.editForm = this.fb.group({
      name: ['', [Validators.required]],
      category: ['', [Validators.required]],
      defaultIcon: [''],
      hasTimeManagement: [false, [Validators.required]],
      showType: [false, [Validators.required]],
      showTime: [false, [Validators.required]],
      canMoveDevice: [false, [Validators.required]],
      canHaveMultiTimeManagement: [false, [Validators.required]],
      acceptedOrderCategories: ['', [Validators.required]],
      orderSelectedPriceCategory: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.loadContainers();
  }

  loadContainers(): void {
    this.isLoading = true;
    this.playstationContainerService.getPlaystationContainers().subscribe({
      next: (response: PlaystationContainer[]) => {
        this.containers = response;
        this.isLoading = false;
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  openCreateModal(content: any): void {
    this.isEditMode = false;
    this.selectedContainer = null;
    this.editForm.reset();
    this.modalService.open(content, { size: 'lg' });
  }

  openEditModal(content: any, container: PlaystationContainer): void {
    this.isEditMode = true;
    this.selectedContainer = container;
    this.editForm.patchValue(container);
    this.modalService.open(content, { size: 'lg' });
  }

  save(): void {
    if (this.editForm.valid) {
      const formData = this.editForm.value;

      if (this.isEditMode && this.selectedContainer) {
        const updatedContainer = { ...this.selectedContainer, ...formData };
        this.playstationContainerService.updatePlaystationContainer(this.selectedContainer.id, updatedContainer).subscribe(() => {
          this.modalService.dismissAll();
          this.loadContainers();
        });
      } else {
        this.playstationContainerService.createPlaystationContainer(formData).subscribe(() => {
          this.modalService.dismissAll();
          this.loadContainers();
        });
      }
    }
  }

  delete(container: any): void {
    if (confirm('Are you sure you want to delete this container?')) {
      this.playstationContainerService.deletePlaystationContainer(container.id).subscribe(() => {
        this.loadContainers();
      });
    }
  }
}
