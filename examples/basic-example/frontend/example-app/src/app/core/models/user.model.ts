import { Tenant } from '.';
import {Gender} from './gender.enum';
export class User {
  id: string;
  firstName: string;
  middleName?: string;
  lastName?: string;
  username: string;
  email?: string;
  roleId: string;
  roleType: number;
  permissions: string[];
  tenantId: string;
  gender: {id: Gender; name: string};
  status?: number;
  roleName?: string;
  userTenantId?: string;
  createdOn: Date;
  invitationLinkExpiredOn?: Date;
  tenant?: Tenant;
  authProvider?: string;
  resourceCost?: number;
  organizationRoleId?: string;
  organizationRoleName?: string;
  displayValue?: string;
  get fullName() {
    const fullNameArray = [];
    if (this.firstName) {
      fullNameArray.push(this.firstName);
    }
    if (this.middleName) {
      fullNameArray.push(this.middleName);
    }
    if (this.lastName) {
      fullNameArray.push(this.lastName);
    }
    return fullNameArray.join(' ');
  }

  constructor(data?: Partial<User>) {
    this.id = data?.id;
    this.firstName = data?.firstName;
    this.middleName = data?.middleName;
    this.lastName = data?.lastName;
    this.username = data?.username;
    this.email = data?.email;
    this.roleId = data?.roleId;
    this.roleType = data?.roleType;
    this.permissions = data?.permissions;
    this.tenantId = data?.tenantId;
    this.status = data?.status;
    this.roleName = data?.roleName;
    this.userTenantId = data?.userTenantId;
    this.createdOn = data?.createdOn;
    this.invitationLinkExpiredOn = data?.invitationLinkExpiredOn;
    this.tenant = data?.tenant;
    this.authProvider = data?.authProvider;
    this.resourceCost = data?.resourceCost;
    this.organizationRoleId = data?.organizationRoleId;
    this.organizationRoleName = data?.organizationRoleName;
    this.displayValue = data?.displayValue;
  }
}
