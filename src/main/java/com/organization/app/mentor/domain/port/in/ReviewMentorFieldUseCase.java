package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.MentorField;
import lombok.Builder;

public interface ReviewMentorFieldUseCase {
    MentorField reviewFieldRegistration(Command command);

    @Builder
    record Command(
            Long registrationId,
            Action action
    ) {}

    enum Action {
        APPROVE, REJECT
    }
}
