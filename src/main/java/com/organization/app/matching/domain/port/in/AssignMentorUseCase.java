package com.organization.app.matching.domain.port.in;

import com.organization.app.matching.domain.model.MatchRequest;

public interface AssignMentorUseCase {
    MatchRequest assignMentor(Long requestId, Long mentorAccountId);
}
