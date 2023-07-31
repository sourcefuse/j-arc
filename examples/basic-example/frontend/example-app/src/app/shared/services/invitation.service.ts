import { Injectable } from "@angular/core";
import { Observable, concatMap } from "rxjs";
import { Role } from "src/app/core/models";
import { ApiService } from "src/app/core/service";
import { AnyAdapter } from "src/app/core/adatpers";
import { InviteUserCommand, VerifyInvitationCommand } from "../commands";
import { UserDto } from "../DTO/user.dto";
import { AuthService } from "./auth.service";
import { HttpHeaders } from "@angular/common/http";
import { AuthTokenSkipHeader } from "src/app/core/interceptors/auth.interceptor";

@Injectable({
    providedIn: 'root'
})
export class InvitationService {

    private readonly authHeaders = new HttpHeaders().set(AuthTokenSkipHeader, '');
    constructor(
        private readonly apiService: ApiService,
        private readonly authService: AuthService,
        private readonly anyAdapter: AnyAdapter,
    ) {
    }

    public inviteUser(requestBody: UserDto) {
        return this.authService
            .currentUser()
            .pipe(
                concatMap(currentUser => this._inviteUser(requestBody, currentUser.tenantId)),
            );

    }

    private _inviteUser(requestBody: UserDto, tenantId: string): Observable<Role[]> {
        requestBody.tenantId = tenantId;
        requestBody.userDetails.defaultTenantId= tenantId;
        const command = new InviteUserCommand(
            this.apiService,
            this.anyAdapter
        );
        command.parameters = {
            data: requestBody
        };
        return command.execute();
    }

    public isValidInvitation(invitationId: string) {
        const command = new VerifyInvitationCommand(
            this.apiService,
            this.anyAdapter,
            invitationId
        );
        command.parameters = {
            headers: this.authHeaders.set('X-Skip-Error-Toast', '')
        };
        return command.execute();
    }
}

