import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AuditLogsRoutingModule } from './audit-logs-routing.module';
import { ListComponent } from './list/list.component';
import {MatTableModule} from '@angular/material/table';


@NgModule({
  declarations: [
    ListComponent
  ],
  imports: [
    CommonModule,
    AuditLogsRoutingModule,
    MatTableModule
  ]
})
export class AuditLogsModule { }
