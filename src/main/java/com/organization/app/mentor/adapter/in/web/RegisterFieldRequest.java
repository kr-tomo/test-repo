package com.organization.app.mentor.adapter.in.web;

import jakarta.validation.constraints.NotNull;

public record RegisterFieldRequest(
        @NotNull(message = "FIELD_REQUIRED")
        Long fieldId
) {}
