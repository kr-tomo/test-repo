package com.organization.app.qa.domain.model;

import com.organization.app.qa.domain.exception.InstantQAException;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InstantQA {
    private Long id;
    private Long menteeAccountId;
    private Long fieldId;
    private String content;
    private LocalDateTime postedAt;
    @Builder.Default
    private List<QAAnswer> answers = new ArrayList<>();

    public static InstantQA create(Long menteeAccountId, Long fieldId, String content) {
        validateContent(content);
        if (fieldId == null) {
            throw new InstantQAException("FIELD_REQUIRED", "분야는 필수입니다.", 400);
        }
        return InstantQA.builder()
                .menteeAccountId(menteeAccountId)
                .fieldId(fieldId)
                .content(content)
                .postedAt(LocalDateTime.now())
                .build();
    }

    public void addAnswer(QAAnswer answer) {
        if (this.answers.stream().anyMatch(a -> a.getMentorAccountId().equals(answer.getMentorAccountId()))) {
            throw InstantQAException.alreadyAnswered();
        }
        this.answers.add(answer);
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
            throw new InstantQAException("QA_CONTENT_REQUIRED", "질문 내용은 필수입니다.", 400);
        }
        if (content.length() > 1000) {
            throw new InstantQAException("QA_CONTENT_TOO_LONG", "질문 내용은 1000자 이하여야 합니다.", 400);
        }
    }
}
