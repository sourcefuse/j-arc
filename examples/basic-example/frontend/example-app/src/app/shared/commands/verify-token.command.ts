import { IAdapter } from "src/app/core/adatpers";
import { GetAPICommand } from "src/app/core/commands";
import { ApiService } from "src/app/core/service";
import { environment } from "src/environments/environment";

export class VerifyTokenCommand<T> extends GetAPICommand<T> {
  constructor(apiService: ApiService, anyAdapter: IAdapter<T>) {
    super(
      apiService,
      anyAdapter,
      `${environment.authApiUrl}/auth/sign-up/verify-token`,
    );
  }
}
