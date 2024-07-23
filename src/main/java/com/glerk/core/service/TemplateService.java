package com.glerk.core.service;

import com.glerk.core.dto.TemplateFormDto;
import com.glerk.core.entity.EmailType;
import com.glerk.core.entity.Group;
import com.glerk.core.entity.Template;
import com.glerk.core.exception.BusinessException;
import com.glerk.core.exception.ErrorCode;
import com.glerk.core.repository.GroupRepository;
import com.glerk.core.repository.TemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final GroupRepository groupRepository;

    public TemplateService(TemplateRepository templateRepository, GroupRepository groupRepository) {
        this.templateRepository = templateRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional(readOnly = true)
    public Template getTemplateByIdAndUserId(Long templateId, Long userId) {
        return templateRepository.findByIdAndCreatedBy(templateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEMPLATE_NOT_FOUND));
    }

    public Template createTemplate(TemplateFormDto templateDto, Long userId) {
        Group group = groupRepository.findByIdAndCreatedBy(templateDto.getGroupId(), userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        Template template = new Template(
                templateDto.getName(),
                templateDto.getHashtag(),
                templateDto.getSubject(),
                templateDto.getBody(),
                group
        );

        setTemplateEmails(templateDto, template);

        template.setLabels(templateDto.getLabels());

        return templateRepository.save(template);
    }

    public void updateTemplate(Long templateId, TemplateFormDto templateDto, Long userId) {
        Template template = templateRepository.findByIdAndCreatedBy(templateId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEMPLATE_NOT_FOUND));

        Group group = groupRepository.findByIdAndCreatedBy(templateDto.getGroupId(), userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GROUP_NOT_FOUND));

        template.setName(templateDto.getName());
        template.setHashtag(templateDto.getHashtag());
        template.setSubject(templateDto.getSubject());
        template.setBody(templateDto.getBody());
        template.setGroup(group);

        template.clearEmails();

        setTemplateEmails(templateDto, template);
        template.setLabels(templateDto.getLabels());

        templateRepository.save(template);
    }

    private void setTemplateEmails(TemplateFormDto templateDto, Template template) {
        if (templateDto.getSender() != null) {
            template.addEmail(templateDto.getSender(), EmailType.SENDER);
        }

        if (templateDto.getRecipients() != null) {
            templateDto.getRecipients().forEach(email -> template.addEmail(email, EmailType.RECIPIENT));
        }

        if (templateDto.getCc() != null) {
            templateDto.getCc().forEach(email -> template.addEmail(email, EmailType.CC));
        }

        if (templateDto.getBcc() != null) {
            templateDto.getBcc().forEach(email -> template.addEmail(email, EmailType.BCC));
        }
    }

    public void deleteTemplate(Long templateId, Long userId) {
        Template template = templateRepository.findById(templateId)
                .orElseThrow(() -> new BusinessException(ErrorCode.TEMPLATE_NOT_FOUND));

        if (!template.getCreatedBy().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_AUTHORIZED);
        }

        templateRepository.delete(template);
    }

    public void deleteTemplates(List<Long> templateIds, Long userId) {
        List<Template> templates = templateRepository.findAllById(templateIds);

        if (templateIds.size() == 0) {
            throw new BusinessException(ErrorCode.TEMPLATE_NOT_FOUND);
        }

        boolean allCreatedByUser = templates.stream()
                .allMatch(template -> template.getCreatedBy().equals(userId));

        if (!allCreatedByUser) {
            throw new BusinessException(ErrorCode.NOT_AUTHORIZED);
        }

        templateRepository.deleteAll(templates);
    }
}
