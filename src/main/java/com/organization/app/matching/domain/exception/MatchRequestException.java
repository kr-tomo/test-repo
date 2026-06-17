package com.organization.app.matching.domain.exception;

import lombok.Getter;

@Getter
public class MatchRequestException extends RuntimeException {
    private final String code;
    private final int status;

    public MatchRequestException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public static MatchRequestException notFound() {
        return new MatchRequestException("MATCH_REQUEST_NOT_FOUND", "존재하지 않는 매칭 요청입니다.", 404);
    }

    public static MatchRequestException notAByMentor() {
        return new MatchRequestException("NOT_A_MENTOR", "멘토 계정만 지정할 수 있습니다.", 422);
    }

    public static MatchRequestException noCapacity() {
        return new MatchRequestException("MENTOR_NO_CAPACITY", "해당 멘토는 현재 매칭 여력이 없습니다.", 422);
    }

    public static MatchRequestException notAssignable() {
        return new MatchRequestException("MATCH_REQUEST_NOT_ASSIGNABLE", "멘토 지정 불가 상태입니다.", 422);
    }

    public static MatchRequestException mentorNotAssigned() {
        return new MatchRequestException("MENTOR_NOT_ASSIGNED", "멘토가 지정되지 않은 요청은 승인할 수 없습니다.", 422);
    }

    public static MatchRequestException alreadyProcessed() {
        return new MatchRequestException("MATCH_REQUEST_ALREADY_PROCESSED", "이미 처리된 요청입니다.", 422);
    }

    public static MatchRequestException forbidden() {
        return new MatchRequestException("FORBIDDEN", "권한이 없습니다.", 403);
    }
}
