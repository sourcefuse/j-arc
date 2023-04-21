package com.sourcefuse.jarc.services.usertenantservice.service;

import com.sourcefuse.jarc.services.usertenantservice.DTO.Group;
import com.sourcefuse.jarc.services.usertenantservice.commonutils.CommonUtils;
import com.sourcefuse.jarc.services.usertenantservice.exceptions.ApiPayLoadException;
import com.sourcefuse.jarc.services.usertenantservice.repository.GroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class GroupServiceImpl  implements GroupService{

    private GroupRepository groupRepository;

    private CommonUtils<Group> commonUtils;
    @Override
    public Group save(Group group) {
        log.info(" ::::  Create Groups  ::::");
        return groupRepository.save(group);
    }

    @Override
    public Long count() {
        log.info(" :::: Count  total no of  Groups ---  Present ::::");
        return groupRepository.count();
    }

    @Override
    public List<Group> findAll() {
        log.info(" :::: Fetch  total no of  Groups  Present ::::");
        return groupRepository.findAll();
    }

    @Override
    public Optional<Group> findById(UUID id) throws ApiPayLoadException {
        log.info(" :::: Fetch  groups  Present against Id ::::"+id);
        Optional<Group> group = groupRepository.findById(id);
        if (group.isPresent()) {
            return group;
        } else
            throw new ApiPayLoadException("No group is present against given value");
    }

    @Override
    public Group update(Group source, Group target) {
        log.info(" :::: Patch total no of  Group  Present ::::");
        commonUtils.copyProperties(source,target);
        log.info("updated values in models are :::::::"+target.toString());
        return groupRepository.save(target);
    }
    @Override
    public void deleteById(UUID id) {

        log.info(" :::: Delete  Groups  Present against Id::::"+id);
        groupRepository.deleteById(id);
    }
}
