package com.glerk.core.repository;

import com.glerk.core.dto.AutocompleteEmailDto;
import com.glerk.core.entity.TemplateEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TemplateEmailRepository extends JpaRepository<TemplateEmail, Long> {

    @Query("SELECT new com.glerk.core.dto.AutocompleteEmailDto(te.email, te.updatedAt) " +
            "FROM TemplateEmail te WHERE te.createdBy = :userId " +
            "ORDER BY te.updatedAt DESC")
    List<AutocompleteEmailDto> findDistinctEmailsByCreatedByOrderByUpdatedAtDesc(@Param("userId") Long userId);
}
