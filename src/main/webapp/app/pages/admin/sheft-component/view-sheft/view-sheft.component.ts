import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AccountService } from 'app/core/auth/account.service';
import { SheftResourceService } from 'app/core/konsolApi/api/sheftResource.service';
import { SheftDTO } from 'app/core/konsolApi/model/sheftDTO';

@Component({
  selector: 'app-view-sheft',
  templateUrl: './view-sheft.component.html',
  styleUrls: ['./view-sheft.component.css'],
})
export class ViewSheftComponent implements OnInit {
  sheft?: SheftDTO;
  loading = false;
  error = false;

  constructor(private route: ActivatedRoute, private router: Router, private sheftService: SheftResourceService, private userService: AccountService) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      if (params['id']) {
        this.loadSheft(params['id']);
      }
    });
  }

  loadSheft(id: string): void {
    this.loading = true;
    this.error = false;
    this.sheftService.getSheft(id).subscribe({
      next: data => {
        this.sheft = data;
        this.loading = false;
      },
      error: error => {
        console.error('Error loading sheft:', error);
        this.error = true;
        this.loading = false;
      },
    });
  }

  goBack(): void {
    window.history.back();
  }
  hasAuthority(authority: string): boolean {
    return this.userService.hasAnyAuthority([authority]);
  } 
}
