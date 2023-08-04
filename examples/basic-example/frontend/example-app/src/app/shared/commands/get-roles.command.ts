import { IAdapter } from 'src/app/core/adatpers';
import { GetListAPICommand } from 'src/app/core/commands';
import { Role } from 'src/app/core/models';
import { ApiService } from 'src/app/core/service';
import { environment } from 'src/environments/environment';

export class GetRolesCommand extends GetListAPICommand<Role> {
  constructor(apiService: ApiService, adapter: IAdapter<Role>) {
    super(apiService, adapter, `${environment.tenantUserApiUrl}/roles`);
  }
}
