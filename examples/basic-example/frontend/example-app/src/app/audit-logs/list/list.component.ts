import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { AuditLogService } from 'src/app/shared/services/audit-log.service';
import { LogDetailsComponent } from '../popups/log-details/log-details.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit {
  displayedColumns: string[] = [
    'logAction',
    'actedOn',
    'actionKey',
    'before',
    'after',
    'action',
  ];
  dataSource = [];
  constructor(
    private readonly auditLogService: AuditLogService,
    public dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    this.getLogs();
  }
  getLogs() {
    this.auditLogService
      .getAuditLogs()
      .pipe(take(1))
      .subscribe((data: any) => {
        this.dataSource = data;
      });
  }

  openDialog(log: any) {
    this.dialog.open(LogDetailsComponent, {
      data: log,
    });
  }
}
