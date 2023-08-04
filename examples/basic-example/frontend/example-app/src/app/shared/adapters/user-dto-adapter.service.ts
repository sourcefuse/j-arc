import { Injectable } from '@angular/core';
import * as moment from 'moment';

import { IAdapter } from 'src/app/core/adatpers';
import { GenderMap } from 'src/app/core/enums';
import { User } from 'src/app/core/models';

@Injectable()
export class UserDtoAdapter implements IAdapter<User> {
  constructor() {}

  adaptToModel(resp: any): User {
    const user: User = new User();
    if (resp) {
      user.authProvider = resp.authProvider;
      user.roleId = resp.roleId;
      user.status = resp.status;
      user.tenantId = resp.tenantId;
      user.userTenantId = resp.userTenantId;
      if (resp.userDetails) {
        user.id = resp.userDetails.id;
        user.firstName = resp.userDetails.firstName;
        user.middleName = resp.userDetails.middleName;
        user.lastName = resp.userDetails.lastName;
        user.username = resp.userDetails.username;
        user.defaultTenantId = resp.userDetails.defaultTenantId;
        user.email = resp.userDetails.email;
        user.roleType = parseInt(resp.userDetails.role, 10);
        user.roleName = resp.userDetails.roleName;
        user.permissions = resp.userDetails.permissions;
        user.gender =
          resp.userDetails.gender && GenderMap[resp.userDetails.gender];
        user.createdOn = moment(resp.userDetails.createdOn).toDate();
      }
    }
    return user;
  }
  adaptFromModel(data: User): any {
    return data;
  }
}
