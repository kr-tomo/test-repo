package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.FieldRegistration;
import lombok.Builder;

public interface ReviewFieldRegistrationUseCase {
    FieldRegistration reviewFieldRegistration(Command command);

    @Builder
    record Command(
            Long registrationId,
            Action action
    ) {}

    enum Action {
        APPROVE, REJECT
    }
}
