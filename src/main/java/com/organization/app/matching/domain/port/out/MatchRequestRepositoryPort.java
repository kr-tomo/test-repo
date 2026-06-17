package com.organization.app.matching.domain.port.out;

import com.organization.app.matching.domain.model.MatchRequest;
import java.util.List;
import java.util.Optional;

public interface MatchRequestRepositoryPort {
    MatchRequest save(MatchRequest matchRequest);
    Optional<MatchRequest> findById(Long id);
    List<MatchRequest> findByMenteeAccountId(Long menteeAccountId);
}
