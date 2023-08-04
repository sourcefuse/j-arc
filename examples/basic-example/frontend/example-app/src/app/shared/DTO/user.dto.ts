import { User } from 'src/app/core/models';

export class UserDto {
  authProvider: string;
  userTenantId: string;
  authId: string;
  tenantId: string;
  roleId: string;
  status: string;
  userDetails: User;

  constructor(data?: Partial<UserDto>) {
    this.authProvider = data?.authProvider;
    this.userTenantId = data?.userTenantId;
    this.authId = data?.authId;
    this.tenantId = data?.tenantId;
    this.roleId = data?.roleId;
    this.status = data?.status;
    this.userDetails = new User(data?.userDetails);
  }
}
