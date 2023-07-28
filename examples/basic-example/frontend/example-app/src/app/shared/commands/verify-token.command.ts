import { IAdapter } from "src/app/core/adatpers";
import { GetAPICommand } from "src/app/core/commands";
import { environment } from "src/environments/environment";
import { ApiService } from "../service/api.service";

export class VerifyTokenCommand<T> extends GetAPICommand<T> {
  constructor(apiService: ApiService, anyAdapter: IAdapter<T>) {
    super(
      apiService,
      anyAdapter,
      `${environment.authApiUrl}/auth/sign-up/verify-token`,
    );
  }
}
