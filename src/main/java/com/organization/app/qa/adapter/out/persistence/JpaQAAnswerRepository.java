package com.organization.app.qa.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaQAAnswerRepository extends JpaRepository<QAAnswerEntity, Long> {
    List<QAAnswerEntity> findByInstantQAIdOrderByAnsweredAtAsc(Long instantQAId);
    boolean existsByInstantQAIdAndMentorAccountId(Long instantQAId, Long mentorAccountId);
}
