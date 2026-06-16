package com.organization.app.mentor.domain.port.in;

import com.organization.app.mentor.domain.model.CareerEntry;
import lombok.Builder;

public interface ModifyCareerUseCase {
    CareerEntry modifyCareer(Command command);
    void withdrawCareer(Long accountId, Long careerId);
    void updateVisibility(Long accountId, Long careerId, boolean visible);

    @Builder
    record Command(
            Long accountId,
            Long careerId,
            String content
    ) {}
}
