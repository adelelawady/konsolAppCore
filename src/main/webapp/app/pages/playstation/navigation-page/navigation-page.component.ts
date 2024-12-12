import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-navigation-page',
  templateUrl: './navigation-page.component.html',
  styleUrls: ['./navigation-page.component.scss'],
})
export class NavigationPageComponent {
  playstationGames = [
    { title: 'God of War Ragnarök', image: 'god-of-war-ragnarok.jpg' },
    { title: 'Horizon Forbidden West', image: 'horizon-forbidden-west.jpg' },
    { title: 'Spider-Man: Miles Morales', image: 'spider-man-miles-morales.jpg' },
    { title: 'Ghost of Tsushima', image: 'ghost-of-tsushima.jpg' },
    { title: 'The Last of Us Part II', image: 'the-last-of-us-part-ii.jpg' },
    { title: 'Ratchet & Clank: Rift Apart', image: 'ratchet-clank-rift-apart.jpg' },
    { title: 'Returnal', image: 'returnal.jpg' },
    { title: "Demon's Souls", image: 'demons-souls.jpg' },
    { title: 'Sackboy: A Big Adventure', image: 'sackboy-a-big-adventure.jpg' },
    { title: "Marvel's Spider-Man: Remastered", image: 'marvels-spider-man-remastered.jpg' },
  ];

  constructor() {
    // Empty constructor
  }
}
