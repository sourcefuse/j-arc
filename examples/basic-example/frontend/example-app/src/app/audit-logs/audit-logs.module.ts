import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuditLogsRoutingModule } from './audit-logs-routing.module';
import { ListComponent } from './list/list.component';
import { MatTableModule } from '@angular/material/table';
import { LogDetailsComponent } from './popups/log-details/log-details.component';
import { MatDialogModule } from '@angular/material/dialog';


@NgModule({
  declarations: [
    ListComponent,
    LogDetailsComponent
  ],
  imports: [
    CommonModule,
    AuditLogsRoutingModule,
    MatTableModule,
    MatDialogModule
  ]
})
export class AuditLogsModule { }
