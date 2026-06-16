package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.FieldRegistration;
import lombok.Builder;

public interface RegisterFieldUseCase {
    FieldRegistration registerField(Command command);

    @Builder
    record Command(
            Long accountId,
            Long fieldId
    ) {}
}
