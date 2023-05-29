package com.sourcefuse.jarc.core.enums;

public enum PermissionKey {
  CREATE_USER_GROUP("CreateUserGroup"),
  VIEW_USER_GROUP_LIST("ViewUserGroupList"),
  UPDATE_USER_GROUP("UpdateUserGroup"),
  DELETE_USER_GROUP("DeleteUserGroup"),
  DELETE_SUBSCRIPTIONS("DeleteSubscriptions"),
  VIEW_ROLES("ViewRoles"),
  NOT_ALLOWED("NotAllowed"),
  CREATE_ROLES("CreateRoles"),
  UPDATE_ROLES("UpdateRoles"),
  DELETE_ROLES("DeleteRoles"),
  VIEW_ANY_USER("ViewAnyUser"),
  VIEW_TENANT_USER("ViewTenantUser"),
  VIEW_TENANT_USER_RESTRICTED("ViewTenantUserRestricted"),
  VIEW_ALL_USER("ViewAllUser"),
  VIEW_OWN_USER("ViewOwnUser"),
  CREATE_ANY_USER("CreateAnyUser"),
  CREATE_TENANT_USER("CreateTenantUser"),
  CREATE_TENANT_USER_RESTRICTED("CreateTenantUserRestricted"),
  UPDATE_ANY_USER("UpdateAnyUser"),
  UPDATE_OWN_USER("UpdateOwnUser"),
  UPDATE_TENANT_USER("UpdateTenantUser"),
  UPDATE_TENANT_USER_RESTRICTED("UpdateTenantUserRestricted"),
  DELETE_ANY_USER("DeleteAnyUser"),
  DELETE_TENANT_USER("DeleteTenantUser"),
  DELETE_TENANT_USER_RESTRICTED("DeleteTenantUserRestricted"),
  CREATE_TENANT("CreateTenant"),
  VIEW_TENANT("ViewTenant"),
  UPDATE_TENANT("UpdateTenant"),
  VIEW_OWN_TENANT("ViewOwnTenant"),
  UPDATE_OWN_TENANT("UpdateOwnTenant"),
  DELETE_TENANT("DeleteTenant"),
  ADD_MEMBER_TO_USER_GROUP("AddMemberToUserGroup"),
  UPDATE_MEMBER_IN_USER_GROUP("UpdateMemberInUserGroup"),
  REMOVE_MEMBER_FROM_USER_GROUP("RemoveMemberFromUserGroup"),
  LEAVE_USER_GROUP("LeaveUserGroup"),
  UPDATE_USER_TENANT_PREFERENCE("UpdateUserTenantPreference"),
  VIEW_USER_TENANT_PREFERENCE("ViewUserTenantPreference");

  private final String value;

  PermissionKey(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
