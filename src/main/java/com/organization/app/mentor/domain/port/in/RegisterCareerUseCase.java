package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.CareerEntry;
import lombok.Builder;

public interface RegisterCareerUseCase {
    CareerEntry registerCareer(Command command);

    @Builder
    record Command(
            Long accountId,
            String content
    ) {}
}
