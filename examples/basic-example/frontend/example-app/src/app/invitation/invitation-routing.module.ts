import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InviteUserComponent } from './invite-user/invite-user.component';
import { LinkExpireComponent } from './link-expire/link-expire.component';

const routes: Routes = [
  {
    path: 'invite-user',
    component: InviteUserComponent,
  },  {
    path: 'link-expire',
    component: LinkExpireComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class InvitationRoutingModule { }
