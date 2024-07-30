package com.glerk.core.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemplateEmail extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private Template template;

    private String email;

    @Enumerated(EnumType.STRING)
    private EmailType type;

    public TemplateEmail(Template template, String email, EmailType type) {
        this.template = template;
        this.email = email;
        this.type = type;
    }
}
