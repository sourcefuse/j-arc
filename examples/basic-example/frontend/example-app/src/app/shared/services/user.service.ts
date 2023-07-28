import { Injectable } from "@angular/core";
import { cloneDeep } from 'lodash';
import { InMemoryStorageService } from "ngx-webstorage-service";
import { Observable, concatMap, of, tap } from "rxjs";
import { AnyAdapter } from "src/app/core/adatpers";
import { User } from "src/app/core/models";
import { ApiService } from "src/app/core/service";
import { StoreKeys } from "src/app/core/store/store-keys.enum";
import { UserDtoAdapter } from "../adapters/user-dto-adapter.service";
import { GetTenantUsersCommand } from "../commands";
import { AuthService } from "./auth.service";

@Injectable({
    providedIn: 'root'
})
export class UserService {
    userMap: Map<string, User>;

    constructor(
        private readonly apiService: ApiService,
        private readonly anyAdapter: AnyAdapter,
        private readonly authService: AuthService,
        private readonly userDtoAdapter: UserDtoAdapter,
        private readonly inMemoryStore: InMemoryStorageService
    ) {
        this.userMap = new Map<string, User>();
    }

    getUsers(reset = false): Observable<User[]> {
        return this.authService
            .currentUser()
            .pipe(
                concatMap(currentUser => this._getUsers(currentUser.tenantId, reset)),
            );
    }

    private _getUsers(tenantId: string, reset = false): Observable<User[]> {
        let usersInStore: User[] = this.inMemoryStore.get(StoreKeys.USER_LIST);
        if (!reset && usersInStore) {
            usersInStore = cloneDeep(
                usersInStore.map(user => this.userDtoAdapter.adaptToModel(user)),
            );
            return of(usersInStore);
        } else {
            const command = new GetTenantUsersCommand(
                this.apiService,
                this.userDtoAdapter,
                tenantId,
            );
            command.parameters = {
            };
            return command.execute().pipe(
                tap(users => {
                    this.inMemoryStore.set(StoreKeys.USER_LIST, cloneDeep(users));
                    for (const user of users) {
                        this.userMap.set(user.userTenantId, user);
                    }
                }),
            );
        }
    }

}