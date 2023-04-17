package com.sourcefuse.userintentservice.commonutils;

import com.sourcefuse.userintentservice.DTO.Role;
import com.sourcefuse.userintentservice.DTO.Tenant;
import com.sourcefuse.userintentservice.DTO.UserTenant;
import org.springframework.stereotype.Component;

@Component
public class CommonUtils<T> {

    public void copyProperties(T source, T destination) {

        if (source instanceof Tenant && destination instanceof Tenant) {

            if (((Tenant) source).getId() != null) ((Tenant) destination).setId(((Tenant) source).getId());

            if (((Tenant) source).getName() != null) ((Tenant) destination).setName(((Tenant) source).getName());

            if (((Tenant) source).getStatus() != null) ((Tenant) destination).setStatus(((Tenant) source).getStatus());

            if (((Tenant) source).getKey() != null) ((Tenant) destination).setKey(((Tenant) source).getKey());

            if (((Tenant) source).getWebsite() != null)
                ((Tenant) destination).setWebsite(((Tenant) source).getWebsite());

            if (((Tenant) source).getAddress() != null)
                ((Tenant) destination).setAddress(((Tenant) source).getAddress());

            if (((Tenant) source).getCity() != null) ((Tenant) destination).setCity(((Tenant) source).getCity());

            if (((Tenant) source).getState() != null) ((Tenant) destination).setState(((Tenant) source).getState());

            if (((Tenant) source).getZip() != null) ((Tenant) destination).setZip(((Tenant) source).getZip());

            if (((Tenant) source).getCountry() != null)
                ((Tenant) destination).setCountry(((Tenant) source).getCountry());

            if (((Tenant) source).getPrimaryContactEmail() != null)
                ((Tenant) destination).setPrimaryContactEmail(((Tenant) source).getPrimaryContactEmail());

            if (((Tenant) source).getAllowedDomain() != null)
                ((Tenant) destination).setAllowedDomain(((Tenant) source).getAllowedDomain());

            if (((Tenant) source).getTenantType() != null)
                ((Tenant) destination).setTenantType(((Tenant) source).getTenantType());

            if (((Tenant) source).getTenantConfigs() != null)
                ((Tenant) destination).setTenantConfigs(((Tenant) source).getTenantConfigs());

            if (((Tenant) source).getUserTenants() != null)
                ((Tenant) destination).setUserTenants(((Tenant) source).getUserTenants());
            if (((Tenant) source).getCreatedBy() != null)
                ((Tenant) destination).setCreatedBy(((Tenant) source).getCreatedBy());
            if (((Tenant) source).getCreatedOn() != null)
                ((Tenant) destination).setCreatedOn(((Tenant) source).getCreatedOn());
            if (((Tenant) source).getModifiedBy() != null)
                ((Tenant) destination).setModifiedBy(((Tenant) source).getModifiedBy());
            if (((Tenant) source).getModifiedOn() != null)
                ((Tenant) destination).setModifiedOn(((Tenant) source).getModifiedOn());
            if (((Tenant) source).getDeletedBy() != null)
                ((Tenant) destination).setDeletedBy(((Tenant) source).getDeletedBy());
            if (((Tenant) source).getDeletedOn() != null)
                ((Tenant) destination).setDeletedOn(((Tenant) source).getDeletedOn());
            if (((Tenant) source).isDeleted())
                ((Tenant) destination).setDeleted(true);

        }

        if (source instanceof Role && destination instanceof Role) {
            if (((Role) source).getId() != null)
                ((Role) destination).setId(((Role) source).getId());
            if (((Role) source).getName() != null)
                ((Role) destination).setName(((Role) source).getName());
            if (((Role) source).getRoleType() != null)
                ((Role) destination).setRoleType(((Role) source).getRoleType());
            if (((Role) source).getPermissions() != null)
                ((Role) destination).setPermissions(((Role) source).getPermissions());
            if (((Role) source).getAllowedClients() != null)
                ((Role) destination).setAllowedClients(((Role) source).getAllowedClients());
            if (((Role) source).getUserTenants() != null)
                ((Role) destination).setUserTenants(((Role) source).getUserTenants());
            if (((Role) source).getCreatedByUser() != null)
                ((Role) destination).setCreatedByUser(((Role) source).getCreatedByUser());
            if (((Role) source).getModifiedByUser() != null)
                ((Role) destination).setModifiedByUser(((Role) source).getModifiedByUser());
            if (((Role) source).getCreatedBy() != null)
                ((Role) destination).setCreatedBy(((Role) source).getCreatedBy());
            if (((Role) source).getCreatedOn() != null)
                ((Role) destination).setCreatedOn(((Role) source).getCreatedOn());
            if (((Role) source).getModifiedBy() != null)
                ((Role) destination).setModifiedBy(((Role) source).getModifiedBy());
            if (((Role) source).getModifiedOn() != null)
                ((Role) destination).setModifiedOn(((Role) source).getModifiedOn());
            if (((Role) source).getDeletedBy() != null)
                ((Role) destination).setDeletedBy(((Role) source).getDeletedBy());
            if (((Role) source).getDeletedOn() != null)
                ((Role) destination).setDeletedOn(((Role) source).getDeletedOn());
            if (((Role) source).isDeleted())
                ((Role) destination).setDeleted(true);
        }

        if (source instanceof UserTenant && destination instanceof UserTenant) {
            if (((UserTenant) source).getId() != null)
                ((UserTenant) destination).setId(((UserTenant) source).getId());
            if (((UserTenant) source).getLocale() != null)
                ((UserTenant) destination).setLocale(((UserTenant) source).getLocale());
            if (((UserTenant) source).getStatus() != null)
                ((UserTenant) destination).setStatus(((UserTenant) source).getStatus());
            if (((UserTenant) source).getUserId() != null)
                ((UserTenant) destination).setUserId(((UserTenant) source).getUserId());
            if (((UserTenant) source).getTenantId() != null)
                ((UserTenant) destination).setTenantId(((UserTenant) source).getTenantId());
            if (((UserTenant) source).getRoleId() != null)
                ((UserTenant) destination).setRoleId(((UserTenant) source).getRoleId());
            if (((UserTenant) source).getUserGroups() != null)
                ((UserTenant) destination).setUserGroups(((UserTenant) source).getUserGroups());
            if (((UserTenant) source).getUserLevelPermissions() != null)
                ((UserTenant) destination).setUserLevelPermissions(((UserTenant) source).getUserLevelPermissions());
            if (((UserTenant) source).getCreatedOn() != null)
                ((UserTenant) destination).setCreatedOn(((UserTenant) source).getCreatedOn());
            if (((UserTenant) source).getModifiedOn() != null)
                ((UserTenant) destination).setModifiedOn(((UserTenant) source).getModifiedOn());
            if (((UserTenant) source).getDeletedBy() != null)
                ((UserTenant) destination).setDeletedBy(((UserTenant) source).getDeletedBy());
            if (((UserTenant) source).getDeletedOn() != null)
                ((UserTenant) destination).setDeletedOn(((UserTenant) source).getDeletedOn());
            if (((UserTenant) source).isDeleted())
                ((UserTenant) destination).setDeleted(true);
        }
    }
}