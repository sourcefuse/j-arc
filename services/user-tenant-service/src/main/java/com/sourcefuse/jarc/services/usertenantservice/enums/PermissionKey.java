package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum PermissionKey {
    CreateUserGroup("CreateUserGroup", "1"),
    ViewUserGroupList("ViewUserGroupList", "2"),
    UpdateUserGroup("UpdateUserGroup", "3"),
    DeleteUserGroup("DeleteUserGroup", "4"),
    DeleteSubscriptions("DeleteSubscriptions", "5"),
    ViewRoles("ViewRoles", "6"),
    NotAllowed("NotAllowed", "7"),
    CreateRoles("CreateRoles", "8"),
    UpdateRoles("UpdateRoles", "9"),
    DeleteRoles("DeleteRoles", "10"),
    ViewAnyUser("ViewAnyUser", "11"),
    ViewTenantUser("ViewTenantUser", "12"),
    ViewTenantUserRestricted("ViewTenantUserRestricted", "13"),
    ViewAllUser("ViewAllUser", "14"),
    ViewOwnUser("ViewOwnUser", "15"),
    CreateAnyUser("CreateAnyUser", "16"),
    CreateTenantUser("CreateTenantUser", "17"),
    CreateTenantUserRestricted("CreateTenantUserRestricted", "18"),
    UpdateAnyUser("UpdateAnyUser", "19"),
    UpdateOwnUser("UpdateOwnUser", "20"),
    UpdateTenantUser("UpdateTenantUser", "21"),
    UpdateTenantUserRestricted("UpdateTenantUserRestricted", "22"),
    DeleteAnyUser("DeleteAnyUser", "23"),
    DeleteTenantUser("DeleteTenantUser", "24"),
    DeleteTenantUserRestricted("DeleteTenantUserRestricted", "25"),
    CreateTenant("CreateTenant", "26"),
    ViewTenant("ViewTenant", "27"),
    UpdateTenant("UpdateTenant", "28"),
    ViewOwnTenant("ViewOwnTenant", "29"),
    UpdateOwnTenant("UpdateOwnTenant", "30"),
    DeleteTenant("DeleteTenant", "31"),
    AddMemberToUserGroup("AddMemberToUserGroup", "32"),
    UpdateMemberInUserGroup("UpdateMemberInUserGroup", "33"),
    RemoveMemberFromUserGroup("RemoveMemberFromUserGroup", "34"),
    LeaveUserGroup("LeaveUserGroup", "35"),
    UpdateUserTenantPreference("UpdateUserTenantPreference", "36"),
    ViewUserTenantPreference("ViewUserTenantPreference", "37");

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
