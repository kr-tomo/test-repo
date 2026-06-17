package com.organization.app.mentor.domain.exception;

import lombok.Getter;

@Getter
public class MentorProfileException extends RuntimeException {
    private final String code;
    private final int status;

    public MentorProfileException(String code, String message, int status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public static MentorProfileException mentorProfileNotFound() {
        return new MentorProfileException("MENTOR_PROFILE_NOT_FOUND", "존재하지 않는 멘토 프로필입니다.", 404);
    }

    public static MentorProfileException careerNotFound() {
        return new MentorProfileException("CAREER_NOT_FOUND", "존재하지 않는 경력 항목입니다.", 404);
    }

    public static MentorProfileException forbidden() {
        return new MentorProfileException("FORBIDDEN", "본인 항목 아님 또는 권한 없음", 403);
    }

    public static MentorProfileException mentorFieldNotFound() {
        return new MentorProfileException("MENTOR_FIELD_NOT_FOUND", "존재하지 않는 분야 등록 항목입니다.", 404);
    }

    public static MentorProfileException mentorFieldDuplicate() {
        return new MentorProfileException("MENTOR_FIELD_DUPLICATE", "이미 등록 중이거나 승인된 분야입니다.", 409);
    }

    public static MentorProfileException modificationNotAllowed() {
        return new MentorProfileException("MODIFICATION_NOT_ALLOWED", "검토 전 상태인 항목만 수정할 수 있습니다.", 422);
    }

    public static MentorProfileException withdrawalNotAllowed() {
        return new MentorProfileException("WITHDRAWAL_NOT_ALLOWED", "검토 전 상태인 항목만 철회할 수 있습니다.", 422);
    }

    public static MentorProfileException visibilityChangeNotAllowed() {
        return new MentorProfileException("VISIBILITY_CHANGE_NOT_ALLOWED", "승인된 항목만 노출 설정을 변경할 수 있습니다.", 422);
    }

    public static MentorProfileException reviewTargetNotPending() {
        return new MentorProfileException("REVIEW_TARGET_NOT_PENDING", "검토 전 상태인 항목만 검토할 수 있습니다.", 422);
    }
}
