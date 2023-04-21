package com.sourcefuse.jarc.services.usertenantservice.controller;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Count;
import com.sourcefuse.jarc.services.usertenantservice.DTO.Group;
import com.sourcefuse.jarc.services.usertenantservice.DTO.UserGroup;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.HttpError;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.HttpErrors;
import com.sourcefuse.jarc.services.usertenantservice.service.GroupService;
import com.sourcefuse.jarc.services.usertenantservice.service.UserGroupsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping(value = {"${api.groups.context.url}"})
@RequiredArgsConstructor
public class UserGroupController {

    private final UserGroupsService usrGService;

    private final GroupService gpService;

    @PostMapping("{id}" + "${api.user-group.context.url}")
    public ResponseEntity<Object> createRole(@Valid @RequestBody UserGroup userGroup, @PathVariable("id") UUID id) throws ApiPayLoadException {
        log.info(" :::::::::::: Creating User Group  Apis consumed against ID:::::::::::" + id);

        Group group = gpService.findById(id).get();

        if (userGroup.getGroupId() == null) userGroup.setGroupId(group.getId());
        // log.info(role.toString());
        //  role.getUserTenants().add(userTenant);
        // log.info(userTenant.toString());
        List<UserGroup> usrGrp = usrGService.findByGpIDUsrTntId(userGroup);
        if (usrGrp == null) {
            usrGrp = new ArrayList<>();
            log.info(" ::::::::::::  User Group not found against groupID::::" + userGroup.getGroupId()
                    + " USer Tenant ID" + userGroup.getUserTenantId());
            usrGrp.add(usrGService.save(userGroup));
            group.setModifiedOn(usrGrp.get(0).getModifiedOn());
            gpService.save(group);
        }
        return new ResponseEntity<>(usrGrp.get(0), HttpStatus.CREATED);
    }

    @PatchMapping("{id}" + "${api.user-group.context.url}" + "{userGroupId}")
    public ResponseEntity<Count> updateAll(@PathVariable("id") UUID id, @RequestBody UserGroup userGroup,
                                           @PathVariable("userGroupId") UUID userGroupId) throws ApiPayLoadException, HttpError {

        log.info("::::::::::::: User-Group  Rest Apis consumed to update User-Group against groupId " +
                ":" + id + " and userGroupId" + userGroupId);

        /** fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
        Group group = gpService.findById(id).get();

        Count count = Count.builder().count(usrGService.getUserGroupCount(id, true)).build();
        if (count.getCount() == 1) {

            List<UserGroup> usrGrp = usrGService.findByGpIDUsrTntIdIsOwn(id, userGroupId, true);

            if (usrGrp != null && !usrGrp.get(0).isOwner())
                throw HttpErrors.Forbidden("${one.owner.msg}");
        }
        if (userGroup.getId() == null) userGroup.setId(userGroupId);
        usrGService.save(userGroup);

        group.setModifiedOn(new Date());
        gpService.save(group);

        return new ResponseEntity<Count>(new Count(1L), HttpStatus.OK);
    }

    @DeleteMapping("{id}" + "${api.user-group.context.url}" + "{userGroupId}")
    public ResponseEntity<Object> deleteUsrGrp(@PathVariable("id") UUID id, @PathVariable("userGroupId") UUID userGroupId)
            throws ApiPayLoadException, HttpError {
        log.info(":::::::::::::  User - Group  Delete rest Apis consumed to delete records against groupId " +
                ":" + id + " and userGroupId" + userGroupId);

        /** fetch value in Group against primary key and also to
         and to update modifiedOn parameter*/
        Group group = gpService.findById(id).get();

        // const isAdmin = +this.currentUser.role === RoleKey.Admin; pending doubt::
        boolean isAdmin = true;
        //this has to be from current User
        UUID useTenantID = null;
        List<UserGroup> usrGrp = usrGService.findByGpIDUsrTntIdGrpID(id, userGroupId, useTenantID);

        UserGroup userGroupRecord = usrGrp.stream().filter(usrGp -> usrGp.getId() == userGroupId).findFirst().get();
        //userGroup.userTenantId === this.currentUser.userTenantId //this has to change doubt::
        UserGroup currentUserGroup = usrGrp.stream().filter(usrGp -> usrGp.getUserTenantId() == userGroupId).findFirst().get();

        if (userGroupRecord == null) throw HttpErrors.Forbidden("${user.group.not.found}");

        /** TODO::
         * Auth logic and Admin persion check logic pending
         * */

        Count count = Count.builder().count(usrGService.getUserGroupCountByGroupId(id)).build();
        if (count.getCount() == 1) {
            if (userGroupRecord.isOwner()) throw HttpErrors.Forbidden("${one.owner.msg");
            else throw HttpErrors.Forbidden("${one.owner.msg");
        }
        usrGService.deleteUserGroup(userGroupId);

        group.setModifiedOn(new Date());
        gpService.save(group);
        return new ResponseEntity<Object>("UserGroup DELETE success", HttpStatus.OK);
    }

    @GetMapping("{id}" + "${api.user-group.context.url}")
    public ResponseEntity<Object> getAllUsTenantByRole(@PathVariable("id") UUID id) throws ApiPayLoadException {

        log.info("::::::::::::: Fetch List of User - Groups :::::::::::::;");

        /** fetch value in Group against primary key also a validation
         if group table does not have the records then  it will also not be
         available in userGroups table */
        Group group = gpService.findById(id).get();

        List<UserGroup> usrGrpList = usrGService.findAll();
        return new ResponseEntity<Object>(usrGrpList, HttpStatus.OK);
    }

    @GetMapping("{id}" + "${api.user-group.count.context.url}")
    public ResponseEntity<Object> countUserGroup(@PathVariable("id") UUID id) {

        log.info("::::::::::::: User Group count Api consumed for groupId :::::::::::::" + id);
        Long grpCnt = usrGService.getUserGroupCountByGroupId(id);
        Count count = Count.builder().count(grpCnt).build();
        return new ResponseEntity<Object>(count, HttpStatus.OK);

    }

}
