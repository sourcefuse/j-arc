import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AnyAdapter } from 'src/app/core/adatpers';
import { ApiService } from 'src/app/core/service';
import { GetAuditLogCommand } from '../commands';

@Injectable({
  providedIn: 'root',
})
export class AuditLogService {
  constructor(
    private readonly apiService: ApiService,
    private readonly anyAdapter: AnyAdapter,
  ) {}

  getAuditLogs(): Observable<any[]> {
    const command = new GetAuditLogCommand(this.apiService, this.anyAdapter);
    command.parameters = {};
    return command.execute();
  }
}
