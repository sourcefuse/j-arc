import { NameId } from './name-id.model';

export class Role extends NameId {
  permissions: string[] = [];
  roleType: number;
  modifiedOn: string;
  modifiedBy: string;
  createdOn: string;
  createdBy: string;
  numberOfUsers?: number;
  permissionList?: string[] = [];

  constructor(data?: Partial<Role>) {
    super(data);
    this.roleType = data?.roleType;
    this.permissions = data?.permissions;
    this.modifiedOn = data?.modifiedOn;
    this.modifiedBy = data?.modifiedBy;
    this.createdOn = data?.createdOn;
    this.createdBy = data?.createdBy;
    this.numberOfUsers = data?.numberOfUsers;
    this.permissionList = data?.permissionList;
  }
}
