package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.MentorField;
import lombok.Builder;

public interface RegisterFieldUseCase {
    MentorField registerField(Command command);

    @Builder
    record Command(
            Long accountId,
            Long fieldId
    ) {}
}
