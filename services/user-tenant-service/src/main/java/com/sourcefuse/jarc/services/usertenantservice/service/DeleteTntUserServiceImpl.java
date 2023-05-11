package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleType;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserGroupsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static com.sourcefuse.jarc.services.usertenantservice.commons.TypeRole.roleTypeMap;

@RequiredArgsConstructor
@Service
public class DeleteTntUserServiceImpl implements DeleteTntUserService{

    private final UserRepository userRepository;
    private final UserTenantRepository userTenantRepository;
    private final UserGroupsRepository userGroupsRepository;
    @Override
    public void deleteUserById(
            IAuthUserWithPermissions currentUser,
            UUID id,
            UUID tenantId
    ) {
        checkForDeleteTenantUserRestrictedPermission(currentUser, id);
        checkForDeleteTenantUserPermission(currentUser, id);
        checkForDeleteAnyUserPermission(currentUser, tenantId);

        Optional<UserTenant> existingUserTenant =
                userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
                        id,
                        currentUser.getTenantId()
                );

        if (existingUserTenant.isPresent()) {
            userGroupsRepository.deleteAllByUserTenantId(
                    existingUserTenant.get().getId()
            );
        }
        userTenantRepository.deleteAllByUserIdAndTenantId(
                id,
                currentUser.getTenantId()
        );

        UserTenant userTenant = userTenantRepository.findByUserId(id);
        UUID defaultTenantId = null;
        if (userTenant != null) {
            defaultTenantId = userTenant.getTenantId();
        }

        Optional<User> optUser = userRepository.findById(id);
        User user;
        if (optUser.isPresent()) {
            user = optUser.get();
            user.setDefaultTenantId(defaultTenantId);
            userRepository.save(user);
        }
    }

    private void checkForDeleteTenantUserRestrictedPermission(
            IAuthUserWithPermissions currentUser,
            UUID id
    ) {
        if (
                currentUser
                        .getPermissions()
                        .contains(PermissionKey.DELETE_TENANT_USER_RESTRICTED.toString())
        ) {
            Optional<UserTenant> userTenant =
                    userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
                            id,
                            currentUser.getTenantId()
                    );
            //  userTenant.getRole().getRoleType() doubt::
            //RoleType.DEFAULT need to change
            if (
                    !userTenant.isPresent() ||
                            !currentUser
                                    .getPermissions()
                                    .contains(
                                            "DeleteTenant" + roleTypeMap.get(RoleType.DEFAULT).permissionKey()
                                    )
            ) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
                );
            }
        }
    }


    private void checkForDeleteTenantUserPermission(
            IAuthUserWithPermissions currentUser,
            UUID id
    ) {
        if (
                currentUser
                        .getPermissions()
                        .contains(PermissionKey.DELETE_TENANT_USER.toString())
        ) {
            Optional<UserTenant> userTenant =
                    userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
                            id,
                            currentUser.getTenantId()
                    );
            if (!userTenant.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
                );
            }
        }
    }

    private static void checkForDeleteAnyUserPermission(
            IAuthUserWithPermissions currentUser,
            UUID tenantId
    ) {
        if (
                !currentUser
                        .getPermissions()
                        .contains(PermissionKey.DELETE_ANY_USER.toString()) &&
                        !tenantId.equals(currentUser.getTenantId())
        ) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
            );
        }
    }
}
