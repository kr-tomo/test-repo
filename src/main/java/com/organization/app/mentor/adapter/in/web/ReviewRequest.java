package com.organization.app.mentor.adapter.in.web;

import jakarta.validation.constraints.NotNull;

public record ReviewRequest(
        @NotNull(message = "REVIEW_ACTION_REQUIRED")
        Action action
) {
    public enum Action {
        APPROVE, REJECT
    }
}
