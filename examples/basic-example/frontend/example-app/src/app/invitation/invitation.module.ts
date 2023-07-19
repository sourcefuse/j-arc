import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { InvitationRoutingModule } from './invitation-routing.module';
import { InviteUserComponent } from './invite-user/invite-user.component';
import { LinkExpireComponent } from './link-expire/link-expire.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
 import { MatButtonToggleModule } from '@angular/material/button-toggle';


@NgModule({
  declarations: [
    InviteUserComponent,
    LinkExpireComponent
  ],
  imports: [
    CommonModule,
    InvitationRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule, 
    MatButtonToggleModule
  ]
})
export class InvitationModule { }
