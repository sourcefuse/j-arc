import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { InviteUserComponent } from '../invitation/invite-user/invite-user.component';
import { MainComponent } from './main/main.component';

const routes: Routes = [
  {
    path: "",
    component:MainComponent,
    children: [
      {
        path: 'users',
        loadChildren: () => import('../user/user.module').then(m => m.UserModule),
      },
      {
        path: 'audit-logs',
        loadChildren: () => import('../audit-logs/audit-logs.module').then(m => m.AuditLogsModule),
      },
      {
        path: 'invite-user',
        component: InviteUserComponent
      },
      {
        path: "**",
        redirectTo: "users"
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class MainRoutingModule { }
