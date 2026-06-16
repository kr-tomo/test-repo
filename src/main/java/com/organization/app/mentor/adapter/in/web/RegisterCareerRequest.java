package com.organization.app.mentor.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterCareerRequest(
        @NotBlank(message = "CAREER_CONTENT_REQUIRED")
        @Size(max = 2000, message = "CAREER_CONTENT_TOO_LONG")
        String content
) {}
