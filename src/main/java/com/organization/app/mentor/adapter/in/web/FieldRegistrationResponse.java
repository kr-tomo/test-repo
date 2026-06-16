package com.organization.app.mentor.adapter.in.web;

import com.organization.app.mentor.domain.model.FieldRegistration;
import com.organization.app.mentor.domain.model.RegistrationStatus;

public record FieldRegistrationResponse(
        Long id,
        Long fieldId,
        RegistrationStatus status,
        Boolean visible
) {
    public static FieldRegistrationResponse from(FieldRegistration registration) {
        return new FieldRegistrationResponse(
                registration.getId(),
                registration.getFieldId(),
                registration.getStatus(),
                registration.getVisible()
        );
    }
}
