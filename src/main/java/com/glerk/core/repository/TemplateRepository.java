package com.glerk.core.repository;

import com.glerk.core.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TemplateRepository extends JpaRepository<Template, Long> {
    Optional<Template> findByIdAndCreatedBy(Long id, Long createdBy);

    List<Template> findByHashtagContainingAndCreatedBy(String hashtag, Long userId);
}
