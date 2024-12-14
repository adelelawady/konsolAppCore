import { Component, OnInit } from '@angular/core';
import { Device } from '../components/device-card/device-card.component';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  devices: Device[] = [
    {
      id: 1,
      name: 'PlayStation 5 - Station 1',
      status: 'available',
      hourlyRate: 150,
      imageUrl: 'content/images/ps5.jpg'
    },
    {
      id: 2,
      name: 'PlayStation 5 - Station 2',
      status: 'in-use',
      hourlyRate: 150,
      currentSession: {
        startTime: '10:00 AM',
        duration: 90,
        cost: 225
      },
      imageUrl: 'content/images/ps5.jpg'
    },
    {
      id: 3,
      name: 'PlayStation 4 Pro - Station 1',
      status: 'available',
      hourlyRate: 100,
      imageUrl: 'content/images/ps4.jpg'
    },
    {
      id: 4,
      name: 'PlayStation 4 Pro - Station 2',
      status: 'maintenance',
      hourlyRate: 100,
      imageUrl: 'content/images/ps4.jpg'
    }
  ];

  constructor() {}

  ngOnInit(): void {}
}
