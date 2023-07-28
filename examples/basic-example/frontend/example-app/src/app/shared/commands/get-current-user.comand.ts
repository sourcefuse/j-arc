import { IAdapter } from "src/app/core/adatpers";
import { GetAPICommand } from "src/app/core/commands";
import { ApiService } from "src/app/core/service/api.service";
import { environment } from "src/environments/environment";

export class GetCurrentUserCommand<T> extends GetAPICommand<T> {
  constructor(apiService: ApiService, adapter: IAdapter<T>) {
    super(apiService, adapter, `${environment.authApiUrl}/auth/me`);
  }
}
