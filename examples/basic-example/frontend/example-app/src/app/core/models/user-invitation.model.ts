export class UserInvitationModel {
  userTenantId: string;
  expireOn?: Date;

  constructor(data?: Partial<UserInvitationModel>) {
    this.userTenantId = data.userTenantId;
  }
}
