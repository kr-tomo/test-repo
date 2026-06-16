package com.organization.app.field.domain.port.in;

import com.organization.app.field.domain.model.Field;
import lombok.Builder;

public interface CreateFieldUseCase {
    Field createField(Command command);

    @Builder
    record Command(String name, Long parentId) {}
}
