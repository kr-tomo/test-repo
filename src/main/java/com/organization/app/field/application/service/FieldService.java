package com.organization.app.field.application.service;

import com.organization.app.field.domain.exception.FieldException;
import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.in.CreateFieldUseCase;
import com.organization.app.field.domain.port.in.DeactivateFieldUseCase;
import com.organization.app.field.domain.port.in.GetFieldsUseCase;
import com.organization.app.field.domain.port.in.UpdateFieldUseCase;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FieldService implements
        CreateFieldUseCase,
        UpdateFieldUseCase,
        DeactivateFieldUseCase,
        GetFieldsUseCase {

    private final FieldRepositoryPort fieldRepositoryPort;

    @Override
    @Transactional
    public Field createField(CreateFieldUseCase.Command command) {
        if (command.parentId() != null) {
            Field parent = fieldRepositoryPort.findById(command.parentId())
                    .orElseThrow(FieldException::parentNotFound);
            if (parent.getStatus() == FieldStatus.INACTIVE) {
                throw(FieldException.parentInactive());
            }
        }

        if (fieldRepositoryPort.existsByNameAndParentId(command.name(), command.parentId())) {
            throw FieldException.duplicateName();
        }

        Field field = Field.create(command.name(), command.parentId());
        return fieldRepositoryPort.save(field);
    }

    @Override
    @Transactional
    public Field updateField(UpdateFieldUseCase.Command command) {
        Field field = fieldRepositoryPort.findById(command.id())
                .orElseThrow(FieldException::fieldNotFound);

        if (fieldRepositoryPort.existsByNameAndParentId(command.name(), field.getParentId())) {
            // Check if it's the same field name
            if (!field.getName().equals(command.name())) {
                throw FieldException.duplicateName();
            }
        }

        field.updateName(command.name());
        return fieldRepositoryPort.save(field);
    }

    @Override
    @Transactional
    public void deactivateField(Long id) {
        Field field = fieldRepositoryPort.findById(id)
                .orElseThrow(FieldException::fieldNotFound);

        if (field.getStatus() == FieldStatus.INACTIVE) {
            throw FieldException.alreadyInactive();
        }

        field.deactivate();
        fieldRepositoryPort.save(field);

        // Recursive deactivation
        List<Field> descendants = fieldRepositoryPort.findAllDescendants(id);
        descendants.forEach(Field::deactivate);
        fieldRepositoryPort.saveAll(descendants);
    }

    @Override
    public List<FieldResponse> getFields() {
        List<Field> allFields = fieldRepositoryPort.findAll();
        
        Map<Long, List<FieldResponse>> childrenMap = allFields.stream()
                .filter(f -> f.getParentId() != null)
                .collect(Collectors.groupingBy(
                        Field::getParentId,
                        Collectors.mapping(f -> new FieldResponse(f.getId(), f.getName(), f.getParentId(), f.getStatus(), new ArrayList<>()), Collectors.toList())
                ));

        List<FieldResponse> rootFields = allFields.stream()
                .filter(f -> f.getParentId() == null)
                .map(f -> new FieldResponse(f.getId(), f.getName(), f.getParentId(), f.getStatus(), new ArrayList<>()))
                .collect(Collectors.toList());

        // Fill tree
        fillChildren(rootFields, childrenMap);
        
        return rootFields;
    }

    private void fillChildren(List<FieldResponse> parents, Map<Long, List<FieldResponse>> childrenMap) {
        for (FieldResponse parent : parents) {
            List<FieldResponse> children = childrenMap.getOrDefault(parent.id(), new ArrayList<>());
            parent.children().addAll(children);
            if (!children.isEmpty()) {
                fillChildren(children, childrenMap);
            }
        }
    }
}
