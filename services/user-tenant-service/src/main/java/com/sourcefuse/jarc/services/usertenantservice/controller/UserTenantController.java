package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.UserTenant;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserView;
import com.sourcefuse.jarc.services.usertenantservice.auth.IAuthUserWithPermissions;
import com.sourcefuse.jarc.services.usertenantservice.enums.AuthorizeErrorKeys;
import com.sourcefuse.jarc.services.usertenantservice.enums.PermissionKey;
import com.sourcefuse.jarc.services.usertenantservice.repository.RoleUserTenantRepository;
import com.sourcefuse.jarc.services.usertenantservice.repository.UserViewRepository;
import com.sourcefuse.jarc.services.usertenantservice.service.TenantUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/user-tenants")
@RequiredArgsConstructor
public class UserTenantController {

    private final RoleUserTenantRepository roleUserTRepository;

    private final UserViewRepository userViewRepository;

    private final TenantUserService<Map> tnUsrService;

    // pending authentication doubt ::
    @GetMapping("{id}")
    public ResponseEntity<Object> getUserTenantById(@PathVariable("id") UUID id) {

        //UserTenant userTenant = roleUTService.findById(id);
        Optional<UserTenant> userTenant = roleUserTRepository.findById(id);
        IAuthUserWithPermissions currentUser = (IAuthUserWithPermissions) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();

        if (userTenant.isPresent()) {
            if (currentUser.getTenantId() != userTenant.get().getTenantId() &&
                    !currentUser.getPermissions().contains(PermissionKey.VIEWANYUSER.toString())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());
            }
            if (currentUser.getId() != userTenant.get().getUserId() &&
                    currentUser.getPermissions().contains(PermissionKey.ViewOwnUser.toString())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, AuthorizeErrorKeys.NOTALLOWEDACCESS.toString());
            }
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User-Tenant is present against given value" + id);

        /**As discussed by samarpan bhattacharya currently
         * checkViewTenantRestrictedPermissions we dont have to implement..
         * One tenant cannot see others tehant data (this logic needs to be implement
         * need to discuss with yesha or team for this)
         *
         * Predicate predicate = null;
         Map<String, Object> map = null;
         if (currentUser.getPermissions().contains(PermissionKey.ViewTenantUserRestricted.toString()) &&
         currentUser.getTenantId() == id) {
         map = tnUsrService.checkViewTenantRestrictedPermissions(currentUser, predicate,
         UserView.class);
         predicate = (Predicate) map.get(CommonConstants.predicate);
         }
         CriteriaBuilder cb = (CriteriaBuilder) map.get(CommonConstants.builder);
         CriteriaQuery cq = (CriteriaQuery) map.get(CommonConstants.criteriaQuery);
         Root root = cq.from(UserView.class);

         if (predicate != null) {
         predicate = cb.and(predicate, cb.equal(root.get("userTenantId"), id));
         } else predicate = cb.equal(root.get("userTenantId"), id);
         cq.where(predicate);*/

        //where and filter pending
        UserView userView = userViewRepository.findByUserTenantId(id);
        if (userView == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not found");
        }
        return new ResponseEntity<Object>(userView, HttpStatus.OK);
    }

}
