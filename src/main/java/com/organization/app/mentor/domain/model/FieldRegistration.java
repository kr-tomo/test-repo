package com.organization.app.mentor.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FieldRegistration {
    private Long id;
    private Long mentorProfileId;
    private Long fieldId;
    private RegistrationStatus status;
    private Boolean visible;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FieldRegistration create(Long mentorProfileId, Long fieldId) {
        return FieldRegistration.builder()
                .mentorProfileId(mentorProfileId)
                .fieldId(fieldId)
                .status(RegistrationStatus.PENDING)
                .build();
    }

    public void updateField(Long fieldId) {
        if (this.status != RegistrationStatus.PENDING) {
            throw new IllegalStateException("MODIFICATION_NOT_ALLOWED");
        }
        this.fieldId = fieldId;
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
