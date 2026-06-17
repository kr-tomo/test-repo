package com.organization.app.matching.domain.port.in;

import com.organization.app.matching.domain.model.MatchRequest;

public interface ApproveMatchRequestUseCase {
    MatchRequest approve(Long requestId);
}
