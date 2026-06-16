package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.CareerEntry;
import lombok.Builder;

public interface ReviewCareerUseCase {
    CareerEntry reviewCareer(Command command);

    @Builder
    record Command(
            Long careerId,
            Action action
    ) {}

    enum Action {
        APPROVE, REJECT
    }
}
