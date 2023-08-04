import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VerifyInvitationComponent } from './invitation/verify-invitation/verify-invitation.component';
import { AuthGuard, LoggedInGuard } from './core/auth';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () =>
      import('./login/login.module').then((m) => m.LoginModule),
    canActivate: [LoggedInGuard], // add route gaurd to check if user is logeed in if not then proceed
  },
  {
    path: 'invitation/:id',
    component: VerifyInvitationComponent,
    canActivate: [LoggedInGuard], // add route gaurd to check if user is logeed in if not then proceed
  },
  {
    path: 'main',
    loadChildren: () => import('./main/main.module').then((m) => m.MainModule),
    canActivate: [AuthGuard],
  },
  {
    path: '**',
    redirectTo: 'main',
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
