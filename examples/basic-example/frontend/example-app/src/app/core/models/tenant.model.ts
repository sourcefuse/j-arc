import { TenantConfig } from ".";
import { TenantStatus } from "../enums";
import { NameId } from "./name-id.model";


export class Tenant extends NameId {
  status: TenantStatus;
  key?: string;
  address?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
  config?: TenantConfig[];

  constructor(data?: Partial<Tenant>) {
    super(data);
    this.status = data?.status;
    this.key = data?.key;
    this.address = data?.address;
    this.city = data?.city;
    this.state = data?.state;
    this.zip = data?.zip;
    this.country = data?.country;
  }
}
