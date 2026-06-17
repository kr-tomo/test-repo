package com.organization.app.qa.application.service;

import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import com.organization.app.mentor.domain.model.MentorField;
import com.organization.app.mentor.domain.model.MentorProfile;
import com.organization.app.mentor.domain.model.RegistrationStatus;
import com.organization.app.mentor.domain.port.out.MentorProfileRepositoryPort;
import com.organization.app.qa.domain.exception.InstantQAException;
import com.organization.app.qa.domain.model.InstantQA;
import com.organization.app.qa.domain.model.QAAnswer;
import com.organization.app.qa.domain.port.in.GetInstantQAsUseCase;
import com.organization.app.qa.domain.port.in.GetMyInstantQAsUseCase;
import com.organization.app.qa.domain.port.in.PostInstantQAUseCase;
import com.organization.app.qa.domain.port.out.InstantQARepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InstantQAService implements PostInstantQAUseCase, GetInstantQAsUseCase, GetMyInstantQAsUseCase {

    private final InstantQARepositoryPort instantQARepository;
    private final FieldRepositoryPort fieldRepository;
    private final MentorProfileRepositoryPort mentorProfileRepository;

    @Override
    @Transactional
    public InstantQA post(Long menteeAccountId, Long fieldId, String content) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> InstantQAException.invalidField("존재하지 않는 분야입니다."));
        
        if (field.getStatus() != FieldStatus.ACTIVE) {
            throw InstantQAException.invalidField("비활성화된 분야입니다.");
        }

        InstantQA instantQA = InstantQA.create(menteeAccountId, fieldId, content);
        return instantQARepository.save(instantQA);
    }

    @Override
    public List<InstantQA> getInstantQAsForMentor(Long mentorAccountId) {
        MentorProfile profile = mentorProfileRepository.findByAccountId(mentorAccountId)
                .orElseThrow(() -> InstantQAException.forbidden("멘토 프로필이 존재하지 않습니다."));

        List<Long> availableFieldIds = profile.getMentorFields().stream()
                .filter(mf -> mf.getStatus() == RegistrationStatus.APPROVED && mf.getVisible())
                .map(MentorField::getFieldId)
                .collect(Collectors.toList());

        if (availableFieldIds.isEmpty()) {
            return Collections.emptyList();
        }

        return instantQARepository.findByFieldIds(availableFieldIds);
    }

    @Override
    public List<InstantQA> getMyInstantQAs(Long menteeAccountId) {
        return instantQARepository.findByMenteeAccountId(menteeAccountId);
    }

    @Override
    public List<QAAnswer> getAnswers(Long menteeAccountId, Long qaId) {
        InstantQA instantQA = instantQARepository.findById(qaId)
                .orElseThrow(() -> InstantQAException.notFound("존재하지 않는 질문입니다."));

        if (!instantQA.getMenteeAccountId().equals(menteeAccountId)) {
            throw InstantQAException.forbidden("본인의 질문 답변만 조회할 수 있습니다.");
        }

        return instantQARepository.findAnswersByInstantQAId(qaId);
    }
}
