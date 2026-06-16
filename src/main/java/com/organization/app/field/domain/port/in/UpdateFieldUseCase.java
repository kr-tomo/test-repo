package com.organization.app.field.domain.port.in;

import com.organization.app.field.domain.model.Field;
import lombok.Builder;

public interface UpdateFieldUseCase {
    Field updateField(Command command);

    @Builder
    record Command(Long id, String name) {}
}
