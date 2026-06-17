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
import com.organization.app.qa.domain.port.out.InstantQARepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class InstantQAServiceTest {

    @Mock
    private InstantQARepositoryPort instantQARepository;
    @Mock
    private FieldRepositoryPort fieldRepository;
    @Mock
    private MentorProfileRepositoryPort mentorProfileRepository;

    @InjectMocks
    private InstantQAService instantQAService;

    @Test
    @DisplayName("[TC-QA-010] 멘토 전문 분야 Q&A 조회")
    void getForMentor() {
        Long mentorAccountId = 1L;
        MentorProfile profile = MentorProfile.builder()
                .accountId(mentorAccountId)
                .mentorFields(List.of(
                        MentorField.builder().fieldId(10L).status(RegistrationStatus.APPROVED).visible(true).build(),
                        MentorField.builder().fieldId(11L).status(RegistrationStatus.APPROVED).visible(true).build(),
                        MentorField.builder().fieldId(12L).status(RegistrationStatus.PENDING).visible(true).build(),
                        MentorField.builder().fieldId(13L).status(RegistrationStatus.APPROVED).visible(false).build()
                ))
                .build();

        given(mentorProfileRepository.findByAccountId(mentorAccountId)).willReturn(Optional.of(profile));
        given(instantQARepository.findByFieldIds(List.of(10L, 11L)))
                .willReturn(List.of(
                        InstantQA.builder().id(100L).fieldId(10L).build(),
                        InstantQA.builder().id(101L).fieldId(11L).build()
                ));

        List<InstantQA> results = instantQAService.getInstantQAsForMentor(mentorAccountId);

        assertThat(results).hasSize(2);
        assertThat(results).extracting(InstantQA::getId).containsExactly(100L, 101L);
    }

    @Test
    @DisplayName("[TC-QA-007] 비활성화 분야로 등록 시 예외 발생")
    void postWithInactiveField() {
        Long fieldId = 10L;
        Field field = Field.builder().id(fieldId).status(FieldStatus.INACTIVE).build();
        given(fieldRepository.findById(fieldId)).willReturn(Optional.of(field));

        assertThatThrownBy(() -> instantQAService.post(1L, fieldId, "질문"))
                .isInstanceOf(InstantQAException.class)
                .hasFieldOrPropertyWithValue("code", "FIELD_NOT_FOUND_OR_INACTIVE");
    }
}
