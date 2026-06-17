package com.organization.app.matching.domain.model;

import com.organization.app.matching.domain.exception.MatchRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatchRequestTest {

    @Test
    @DisplayName("[TC-MR-001] MatchRequest 생성 성공")
    void createSuccess() {
        MatchRequest mr = MatchRequest.create(1L, 10L, null);
        assertThat(mr.getMenteeAccountId()).isEqualTo(1L);
        assertThat(mr.getFieldId()).isEqualTo(10L);
        assertThat(mr.getMentorAccountId()).isNull();
        assertThat(mr.getStatus()).isEqualTo(MatchRequestStatus.REQUESTED);
    }

    @Test
    @DisplayName("[TC-MR-010] 멘토 지정 성공")
    void assignMentorSuccess() {
        MatchRequest mr = MatchRequest.create(1L, 10L, null);
        mr.assignMentor(7L);
        assertThat(mr.getMentorAccountId()).isEqualTo(7L);
        assertThat(mr.getStatus()).isEqualTo(MatchRequestStatus.MENTOR_ASSIGNED);
    }

    @Test
    @DisplayName("[TC-MR-019] 매칭 승인 성공")
    void approveSuccess() {
        MatchRequest mr = MatchRequest.create(1L, 10L, 7L);
        mr.approve();
        assertThat(mr.getStatus()).isEqualTo(MatchRequestStatus.APPROVED);
    }

    @Test
    @DisplayName("[TC-MR-021] 멘토 미지정 시 승인 실패")
    void approveFailWithoutMentor() {
        MatchRequest mr = MatchRequest.create(1L, 10L, null);
        assertThatThrownBy(mr::approve)
                .isInstanceOf(MatchRequestException.class)
                .hasFieldOrPropertyWithValue("code", "MENTOR_NOT_ASSIGNED");
    }
}
