package com.organization.app.field.adapter.in.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFieldRequest(
    @NotBlank(message = "FIELD_NAME_REQUIRED")
    @Size(max = 100, message = "FIELD_NAME_TOO_LONG")
    String name,
    Long parentId
) {}
