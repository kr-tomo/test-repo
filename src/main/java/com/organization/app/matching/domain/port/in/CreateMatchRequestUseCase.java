package com.organization.app.matching.domain.port.in;

import com.organization.app.matching.domain.model.MatchRequest;

public interface CreateMatchRequestUseCase {
    MatchRequest create(Long menteeAccountId, Long fieldId, Long mentorAccountId);
}
