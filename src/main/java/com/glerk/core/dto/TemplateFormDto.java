package com.glerk.core.dto;

import com.glerk.core.entity.EmailType;
import com.glerk.core.entity.Template;
import com.glerk.core.entity.TemplateEmail;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class TemplateFormDto {

    @NotBlank(message = "템플릿 이름을 입력해 주세요.")
    @Size(min = 1, max = 50, message = "템플릿 이름은 1~50자 사이여야 합니다.")
    private String name;

    @NotBlank(message = "해시태그를 입력해 주세요.")
    @Size(min = 1, max = 50, message = "해시태그는 1~50자 사이여야 합니다.")
    private String hashtag;

    @Size(max = 50, message = "제목은 50자를 초과할 수 없습니다.")
    private String subject;

    private String body;

    @Email(message = "발신자의 이메일 형식이 잘못되었습니다.")
    private String sender;

    @Valid
    private List<@Email(message = "수신자의 이메일 형식이 잘못되었습니다.") String> recipients;

    @Valid
    private List<@Email(message = "참조 이메일 형식이 잘못되었습니다.") String> cc;

    @Valid
    private List<@Email(message = "비밀 참조 이메일 형식이 잘못되었습니다.") String> bcc;

    private List<String> labels;

    @NotNull(message = "그룹을 선택해 주세요.")
    private Long groupId;

    public static TemplateFormDto fromEntity(Template template) {
        TemplateFormDto dto = new TemplateFormDto();
        dto.setName(template.getName());
        dto.setHashtag(template.getHashtag());
        dto.setSubject(template.getSubject());
        dto.setBody(template.getBody());
        dto.setLabels(template.getLabels());

        if (template.getGroup() != null) {
            dto.setGroupId(template.getGroup().getId());
        }

        dto.setSender(template.getEmails().stream()
                .filter(email -> email.getType() == EmailType.SENDER)
                .map(TemplateEmail::getEmail)
                .findFirst().orElse(null));

        dto.setRecipients(template.getEmails().stream()
                .filter(email -> email.getType() == EmailType.RECIPIENT)
                .map(TemplateEmail::getEmail)
                .collect(Collectors.toList()));

        dto.setCc(template.getEmails().stream()
                .filter(email -> email.getType() == EmailType.CC)
                .map(TemplateEmail::getEmail)
                .collect(Collectors.toList()));

        dto.setBcc(template.getEmails().stream()
                .filter(email -> email.getType() == EmailType.BCC)
                .map(TemplateEmail::getEmail)
                .collect(Collectors.toList()));

        return dto;
    }
}
