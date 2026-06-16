package com.organization.app.field.domain.exception;

import lombok.Getter;

@Getter
public class FieldException extends RuntimeException {
    private final String code;
    private final int status;

    public FieldException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public static FieldException duplicateName() {
        return new FieldException("FIELD_NAME_DUPLICATE", "같은 상위 분야에 동일한 이름의 분야가 존재합니다.", 409);
    }

    public static FieldException parentNotFound() {
        return new FieldException("PARENT_FIELD_NOT_FOUND", "존재하지 않는 상위 분야입니다.", 404);
    }

    public static FieldException parentInactive() {
        return new FieldException("PARENT_FIELD_INACTIVE", "비활성화된 상위 분야입니다.", 422);
    }

    public static FieldException fieldNotFound() {
        return new FieldException("FIELD_NOT_FOUND", "존재하지 않는 분야입니다.", 404);
    }

    public static FieldException alreadyInactive() {
        return new FieldException("FIELD_ALREADY_INACTIVE", "이미 비활성화된 분야입니다.", 409);
    }
}
