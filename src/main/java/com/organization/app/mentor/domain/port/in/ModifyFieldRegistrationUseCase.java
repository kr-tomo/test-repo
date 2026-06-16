package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.FieldRegistration;
import lombok.Builder;

public interface ModifyFieldRegistrationUseCase {
    FieldRegistration modifyFieldRegistration(Command command);
    void withdrawFieldRegistration(Long accountId, Long registrationId);
    void updateVisibility(Long accountId, Long registrationId, boolean visible);

    @Builder
    record Command(
            Long accountId,
            Long registrationId,
            Long fieldId
    ) {}
}
