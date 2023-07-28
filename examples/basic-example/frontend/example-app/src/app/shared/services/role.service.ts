import { Injectable } from "@angular/core";
import { cloneDeep } from 'lodash';
import { InMemoryStorageService } from "ngx-webstorage-service";
import { Observable, concatMap, of, tap } from "rxjs";
import { AnyAdapter } from "src/app/core/adatpers";
import { Role, User } from "src/app/core/models";
import { ApiService } from "src/app/core/service";
import { StoreKeys } from "src/app/core/store/store-keys.enum";
import { UserDtoAdapter } from "../adapters/user-dto-adapter.service";
import { GetTenantUsersCommand } from "../commands";
import { AuthService } from "./auth.service";
import { GetRolesCommand } from "../commands/get-roles.command";
import { RolesAdapter } from "../adapters";

@Injectable({
    providedIn: 'root'
})
export class RoleService {

    constructor(
        private readonly apiService: ApiService,
        private readonly roleAdapter: RolesAdapter,
        private readonly inMemoryStore: InMemoryStorageService
    ) {
    }

    public getRoles(reset = false): Observable<Role[]> {
        const rolesInStore = this.inMemoryStore.get(StoreKeys.ROLES);
        if (!reset && rolesInStore) {
            return of(cloneDeep(rolesInStore));
        } else {
            const command = new GetRolesCommand(
                this.apiService,
                this.roleAdapter,
            );
            command.parameters = {
            };
            return command.execute().pipe(
                tap(roles => {
                    this.inMemoryStore.set(StoreKeys.ROLES, cloneDeep(roles));
                }),
            );
        }
    }
}

