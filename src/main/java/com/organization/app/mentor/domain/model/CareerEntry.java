package com.organization.app.mentor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class CareerEntry {
    private Long id;
    private Long mentorProfileId;
    private String content;
    private RegistrationStatus status;
    private Boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CareerEntry create(Long mentorProfileId, String content) {
        return CareerEntry.builder()
                .mentorProfileId(mentorProfileId)
                .content(content)
                .status(RegistrationStatus.PENDING)
                .build();
    }

    public void updateContent(String content) {
        if (this.status != RegistrationStatus.PENDING) {
            throw new IllegalStateException("MODIFICATION_NOT_ALLOWED");
        }
        this.content = content;
    }

    public void withdraw() {
        if (this.status != RegistrationStatus.PENDING) {
            throw new IllegalStateException("WITHDRAWAL_NOT_ALLOWED");
        }
        this.status = RegistrationStatus.WITHDRAWN;
    }

    public void updateVisibility(boolean visible) {
        if (this.status != RegistrationStatus.APPROVED) {
            throw new IllegalStateException("VISIBILITY_CHANGE_NOT_ALLOWED");
        }
        this.visible = visible;
    }

    public void approve() {
        if (this.status != RegistrationStatus.PENDING) {
            throw new IllegalStateException("REVIEW_TARGET_NOT_PENDING");
        }
        this.status = RegistrationStatus.APPROVED;
        this.visible = true;
    }

    public void reject() {
        if (this.status != RegistrationStatus.PENDING) {
            throw new IllegalStateException("REVIEW_TARGET_NOT_PENDING");
        }
        this.status = RegistrationStatus.REJECTED;
    }
}
