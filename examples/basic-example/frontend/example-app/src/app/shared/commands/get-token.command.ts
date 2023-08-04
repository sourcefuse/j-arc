import { IAdapter } from 'src/app/core/adatpers';
import { PostAPICommand } from 'src/app/core/commands';
import { ApiService } from 'src/app/core/service/api.service';
import { environment } from 'src/environments/environment';

export class GetTokenCommand<T> extends PostAPICommand<T> {
  constructor(apiService: ApiService, adapter: IAdapter<T>) {
    super(apiService, adapter, `${environment.authApiUrl}/auth/token`);
  }
}
