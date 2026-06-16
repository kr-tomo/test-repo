package com.organization.app.field.query.port.in;

import com.organization.app.field.domain.model.FieldStatus;
import java.util.List;

public interface GetFieldsQuery {
    List<FieldResponse> getFields();

    record FieldResponse(
            Long id,
            String name,
            Long parentId,
            FieldStatus status,
            List<FieldResponse> children
    ) {}
}
