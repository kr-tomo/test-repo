package com.organization.app.field.domain.port.out;

import com.organization.app.field.domain.model.Field;
import java.util.List;
import java.util.Optional;

public interface FieldRepositoryPort {
    Field save(Field field);
    Optional<Field> findById(Long id);
    List<Field> findAll();
    boolean existsByNameAndParentId(String name, Long parentId);
    List<Field> findAllDescendants(Long id);
    void saveAll(List<Field> fields);
}
