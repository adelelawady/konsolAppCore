import { Component, OnInit, inject } from '@angular/core';
import { PlaystationContainerResourceService } from 'app/core/konsolApi/api/playstationContainerResource.service';
import { PlaystationContainer } from 'app/core/konsolApi/model/playstationContainer';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { ItemResourceService, CategoryItem } from 'app/core/konsolApi';

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
  availableCategories: CategoryItem[] = [];

  private playstationContainerService = inject(PlaystationContainerResourceService);
  private itemResourceService = inject(ItemResourceService);
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
      acceptedOrderCategories: [[], [Validators.required]],
      orderSelectedPriceCategory: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.loadContainers();
    this.loadCategories();
  }

  loadCategories(): void {
    this.itemResourceService.getAllItemsCategories().subscribe({
      next: categories => {
        this.availableCategories = categories;
      },
      error(error) {
        console.error('Error loading categories:', error);
      },
    });
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

    // eslint-disable-next-line @typescript-eslint/no-unnecessary-condition
    const acceptedCategories = container.acceptedOrderCategories || [];

    // Find matching CategoryItems from availableCategories
    const selectedCategories = acceptedCategories.filter(category => this.availableCategories.some(catItem => catItem.name === category));

    this.editForm.patchValue({
      ...container,
      acceptedOrderCategories: selectedCategories,
    });
    this.modalService.open(content, { size: 'lg' });
  }

  save(): void {
    if (this.editForm.valid) {
      const formData = this.editForm.value;

      const processedFormData = {
        ...formData,
        acceptedOrderCategories: formData.acceptedOrderCategories,
      };

      if (this.isEditMode && this.selectedContainer) {
        const updatedContainer = { ...this.selectedContainer, ...processedFormData };
        this.playstationContainerService.updatePlaystationContainer(this.selectedContainer.id, updatedContainer).subscribe(() => {
          this.modalService.dismissAll();
          this.loadContainers();
          window.location.reload();
        });
      } else {
        this.playstationContainerService.createPlaystationContainer(processedFormData).subscribe(() => {
          this.modalService.dismissAll();
          this.loadContainers();
          window.location.reload();
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

  onTagsChanged(event: any): void {
    this.editForm.patchValue({
      acceptedOrderCategories: event,
    });
  }
}
