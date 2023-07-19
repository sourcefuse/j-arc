import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [

  {
    path: 'login',
    loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
    canActivate: [],// add route gaurd to check if user is logeed in if not then proceed
  },

  {
    path: 'signup',
    loadChildren: () => import('./signup/signup.module').then(m => m.SignupModule),
    canActivate: [],// add route gaurd to check if user is logeed in if yes then proceed
  },

  {
    path: 'invitation',
    loadChildren: () => import('./invitation/invitation.module').then(m => m.InvitationModule),
    canActivate: [],// add route gaurd to check if user is logeed in if yes then proceed
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
