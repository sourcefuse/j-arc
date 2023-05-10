package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum PermissionKey {
  CREATE_USER_GROUP("CreateUserGroup", "1"),
  VIEW_USER_GROUP_LIST("ViewUserGroupList", "2"),
  UPDATE_USER_GROUP("UpdateUserGroup", "3"),
  DELETE_USER_GROUP("DeleteUserGroup", "4"),
  DELETE_SUBSCRIPTIONS("DeleteSubscriptions", "5"),
  VIEW_ROLES("ViewRoles", "6"),
  NOT_ALLOWED("NotAllowed", "7"),
  CREATE_ROLES("CreateRoles", "8"),
  UPDATE_ROLES("UpdateRoles", "9"),
  DELETE_ROLES("DeleteRoles", "10"),
  VIEW_ANY_USER("ViewAnyUser", "11"),
  VIEW_TENANT_USER("ViewTenantUser", "12"),
  VIEW_TENANT_USER_RESTRICTED("ViewTenantUserRestricted", "13"),
  VIEW_ALL_USER("ViewAllUser", "14"),
  VIEW_OWN_USER("ViewOwnUser", "15"),
  CREATE_ANY_USER("CreateAnyUser", "16"),
  CREATE_TENANT_USER("CreateTenantUser", "17"),
  CREATE_TENANT_USER_RESTRICTED("CreateTenantUserRestricted", "18"),
  UPDATE_ANY_USER("UpdateAnyUser", "19"),
  UPDATE_OWN_USER("UpdateOwnUser", "20"),
  UPDATE_TENANT_USER("UpdateTenantUser", "21"),
  UPDATE_TENANT_USER_RESTRICTED("UpdateTenantUserRestricted", "22"),
  DELETE_ANY_USER("DeleteAnyUser", "23"),
  DELETE_TENANT_USER("DeleteTenantUser", "24"),
  DELETE_TENANT_USER_RESTRICTED("DeleteTenantUserRestricted", "25"),
  CREATE_TENANT("CreateTenant", "26"),
  VIEW_TENANT("ViewTenant", "27"),
  UPDATE_TENANT("UpdateTenant", "28"),
  VIEW_OWN_TENANT("ViewOwnTenant", "29"),
  UPDATE_OWN_TENANT("UpdateOwnTenant", "30"),
  DELETE_TENANT("DeleteTenant", "31"),
  ADD_MEMBER_TO_USER_GROUP("AddMemberToUserGroup", "32"),
  UPDATE_MEMBER_IN_USER_GROUP("UpdateMemberInUserGroup", "33"),
  REMOVE_MEMBER_FROM_USER_GROUP("RemoveMemberFromUserGroup", "34"),
  LEAVE_USER_GROUP("LeaveUserGroup", "35"),
  UPDATE_USER_TENANT_PREFERENCE("UpdateUserTenantPreference", "36"),
  VIEW_USER_TENANT_PREFERENCE("ViewUserTenantPreference", "37");

  private final String key;
  private final String value;

  PermissionKey(String key, String value) {
    this.key = key;
    this.value = value;
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
