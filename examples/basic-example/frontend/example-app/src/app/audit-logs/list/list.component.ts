import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { AuditLogService } from 'src/app/shared/services/audit-log.service';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss']
})
export class ListComponent implements OnInit {

  displayedColumns: string[] = ['action', 'actedOn', 'actionKey', 'before', 'after'];
  dataSource = [];
  constructor(private readonly auditLogService: AuditLogService) { }

  ngOnInit(): void {
    this.getLogs();
  }
  getLogs(){
    this.auditLogService.getAuditLogs()
      .pipe(take(1))
      .subscribe((data: any) => {
        this.dataSource = data;
      });
  }
}
