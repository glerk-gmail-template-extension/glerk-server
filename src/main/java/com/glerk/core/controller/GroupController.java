package com.glerk.core.controller;

import com.glerk.core.config.User.CurrentUser;
import com.glerk.core.dto.GroupDto;
import com.glerk.core.dto.GroupUpdateDto;
import com.glerk.core.entity.Group;
import com.glerk.core.entity.User;
import com.glerk.core.exception.BusinessException;
import com.glerk.core.exception.ErrorCode;
import com.glerk.core.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<GroupDto>> getGroupsByGroupNameOrTemplate(@RequestParam(value = "groupId", required = false) Long groupId,
                                                                         @RequestParam(value = "templateName", required = false) String templateName,
                                                                         @CurrentUser User user) {
        List<GroupDto> groupDtos = groupService.findByGroupIdOrTemplateNameAndUserId(groupId, templateName, user.getId());
        return ResponseEntity.ok().body(groupDtos);
    }

    @PostMapping
    public ResponseEntity<GroupDto> createGroup(@RequestBody @Valid Group group, @CurrentUser User user) {
        boolean exists = groupService.existsByName(group.getName(), user.getId());
        if (exists) {
            throw new BusinessException(ErrorCode.GROUP_NAME_ALREADY_EXIST);
        }

        GroupDto groupDto = groupService.createGroup(group.getName());
        return ResponseEntity.ok().body(groupDto);
    }

    @PutMapping("/{groupId}")
    public ResponseEntity<GroupDto> updateGroupName(
            @PathVariable Long groupId,
            @RequestBody @Valid GroupUpdateDto group,
            @CurrentUser User user) {
        GroupDto updatedGroup = groupService.updateGroup(groupId, group.getName(), user.getId());
        return ResponseEntity.ok().body(updatedGroup);
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Long> deleteGroup(@PathVariable Long groupId, @CurrentUser User user) {
        groupService.deleteGroup(groupId, user.getId());
        return ResponseEntity.ok().body(groupId);
    }
}
