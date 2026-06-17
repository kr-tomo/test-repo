package com.organization.app.qa.application.service;

import com.organization.app.qa.domain.exception.InstantQAException;
import com.organization.app.qa.domain.model.InstantQA;
import com.organization.app.qa.domain.model.QAAnswer;
import com.organization.app.qa.domain.port.in.AnswerInstantQAUseCase;
import com.organization.app.qa.domain.port.in.UpdateQAAnswerUseCase;
import com.organization.app.qa.domain.port.out.InstantQARepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QAAnswerService implements AnswerInstantQAUseCase, UpdateQAAnswerUseCase {

    private final InstantQARepositoryPort instantQARepository;

    @Override
    @Transactional
    public QAAnswer answer(Long mentorAccountId, Long qaId, String content) {
        InstantQA instantQA = instantQARepository.findById(qaId)
                .orElseThrow(() -> InstantQAException.notFound("존재하지 않는 질문입니다."));

        if (instantQARepository.existsAnswerByInstantQAIdAndMentorAccountId(qaId, mentorAccountId)) {
            throw InstantQAException.alreadyAnswered();
        }

        QAAnswer answer = QAAnswer.create(qaId, mentorAccountId, content);
        return instantQARepository.saveAnswer(answer);
    }

    @Override
    @Transactional
    public QAAnswer update(Long mentorAccountId, Long qaId, Long answerId, String content) {
        QAAnswer answer = instantQARepository.findAnswerById(answerId)
                .orElseThrow(() -> InstantQAException.answerNotFound("존재하지 않는 답변입니다."));

        if (!answer.getInstantQAId().equals(qaId)) {
            throw InstantQAException.notFound("질문에 해당 답변이 존재하지 않습니다.");
        }

        answer.updateContent(mentorAccountId, content);
        return instantQARepository.saveAnswer(answer);
    }
}
