package com.organization.app.field.domain.port.in;

import com.organization.app.field.domain.model.FieldStatus;
import java.util.List;

public interface GetFieldsUseCase {
    List<FieldResponse> getFields();

    record FieldResponse(
            Long id,
            String name,
            Long parentId,
            FieldStatus status,
            List<FieldResponse> children
    ) {}
}
