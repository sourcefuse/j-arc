import { IAdapter } from "src/app/core/adatpers";
import { PostAPICommand } from "src/app/core/commands";
import { environment } from "src/environments/environment";
import { ApiService } from "../service/api.service";

export class RefreshTokenCommand<T> extends PostAPICommand<T> {
  constructor(apiService: ApiService, adapter: IAdapter<T>) {
    super(apiService, adapter, `${environment.authApiUrl}/auth/token-refresh`);
  }
}
