package com.organization.app.matching.application.service;

import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import com.organization.app.matching.domain.exception.MatchRequestException;
import com.organization.app.matching.domain.model.MatchRequest;
import com.organization.app.matching.domain.port.in.ApproveMatchRequestUseCase;
import com.organization.app.matching.domain.port.in.AssignMentorUseCase;
import com.organization.app.matching.domain.port.in.CreateMatchRequestUseCase;
import com.organization.app.matching.domain.port.in.GetMyMatchRequestsQuery;
import com.organization.app.matching.domain.port.out.MatchRequestRepositoryPort;
import com.organization.app.mentor.domain.port.out.MentorProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchRequestService implements 
        CreateMatchRequestUseCase, 
        AssignMentorUseCase, 
        ApproveMatchRequestUseCase,
        GetMyMatchRequestsQuery {

    private final MatchRequestRepositoryPort matchRequestRepository;
    private final FieldRepositoryPort fieldRepository;
    private final MentorProfileRepositoryPort mentorProfileRepository;

    @Override
    @Transactional
    public MatchRequest create(Long menteeAccountId, Long fieldId, Long mentorAccountId) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new MatchRequestException("FIELD_NOT_FOUND_OR_INACTIVE", "존재하지 않는 분야입니다.", 422));
        
        if (field.getStatus() != FieldStatus.ACTIVE) {
            throw new MatchRequestException("FIELD_NOT_FOUND_OR_INACTIVE", "비활성화된 분야입니다.", 422);
        }

        if (mentorAccountId != null) {
            validateMentor(mentorAccountId);
        }

        MatchRequest matchRequest = MatchRequest.create(menteeAccountId, fieldId, mentorAccountId);
        return matchRequestRepository.save(matchRequest);
    }

    @Override
    @Transactional
    public MatchRequest assignMentor(Long requestId, Long mentorAccountId) {
        MatchRequest matchRequest = matchRequestRepository.findById(requestId)
                .orElseThrow(MatchRequestException::notFound);

        validateMentor(mentorAccountId);

        matchRequest.assignMentor(mentorAccountId);
        return matchRequestRepository.save(matchRequest);
    }

    @Override
    @Transactional
    public MatchRequest approve(Long requestId) {
        MatchRequest matchRequest = matchRequestRepository.findById(requestId)
                .orElseThrow(MatchRequestException::notFound);

        matchRequest.approve();
        return matchRequestRepository.save(matchRequest);
    }

    @Override
    public List<MatchRequest> getMyRequests(Long menteeAccountId) {
        return matchRequestRepository.findByMenteeAccountId(menteeAccountId);
    }

    private void validateMentor(Long mentorAccountId) {
        // Using MentorProfile as a proxy for MENTOR role since Account domain is not explicitly defined in the provided snippets.
        // And for OQ-003, we'll assume a mentor has capacity if their profile exists for now.
        boolean isMentor = mentorProfileRepository.findByAccountId(mentorAccountId).isPresent();
        if (!isMentor) {
            throw MatchRequestException.notAByMentor();
        }
        
        // Placeholder for capacity check (OQ-003)
        // In a real scenario, this would check active match count or a specific capacity flag in MentorProfile.
        // For now, we assume all mentors have capacity if they have a profile.
    }
}
