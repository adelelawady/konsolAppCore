import { Component, OnInit } from '@angular/core';
import { Device } from '../device-card/device-card.component';

@Component({
  selector: 'jhi-device-list',
  templateUrl: './device-list.component.html',
  styleUrls: ['./device-list.component.scss']
})
export class DeviceListComponent implements OnInit {
  devices: Device[] = [
    {
      id: 1,
      name: 'PS4 Station 1',
      roomName: 'ROOM 1',
      deviceType: 'PS4',
      status: 'maintenance',
      hourlyRate: 100,
      imageUrl: 'content/images/ps4.jpg'
    },
    {
      id: 2,
      name: 'PS5 Station 1',
      roomName: 'ROOM 2',
      deviceType: 'PS5',
      status: 'in-use',
      duration: 45,
      cost: 150,
      hourlyRate: 150,
      imageUrl: 'content/images/ps5.jpg'
    }
  ];

  constructor() {}

  ngOnInit(): void {}
}
