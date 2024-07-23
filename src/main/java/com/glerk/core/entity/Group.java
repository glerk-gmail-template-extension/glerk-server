package com.glerk.core.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @NotBlank(message = "Group name is required")
    @Size(min = 1, max = 30, message = "Group name must be between 1 and 30 characters")
    private String name;

    @OneToMany(mappedBy = "group", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Template> templates = new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }
}
