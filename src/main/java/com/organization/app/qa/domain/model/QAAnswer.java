package com.organization.app.qa.domain.model;

import com.organization.app.qa.domain.exception.InstantQAException;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class QAAnswer {
    private Long id;
    private Long instantQAId;
    private Long mentorAccountId;
    private String content;
    private LocalDateTime answeredAt;
    private LocalDateTime updatedAt;

    public static QAAnswer create(Long instantQAId, Long mentorAccountId, String content) {
        validateContent(content);
        return QAAnswer.builder()
                .instantQAId(instantQAId)
                .mentorAccountId(mentorAccountId)
                .content(content)
                .answeredAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void updateContent(Long mentorAccountId, String content) {
        if (!this.mentorAccountId.equals(mentorAccountId)) {
            throw InstantQAException.forbidden("본인의 답변만 수정할 수 있습니다.");
        }
        validateContent(content);
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InstantQAException("ANSWER_CONTENT_REQUIRED", "답변 내용은 필수입니다.", 400);
        }
        if (content.length() > 2000) {
            throw new InstantQAException("ANSWER_CONTENT_TOO_LONG", "답변 내용은 2000자 이하여야 합니다.", 400);
        }
    }
}
