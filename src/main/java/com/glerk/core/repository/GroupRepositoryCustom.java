package com.glerk.core.repository;

import com.glerk.core.entity.Group;

import java.util.List;

public interface GroupRepositoryCustom {

    List<Group> findByGroupIdOrTemplateNameAndUserId(Long groupId, String templateName, Long userId);
}
