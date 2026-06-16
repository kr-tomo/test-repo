package com.organization.app.field.adapter.out.persistence;

import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FieldPersistenceAdapter implements FieldRepositoryPort {

    private final JpaFieldRepository jpaFieldRepository;

    @Override
    public Field save(Field field) {
        FieldEntity entity = toEntity(field);
        FieldEntity saved = jpaFieldRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Field> findById(Long id) {
        return jpaFieldRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Field> findAll() {
        return jpaFieldRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndParentId(String name, Long parentId) {
        return jpaFieldRepository.existsByNameAndParentId(name, parentId);
    }

    @Override
    public List<Field> findAllDescendants(Long id) {
        List<Field> allFields = findAll();
        List<Field> descendants = new java.util.ArrayList<>();
        collectDescendants(id, allFields, descendants);
        return descendants;
    }

    private void collectDescendants(Long parentId, List<Field> allFields, List<Field> descendants) {
        List<Field> children = allFields.stream()
                .filter(f -> parentId.equals(f.getParentId()))
                .collect(Collectors.toList());
        descendants.addAll(children);
        for (Field child : children) {
            collectDescendants(child.getId(), allFields, descendants);
        }
    }

    @Override
    public void saveAll(List<Field> fields) {
        List<FieldEntity> entities = fields.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
        jpaFieldRepository.saveAll(entities);
    }

    private FieldEntity toEntity(Field field) {
        return FieldEntity.builder()
                .id(field.getId())
                .name(field.getName())
                .parentId(field.getParentId())
                .status(field.getStatus())
                .build();
    }

    private Field toDomain(FieldEntity entity) {
        return Field.builder()
                .id(entity.getId())
                .name(entity.getName())
                .parentId(entity.getParentId())
                .status(entity.getStatus())
                .build();
    }
}
