package com.glerk.core.dto;

import com.glerk.core.entity.Group;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GroupDto {

    private Long id;
    private String name;
    private List<TemplateDto> templates = new ArrayList<>();

    public static GroupDto fromEntity(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(group.getId());
        groupDto.setName(group.getName());

        List<TemplateDto> templateDtos = group.getTemplates().stream()
                .map(TemplateDto::fromEntity)
                .collect(Collectors.toList());

        groupDto.setTemplates(templateDtos);
        return groupDto;
    }
}
