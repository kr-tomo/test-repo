package com.organization.app.qa.domain.model;

import com.organization.app.qa.domain.exception.InstantQAException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InstantQATest {

    @Test
    @DisplayName("[TC-QA-001] InstantQA 생성 성공")
    void createSuccess() {
        InstantQA qa = InstantQA.create(1L, 10L, "질문 내용");
        assertThat(qa.getMenteeAccountId()).isEqualTo(1L);
        assertThat(qa.getFieldId()).isEqualTo(10L);
        assertThat(qa.getContent()).isEqualTo("질문 내용");
        assertThat(qa.getPostedAt()).isNotNull();
    }

    @Test
    @DisplayName("[TC-QA-003] content 공백일 때 예외 발생")
    void createWithBlankContent() {
        assertThatThrownBy(() -> InstantQA.create(1L, 10L, "   "))
                .isInstanceOf(InstantQAException.class)
                .hasFieldOrPropertyWithValue("code", "QA_CONTENT_REQUIRED");
    }

    @Test
    @DisplayName("[TC-QA-005] content 정확히 1000자일 때 성공")
    void createWithMaxContent() {
        String content = "a".repeat(1000);
        InstantQA qa = InstantQA.create(1L, 10L, content);
        assertThat(qa.getContent()).hasSize(1000);
    }

    @Test
    @DisplayName("[TC-QA-004] content 1000자 초과 시 예외 발생")
    void createWithTooLongContent() {
        String content = "a".repeat(1001);
        assertThatThrownBy(() -> InstantQA.create(1L, 10L, content))
                .isInstanceOf(InstantQAException.class)
                .hasFieldOrPropertyWithValue("code", "QA_CONTENT_TOO_LONG");
    }

    @Test
    @DisplayName("[TC-QA-020] 동일 멘토 재답변 시 예외 발생")
    void addDuplicateAnswer() {
        InstantQA qa = InstantQA.create(1L, 10L, "질문");
        QAAnswer answer1 = QAAnswer.create(1L, 2L, "답변1");
        qa.addAnswer(answer1);

        QAAnswer answer2 = QAAnswer.create(1L, 2L, "답변2");
        assertThatThrownBy(() -> qa.addAnswer(answer2))
                .isInstanceOf(InstantQAException.class)
                .hasFieldOrPropertyWithValue("code", "ANSWER_ALREADY_EXISTS");
    }

    @Test
    @DisplayName("[TC-QA-022] 다른 멘토는 동일 질문에 답변 가능")
    void addDifferentMentorAnswer() {
        InstantQA qa = InstantQA.create(1L, 10L, "질문");
        QAAnswer answer1 = QAAnswer.create(1L, 2L, "답변1");
        qa.addAnswer(answer1);

        QAAnswer answer2 = QAAnswer.create(1L, 3L, "답변2");
        qa.addAnswer(answer2);
        assertThat(qa.getAnswers()).hasSize(2);
    }
}
