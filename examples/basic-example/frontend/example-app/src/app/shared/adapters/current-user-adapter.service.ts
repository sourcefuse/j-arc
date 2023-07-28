import {Injectable} from '@angular/core';
import * as moment from 'moment';

import { IAdapter } from 'src/app/core/adatpers';
import { User, GenderMap } from 'src/app/core/models';

@Injectable()
export class CurrentUserAdapter implements IAdapter<User> {
  constructor() {}

  adaptToModel(resp: any): User {
    const user: User = new User();
    if (resp) {
      user.id = resp.id;
      user.firstName = resp.firstName;
      user.middleName = resp.middleName;
      user.lastName = resp.lastName;
      user.username = resp.username;
      user.email = resp.email;
      user.roleId = resp.roleId;
      user.roleType = parseInt(resp.role, 10);
      user.roleName = resp.roleName;
      user.permissions = resp.permissions;
      user.tenantId = resp.tenantId;
      user.gender = resp.gender && GenderMap[resp.gender];
      user.userTenantId = resp.userTenantId;
      user.status = resp.status;
      user.createdOn = moment(resp.createdOn).toDate();
      user.invitationLinkExpiredOn = this.setInvitationExpireTime(resp);
      user.authProvider = resp.authProvider;
    }
    return user;
  }
  adaptFromModel(data: User): any {
    return data;
  }

  private setInvitationExpireTime(resp: any) {
    let date: Date;
    if (resp.expiresOn) {
      date = moment(resp.expiresOn).toDate();
    }
    if (resp.invitationLinkExpiredOn) {
      date = moment(resp.invitationLinkExpiredOn).toDate();
    }
    return date;
  }
}
