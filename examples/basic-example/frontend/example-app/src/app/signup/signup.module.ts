import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SignupRoutingModule } from './signup-routing.module';
import { SignupComponent } from './signup.component';
import { LinkExpireComponent } from '../invitation/link-expire/link-expire.component';


@NgModule({
  declarations: [
    SignupComponent,
     LinkExpireComponent
  ],
  imports: [
    CommonModule,
    SignupRoutingModule
  ]
})
export class SignupModule { }
