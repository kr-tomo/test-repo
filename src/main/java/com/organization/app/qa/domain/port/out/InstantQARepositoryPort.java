package com.organization.app.qa.domain.port.out;

import com.organization.app.qa.domain.model.InstantQA;
import com.organization.app.qa.domain.model.QAAnswer;
import java.util.List;
import java.util.Optional;

public interface InstantQARepositoryPort {
    InstantQA save(InstantQA instantQA);
    Optional<InstantQA> findById(Long id);
    List<InstantQA> findByFieldIds(List<Long> fieldIds);
    List<InstantQA> findByMenteeAccountId(Long menteeAccountId);
    
    QAAnswer saveAnswer(QAAnswer answer);
    Optional<QAAnswer> findAnswerById(Long id);
    List<QAAnswer> findAnswersByInstantQAId(Long instantQAId);
    boolean existsAnswerByInstantQAIdAndMentorAccountId(Long instantQAId, Long mentorAccountId);
}
