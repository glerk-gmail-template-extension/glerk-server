package com.glerk.core.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Template extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    private Long id;
    private String name;
    private String hashtag;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TemplateEmail> emails = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "template_labels", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "label")
    private List<String> labels = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    public Template(String name, String hashtag, String subject, String body, Group group) {
        this.name = name;
        this.hashtag = hashtag;
        this.subject = subject;
        this.body = body;

        if (group != null) {
            changeGroup(group);
        }
    }

    public void changeGroup(Group group) {
        if (this.group != null) {
            this.group.getTemplates().remove(this);
        }

        this.group = group;
        group.getTemplates().add(this);
    }

    public void addEmail(String email, EmailType type) {
        TemplateEmail templateEmail = new TemplateEmail(this, email, type);
        this.emails.add(templateEmail);
    }

    public void clearEmails() {
        this.emails.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Template template = (Template) o;
        return Objects.equals(id, template.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
