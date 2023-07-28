export class UserLevelResource {
  id?: string;
  userTenantId: string;
  resourceName: string;
  resourceValue: string;
  allowed: boolean;
  constructor(data?: Partial<UserLevelResource>) {
    this.id = data?.id;
    this.userTenantId = data?.userTenantId;
    this.resourceName = data?.resourceName;
    this.resourceValue = data?.resourceValue;
    this.allowed = data?.allowed;
  }
}
