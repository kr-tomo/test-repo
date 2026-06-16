package com.organization.app.mentor.adapter.in.web;

import com.organization.app.mentor.domain.model.CareerEntry;
import com.organization.app.mentor.domain.model.RegistrationStatus;

public record CareerResponse(
        Long id,
        String content,
        RegistrationStatus status,
        Boolean visible
) {
    public static CareerResponse from(CareerEntry entry) {
        return new CareerResponse(entry.getId(), entry.getContent(), entry.getStatus(), entry.getVisible());
    }
}
