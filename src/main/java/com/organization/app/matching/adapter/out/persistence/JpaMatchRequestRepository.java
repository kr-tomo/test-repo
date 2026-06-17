package com.organization.app.matching.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaMatchRequestRepository extends JpaRepository<MatchRequestEntity, Long> {
    List<MatchRequestEntity> findByMenteeAccountIdOrderByRequestedAtDesc(Long menteeAccountId);
}
