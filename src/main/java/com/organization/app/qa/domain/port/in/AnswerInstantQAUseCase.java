package com.organization.app.qa.domain.port.in;

import com.organization.app.qa.domain.model.QAAnswer;

public interface AnswerInstantQAUseCase {
    QAAnswer answer(Long mentorAccountId, Long qaId, String content);
}
