package com.organization.app.matching.domain.model;

import com.organization.app.matching.domain.exception.MatchRequestException;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MatchRequest {
    private Long id;
    private Long menteeAccountId;
    private Long mentorAccountId;
    private Long fieldId;
    private MatchRequestStatus status;
    private LocalDateTime requestedAt;

    public static MatchRequest create(Long menteeAccountId, Long fieldId, Long mentorAccountId) {
        if (fieldId == null) {
            throw new MatchRequestException("FIELD_REQUIRED", "분야는 필수입니다.", 400);
        }
        return MatchRequest.builder()
                .menteeAccountId(menteeAccountId)
                .fieldId(fieldId)
                .mentorAccountId(mentorAccountId)
                .status(MatchRequestStatus.REQUESTED)
                .requestedAt(LocalDateTime.now())
                .build();
    }

    public void assignMentor(Long mentorAccountId) {
        if (this.status == MatchRequestStatus.APPROVED || this.status == MatchRequestStatus.CANCELLED) {
            throw MatchRequestException.notAssignable();
        }
        this.mentorAccountId = mentorAccountId;
        this.status = MatchRequestStatus.MENTOR_ASSIGNED;
    }

    public void approve() {
        if (this.mentorAccountId == null) {
            throw MatchRequestException.mentorNotAssigned();
        }
        if (this.status == MatchRequestStatus.APPROVED || this.status == MatchRequestStatus.CANCELLED) {
            throw MatchRequestException.alreadyProcessed();
        }
        this.status = MatchRequestStatus.APPROVED;
    }

    public void cancel() {
        if (this.status == MatchRequestStatus.APPROVED || this.status == MatchRequestStatus.CANCELLED) {
            throw MatchRequestException.alreadyProcessed();
        }
        this.status = MatchRequestStatus.CANCELLED;
    }
}
