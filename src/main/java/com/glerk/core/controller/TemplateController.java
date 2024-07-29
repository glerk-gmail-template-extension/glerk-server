package com.glerk.core.controller;

import com.glerk.core.config.User.CurrentUser;
import com.glerk.core.dto.HashtagDto;
import com.glerk.core.dto.TemplateFormDto;
import com.glerk.core.entity.Template;
import com.glerk.core.entity.User;
import com.glerk.core.service.TemplateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/templates")
public class TemplateController {

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateFormDto> getTemplateById(@PathVariable Long templateId, @CurrentUser User user) {
        Template template = templateService.getTemplateByIdAndUserId(templateId, user.getId());
        return ResponseEntity.ok().body(TemplateFormDto.fromEntity(template));
    }

    @PostMapping
    public ResponseEntity<Long> createTemplate(@RequestBody @Valid TemplateFormDto templateDto, @CurrentUser User user) {
        Template createdTemplate = templateService.createTemplate(templateDto, user.getId());
        return ResponseEntity.ok().body(createdTemplate.getId());
    }

    @PutMapping("/{templateId}")
    public ResponseEntity<Long> updateTemplate(
            @PathVariable Long templateId,
            @RequestBody @Valid TemplateFormDto templateDto,
            @CurrentUser User user) {
        templateService.updateTemplate(templateId, templateDto, user.getId());
        return ResponseEntity.ok().body(templateId);
    }

    @DeleteMapping(("/{templateId}"))
    public ResponseEntity<Long> deleteTemplate(@PathVariable Long templateId, @CurrentUser User user) {
        templateService.deleteTemplate(templateId, user.getId());
        return ResponseEntity.ok().body(templateId);
    }

    @PostMapping("/delete")
    public ResponseEntity<List<Long>> deleteTemplates(@RequestBody List<Long> templateIds, @CurrentUser User user) {
        templateService.deleteTemplates(templateIds, user.getId());
        return ResponseEntity.ok().body(templateIds);
    }

    @GetMapping("/hashtag/{hashtag}")
    public ResponseEntity<List<HashtagDto>> getTemplateByHashtag(@PathVariable String hashtag, @CurrentUser User user) {
        List<HashtagDto> hashtagDtos = templateService.getTemplatesByHashtagAndUserId(hashtag, user.getId());
        return ResponseEntity.ok().body(hashtagDtos);
    }
}
