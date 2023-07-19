import {CurrentUserAdapter} from './current-user-adapter.service';
import {ForgotPasswordAdapter} from './forgot-password-adapter.service';
import {LoginAdapter} from './login-adapter.service';
import {OrganizationRoleAdapter} from './organization-role-adapter.service';
import {ResetPasswordAdapter} from './reset-password-adapter.service';
import {ResourcePlaceholderAdapterService} from './resource-placeholder-adapter.service';
import {ResourcePlaceholderToMemberAdapter} from './resource-placeholder-to-member-adapter.service';
import {SignUpAdapter} from './signup-adapter.service';
import {TeamToMemberAdapter} from './team-to-member-adapter.service';
import {TeamAdapterService} from './team.adapter.service';
import {UserResourceAdapterService} from './user-resource-adapter.service';
import {UserTenantPrefsAdapterService} from './user-tenant-prefs-adapter.serivce';
import {UserToMemberDetailsAdapter} from './user-to-member-adapter.service';
import {UserToMemberAdapter} from './user-to-member-details-adapter.service';

export const Adapters = [
  CurrentUserAdapter,
  LoginAdapter,
  UserResourceAdapterService,
  SignUpAdapter,
  UserTenantPrefsAdapterService,
  ForgotPasswordAdapter,
  ResetPasswordAdapter,
  TeamToMemberAdapter,
  UserToMemberAdapter,
  UserToMemberDetailsAdapter,
  TeamAdapterService,
  ResourcePlaceholderAdapterService,
  ResourcePlaceholderToMemberAdapter,
  OrganizationRoleAdapter,
];

export * from './current-user-adapter.service';
export * from './forgot-password-adapter.service';
export * from './login-adapter.service';
export * from './organization-role-adapter.service';
export * from './reset-password-adapter.service';
export * from './resource-placeholder-adapter.service';
export * from './resource-placeholder-to-member-adapter.service';
export * from './signup-adapter.service';
export * from './team-to-member-adapter.service';
export * from './team.adapter.service';
export * from './user-resource-adapter.service';
export * from './user-tenant-prefs-adapter.serivce';
export * from './user-to-member-adapter.service';
