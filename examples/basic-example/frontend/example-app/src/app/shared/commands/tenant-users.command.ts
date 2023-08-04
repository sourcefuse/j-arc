import { IAdapter } from 'src/app/core/adatpers';
import { GetListAPICommand } from 'src/app/core/commands';
import { ApiService } from 'src/app/core/service';
import { environment } from 'src/environments/environment';

export class GetTenantUsersCommand<T> extends GetListAPICommand<T> {
  constructor(apiService: ApiService, adapter: IAdapter<T>, tenantId: string) {
    super(
      apiService,
      adapter,
      `${environment.tenantUserApiUrl}/tenants/${tenantId}/users`,
    );
  }
}
