import { IAdapter } from "src/app/core/adatpers";
import { PostAPICommand } from "src/app/core/commands";
import { environment } from "src/environments/environment";
import { ApiService } from "src/app/core/service";

export class InviteUserCommand<T> extends PostAPICommand<T> {
    constructor(apiService: ApiService, adapter: IAdapter<T>) {
        super(apiService, adapter, `${environment.facadeApiUrl}/invite-user`);
    }
}
