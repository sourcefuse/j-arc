package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.dto.User;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.dto.UserView;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.enums.RoleType;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.sourcefuse.jarc.services.usertenantservice.commons.TypeRole.roleTypeMap;

@RequiredArgsConstructor
@Service
public class UpdateTntUserServiceImpl implements UpdateTntUserService{

    private final UserTenantRepository userTenantRepository;

    private final UserRepository userRepository;

    @Override
    public void updateById(
            IAuthUserWithPermissions currentUser,
            UUID id,
            UserView userView,
            UUID tenantId
    ) {
        checkForUpdatePermissions(currentUser, id, tenantId);
        if (userView.getUsername() != null) {
            long usrCount = userRepository.countByIdNotAndUsername(
                    id,
                    userView.getUsername()
            );
            if (usrCount > 0) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        "${user.name.exits}"
                );
            }
        }
        User tempUser = new User();
        BeanUtils.copyProperties(userView, tempUser);
        User savUser;
        Optional<User> optUser = userRepository.findById(id);
        if (optUser.isPresent()) {
            savUser = optUser.get();
            BeanUtils.copyProperties(
                    tempUser,
                    savUser,
                    CommonUtils.getNullPropertyNames(tempUser)
            );
            userRepository.save(savUser);
        }
        updateUserTenant(userView, id, currentUser);
    }
    private void checkForUpdatePermissions(
            IAuthUserWithPermissions currentUser,
            UUID id,
            UUID tenantId
    ) {
        extracted(currentUser, id, tenantId);

        if (
                currentUser
                        .getPermissions()
                        .contains(PermissionKey.UPDATE_TENANT_USER.toString())
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

        if (
                !currentUser
                        .getPermissions()
                        .contains(PermissionKey.UPDATE_ANY_USER.toString()) &&
                        !tenantId.equals(currentUser.getTenantId())
        ) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
            );
        }
    }
    private void extracted(
            IAuthUserWithPermissions currentUser,
            UUID id,
            UUID tenantId
    ) {
        if (
                !currentUser.getId().equals(id) &&
                        currentUser
                                .getPermissions()
                                .contains(PermissionKey.UPDATE_OWN_USER.toString())
        ) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
            );
        }
        if (
                currentUser.getTenantId().equals(tenantId) &&
                        currentUser
                                .getPermissions()
                                .contains(PermissionKey.UPDATE_TENANT_USER_RESTRICTED.toString()) &&
                        !currentUser.getId().equals(id)
        ) {
            Optional<UserTenant> userTenant =
                    userTenantRepository.findFirstByUserIdAndTenantIdOrderByIdAsc(
                            id,
                            currentUser.getTenantId()
                    );
            //  userTenant.getRole().getRoleType()  doubt::
            //RoleType.DEFAULT this need to change
            if (
                    !userTenant.isPresent() ||
                            !currentUser
                                    .getPermissions()
                                    .contains(
                                            "UpdateTenant" + roleTypeMap.get(RoleType.DEFAULT).permissionKey()
                                    )
            ) {
                throw new ResponseStatusException(
                        HttpStatus.FORBIDDEN,
                        AuthorizeErrorKeys.NOT_ALLOWED_ACCESS.toString()
                );
            }
        }
    }

    private void updateUserTenant(
            UserView userView,
            UUID id,
            IAuthUserWithPermissions currentUser
    ) {
        UserTenant utData = new UserTenant();
        if (userView.getRoleId() != null) {
            utData.setRoleId(userView.getRoleId());
        }
        if (userView.getStatus() != null) {
            utData.setStatus(userView.getStatus());
        }
        if (utData.getRoleId() != null && utData.getStatus() != null) {
            List<UserTenant> usrTntLis =
                    userTenantRepository.findAllByUserIdAndTenantId(
                            id,
                            userView.getTenantId() != null
                                    ? userView.getTenantId()
                                    : currentUser.getTenantId()
                    );
            usrTntLis.forEach((UserTenant usrTnt) -> {
                BeanUtils.copyProperties(
                        utData,
                        usrTnt,
                        CommonUtils.getNullPropertyNames(utData)
                );
                usrTnt = userTenantRepository.save(usrTnt);
            });
        }
    }
}


