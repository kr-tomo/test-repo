package com.organization.app.qa.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaInstantQARepository extends JpaRepository<InstantQAEntity, Long> {
    List<InstantQAEntity> findByFieldIdInOrderByPostedAtDesc(List<Long> fieldIds);
    List<InstantQAEntity> findByMenteeAccountIdOrderByPostedAtDesc(Long menteeAccountId);
}
