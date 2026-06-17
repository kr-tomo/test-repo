package com.organization.app.matching.application.service;

import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import com.organization.app.matching.domain.exception.MatchRequestException;
import com.organization.app.matching.domain.model.MatchRequest;
import com.organization.app.matching.domain.port.out.MatchRequestRepositoryPort;
import com.organization.app.mentor.domain.model.MentorProfile;
import com.organization.app.mentor.domain.port.out.MentorProfileRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MatchRequestServiceTest {

    @Mock
    private MatchRequestRepositoryPort matchRequestRepository;
    @Mock
    private FieldRepositoryPort fieldRepository;
    @Mock
    private MentorProfileRepositoryPort mentorProfileRepository;

    @InjectMocks
    private MatchRequestService matchRequestService;

    @Test
    @DisplayName("[TC-MR-001] 분야 기반 매칭 요청 성공")
    void createSuccess() {
        Long menteeId = 1L;
        Long fieldId = 10L;
        given(fieldRepository.findById(fieldId)).willReturn(Optional.of(Field.builder().id(fieldId).status(FieldStatus.ACTIVE).build()));
        given(matchRequestRepository.save(any())).willAnswer(invocation -> invocation.getArgument(0));

        MatchRequest result = matchRequestService.create(menteeId, fieldId, null);

        assertThat(result.getMenteeAccountId()).isEqualTo(menteeId);
        assertThat(result.getFieldId()).isEqualTo(fieldId);
        assertThat(result.getMentorAccountId()).isNull();
    }

    @Test
    @DisplayName("[TC-MR-007] 멘토가 아닐 때 요청 실패")
    void createFailNotMentor() {
        Long menteeId = 1L;
        Long fieldId = 10L;
        Long mentorId = 7L;
        given(fieldRepository.findById(fieldId)).willReturn(Optional.of(Field.builder().id(fieldId).status(FieldStatus.ACTIVE).build()));
        given(mentorProfileRepository.findByAccountId(mentorId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> matchRequestService.create(menteeId, fieldId, mentorId))
                .isInstanceOf(MatchRequestException.class)
                .hasFieldOrPropertyWithValue("code", "NOT_A_MENTOR");
    }
}
