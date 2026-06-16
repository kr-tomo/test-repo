package com.organization.app.field.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFieldRepository extends JpaRepository<FieldEntity, Long> {
    boolean existsByNameAndParentId(String name, Long parentId);
}
