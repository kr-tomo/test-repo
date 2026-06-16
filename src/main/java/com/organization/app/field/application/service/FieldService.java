package com.organization.app.field.application.service;

import com.organization.app.field.domain.exception.FieldException;
import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.in.CreateFieldUseCase;
import com.organization.app.field.domain.port.in.DeactivateFieldUseCase;
import com.organization.app.field.domain.port.in.UpdateFieldUseCase;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FieldService implements
        CreateFieldUseCase,
        UpdateFieldUseCase,
        DeactivateFieldUseCase {

    private final FieldRepositoryPort fieldRepositoryPort;

    @Override
    public Field createField(CreateFieldUseCase.Command command) {
        if (command.parentId() != null) {
            Field parent = fieldRepositoryPort.findById(command.parentId())
                    .orElseThrow(FieldException::parentNotFound);
            if (parent.getStatus() == FieldStatus.INACTIVE) {
                throw(FieldException.parentInactive());
            }
        }

        if (fieldRepositoryPort.existsByName(command.name())) {
            throw FieldException.duplicateName();
        }

        Field field = Field.create(command.name(), command.parentId());
        return fieldRepositoryPort.save(field);
    }

    @Override
    public Field updateField(UpdateFieldUseCase.Command command) {
        Field field = fieldRepositoryPort.findById(command.id())
                .orElseThrow(FieldException::fieldNotFound);

        if (fieldRepositoryPort.existsByName(command.name())) {
            // Check if it's the same field name
            if (!field.getName().equals(command.name())) {
                throw FieldException.duplicateName();
            }
        }

        field.updateName(command.name());
        return fieldRepositoryPort.save(field);
    }

    @Override
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
}
