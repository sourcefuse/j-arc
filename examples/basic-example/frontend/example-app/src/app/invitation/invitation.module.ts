import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { InvitationRoutingModule } from './invitation-routing.module';
import { InviteUserComponent } from './invite-user/invite-user.component';
import { LinkExpireComponent } from './link-expire/link-expire.component';


@NgModule({
  declarations: [
    InviteUserComponent,
    LinkExpireComponent
  ],
  imports: [
    CommonModule,
    InvitationRoutingModule
  ]
})
export class InvitationModule { }
