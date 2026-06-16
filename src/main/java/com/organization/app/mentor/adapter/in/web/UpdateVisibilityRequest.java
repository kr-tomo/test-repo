package com.organization.app.mentor.adapter.in.web;

import jakarta.validation.constraints.NotNull;

public record UpdateVisibilityRequest(
        @NotNull
        Boolean visible
) {}
