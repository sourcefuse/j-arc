import { IAdapter } from 'src/app/core/adatpers';
import { GetAPICommand } from 'src/app/core/commands';
import { environment } from 'src/environments/environment';
import { ApiService } from 'src/app/core/service';

export class VerifyResetPasswordTokenCommand<T> extends GetAPICommand<T> {
  constructor(apiService: ApiService, anyAdapter: IAdapter<T>, token: string) {
    super(
      apiService,
      anyAdapter,
      `${environment.authApiUrl}/auth/verify-reset-password-link?token=${token}`,
    );
  }
}
