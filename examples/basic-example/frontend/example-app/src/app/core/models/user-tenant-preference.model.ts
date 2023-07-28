export class UserTenantPreference {
  id?: string;
  configKey: string;
  configValue;
  userTenantId?: string;

  constructor(data?: Partial<UserTenantPreference>) {
    this.id = data?.id;
    this.configKey = data?.configKey;
    this.configValue = data?.configValue;
    this.userTenantId = data?.userTenantId;
  }
}
