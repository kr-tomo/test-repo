package com.organization.app.qa.adapter.out.persistence;

import com.organization.app.qa.domain.model.InstantQA;
import com.organization.app.qa.domain.model.QAAnswer;
import com.organization.app.qa.domain.port.out.InstantQARepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class InstantQAPersistenceAdapter implements InstantQARepositoryPort {

    private final JpaInstantQARepository instantQARepository;
    private final JpaQAAnswerRepository answerRepository;

    @Override
    public InstantQA save(InstantQA instantQA) {
        InstantQAEntity entity = toEntity(instantQA);
        InstantQAEntity saved = instantQARepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<InstantQA> findById(Long id) {
        return instantQARepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<InstantQA> findByFieldIds(List<Long> fieldIds) {
        return instantQARepository.findByFieldIdInOrderByPostedAtDesc(fieldIds).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<InstantQA> findByMenteeAccountId(Long menteeAccountId) {
        return instantQARepository.findByMenteeAccountIdOrderByPostedAtDesc(menteeAccountId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public QAAnswer saveAnswer(QAAnswer answer) {
        QAAnswerEntity entity = toAnswerEntity(answer);
        QAAnswerEntity saved = answerRepository.save(entity);
        return toAnswerDomain(saved);
    }

    @Override
    public Optional<QAAnswer> findAnswerById(Long id) {
        return answerRepository.findById(id).map(this::toAnswerDomain);
    }

    @Override
    public List<QAAnswer> findAnswersByInstantQAId(Long instantQAId) {
        return answerRepository.findByInstantQAIdOrderByAnsweredAtAsc(instantQAId).stream()
                .map(this::toAnswerDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsAnswerByInstantQAIdAndMentorAccountId(Long instantQAId, Long mentorAccountId) {
        return answerRepository.existsByInstantQAIdAndMentorAccountId(instantQAId, mentorAccountId);
    }

    private InstantQA toDomain(InstantQAEntity entity) {
        return InstantQA.builder()
                .id(entity.getId())
                .menteeAccountId(entity.getMenteeAccountId())
                .fieldId(entity.getFieldId())
                .content(entity.getContent())
                .postedAt(entity.getPostedAt())
                .build();
    }

    private InstantQAEntity toEntity(InstantQA domain) {
        return InstantQAEntity.builder()
                .id(domain.getId())
                .menteeAccountId(domain.getMenteeAccountId())
                .fieldId(domain.getFieldId())
                .content(domain.getContent())
                .postedAt(domain.getPostedAt())
                .build();
    }

    private QAAnswer toAnswerDomain(QAAnswerEntity entity) {
        return QAAnswer.builder()
                .id(entity.getId())
                .instantQAId(entity.getInstantQAId())
                .mentorAccountId(entity.getMentorAccountId())
                .content(entity.getContent())
                .answeredAt(entity.getAnsweredAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private QAAnswerEntity toAnswerEntity(QAAnswer domain) {
        return QAAnswerEntity.builder()
                .id(domain.getId())
                .instantQAId(domain.getInstantQAId())
                .mentorAccountId(domain.getMentorAccountId())
                .content(domain.getContent())
                .answeredAt(domain.getAnsweredAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}
