package com.glerk.core.service;

import com.glerk.core.dto.GroupDto;
import com.glerk.core.entity.Group;
import com.glerk.core.exception.BusinessException;
import com.glerk.core.exception.ErrorCode;
import com.glerk.core.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Transactional(readOnly = true)
    public List<GroupDto> findByGroupIdOrTemplateNameAndUserId(Long groupId, String templateName, Long userId) {
        List<Group> groups = groupRepository.findByGroupIdOrTemplateNameAndUserId(groupId, templateName, userId);

        return Optional.ofNullable(groups)
                .orElse(Collections.emptyList())
                .stream()
                .map(GroupDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public boolean existsByName(String name, Long userId) {
        return groupRepository.existsByNameEqualsIgnoreCaseAndCreatedBy(name, userId);
    }

    public GroupDto createGroup(String name) {
        Group group = groupRepository.save(new Group(name));
        return GroupDto.fromEntity(group);
    }

    public GroupDto updateGroup(Long groupId, String newName, Long userId) {
        Group group = groupRepository.findByIdAndCreatedBy(groupId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        if (groupRepository.existsByNameEqualsIgnoreCaseAndCreatedByAndIdNot(newName, userId, groupId)) {
            throw new BusinessException(ErrorCode.GROUP_NAME_ALREADY_EXIST);
        }

        group.setName(newName);
        Group updatedGroup = groupRepository.save(group);
        return GroupDto.fromEntity(updatedGroup);
    }

    public void deleteGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        if (!group.getCreatedBy().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_AUTHORIZED);
        }

        groupRepository.delete(group);
    }
}
