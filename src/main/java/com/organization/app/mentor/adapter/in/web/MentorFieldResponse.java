package com.organization.app.mentor.adapter.in.web;

import com.organization.app.mentor.domain.model.MentorField;
import com.organization.app.mentor.domain.model.RegistrationStatus;

public record MentorFieldResponse(
        Long id,
        Long fieldId,
        RegistrationStatus status,
        Boolean visible
) {
    public static MentorFieldResponse from(MentorField registration) {
        return new MentorFieldResponse(
                registration.getId(),
                registration.getFieldId(),
                registration.getStatus(),
                registration.getVisible()
        );
    }
}
