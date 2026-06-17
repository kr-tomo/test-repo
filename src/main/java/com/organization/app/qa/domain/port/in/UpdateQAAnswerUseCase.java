package com.organization.app.qa.domain.port.in;

import com.organization.app.qa.domain.model.QAAnswer;

public interface UpdateQAAnswerUseCase {
    QAAnswer update(Long mentorAccountId, Long qaId, Long answerId, String content);
}
