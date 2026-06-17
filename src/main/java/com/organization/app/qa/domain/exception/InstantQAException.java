package com.organization.app.qa.domain.exception;

import lombok.Getter;

@Getter
public class InstantQAException extends RuntimeException {
    private final String code;
    private final int status;

    public InstantQAException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public static InstantQAException notFound(String message) {
        return new InstantQAException("INSTANT_QA_NOT_FOUND", message, 404);
    }

    public static InstantQAException answerNotFound(String message) {
        return new InstantQAException("QA_ANSWER_NOT_FOUND", message, 404);
    }

    public static InstantQAException alreadyAnswered() {
        return new InstantQAException("ANSWER_ALREADY_EXISTS", "이미 답변한 질문입니다.", 409);
    }

    public static InstantQAException forbidden(String message) {
        return new InstantQAException("FORBIDDEN", message, 403);
    }

    public static InstantQAException invalidField(String message) {
        return new InstantQAException("FIELD_NOT_FOUND_OR_INACTIVE", message, 422);
    }
}
