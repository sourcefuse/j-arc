package com.sourcefuse.jarc.services.usertenantservice.enums;

public enum PermissionKey {
    CREATEUSERGROUP("CreateUserGroup", "1"),
    VIEWUSERGROUPLIST("ViewUserGroupList", "2"),
    UPDATEUSERGROUP("UpdateUserGroup", "3"),
    DELETEUSERGROUP("DeleteUserGroup", "4"),
    DELETESUBSCRIPTIONS("DeleteSubscriptions", "5"),
    VIEWROLES("ViewRoles", "6"),
    NOTALLOWED("NotAllowed", "7"),
    CREATEROLES("CreateRoles", "8"),
    UPDATEROLES("UpdateRoles", "9"),
    DELETEROLES("DeleteRoles", "10"),
    VIEWANYUSER("ViewAnyUser", "11"),
    VIEWTENANTUSER("ViewTenantUser", "12"),
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
