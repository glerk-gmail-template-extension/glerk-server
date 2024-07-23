package com.glerk.core.repository;

import com.glerk.core.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {

    boolean existsByNameEqualsIgnoreCaseAndCreatedBy(String name, Long userId);

    Optional<Group> findByIdAndCreatedBy(Long id, Long createdBy);

    boolean existsByNameEqualsIgnoreCaseAndCreatedByAndIdNot(String name, Long createdBy, Long id);
}
