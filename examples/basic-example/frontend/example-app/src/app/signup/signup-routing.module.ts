import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SignupComponent } from './signup.component';
import { LinkExpireComponent } from '../invitation/link-expire/link-expire.component';

const routes: Routes = [
  {
    path: '',
    component: SignupComponent,
  }, {
    path: 'link-expire',
    component: LinkExpireComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class SignupRoutingModule { }
