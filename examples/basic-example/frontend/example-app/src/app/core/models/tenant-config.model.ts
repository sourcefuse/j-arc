export class TenantConfig {
  id: string;
  configKey: string;
  configValue: any;
  tenantId: string;

  constructor(data?: Partial<TenantConfig>) {
    this.id = data?.id;
    this.configKey = data?.configKey;
    this.configValue = data?.configValue;
    this.tenantId = data?.tenantId;
  }
}
