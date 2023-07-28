import { IAdapter } from "src/app/core/adatpers";
import { GetAPICommand } from "src/app/core/commands";
import { environment } from "src/environments/environment";
import { ApiService } from "../service/api.service";

export class GetCurrentUserCommand<T> extends GetAPICommand<T> {
  constructor(apiService: ApiService, adapter: IAdapter<T>) {
    super(apiService, adapter, `${environment.authApiUrl}/auth/me`);
  }
}
