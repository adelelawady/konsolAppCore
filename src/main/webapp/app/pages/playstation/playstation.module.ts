import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavigationPageComponent } from './navigation-page/navigation-page.component';
import { PlaystationRoutingModule } from './playstation-routing.module';

@NgModule({
  imports: [CommonModule, PlaystationRoutingModule],
  declarations: [NavigationPageComponent],
})
export class PlaystationModule {}
