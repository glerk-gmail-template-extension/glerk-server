package com.glerk.core.dto;

import com.glerk.core.entity.Template;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemplateDto {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public static TemplateDto fromEntity(Template template) {
        TemplateDto templateDto = new TemplateDto();
        templateDto.setId(template.getId());
        templateDto.setName(template.getName());
        templateDto.setCreatedAt(template.getCreatedAt());
        return templateDto;
    }
}
