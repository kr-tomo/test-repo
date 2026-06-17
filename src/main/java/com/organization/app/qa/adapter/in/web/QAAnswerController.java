package com.organization.app.qa.adapter.in.web;

import com.organization.app.qa.domain.model.QAAnswer;
import com.organization.app.qa.domain.port.in.AnswerInstantQAUseCase;
import com.organization.app.qa.domain.port.in.GetMyInstantQAsUseCase;
import com.organization.app.qa.domain.port.in.UpdateQAAnswerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "QA Answer", description = "Q&A 답변 API")
@RestController
@RequestMapping("/instant-qas/{qaId}/answers")
@RequiredArgsConstructor
public class QAAnswerController {

    private final AnswerInstantQAUseCase answerInstantQAUseCase;
    private final UpdateQAAnswerUseCase updateQAAnswerUseCase;
    private final GetMyInstantQAsUseCase getMyInstantQAsUseCase;

    @Operation(summary = "답변 목록 조회", description = "멘티가 자신의 질문에 달린 답변 목록을 조회합니다.")
    @GetMapping
    public List<QAAnswerResponse> getAnswers(@PathVariable Long qaId) {
        // TODO: Get menteeAccountId from security context
        Long menteeAccountId = 1L;
        return getMyInstantQAsUseCase.getAnswers(menteeAccountId, qaId).stream()
                .map(QAAnswerResponse::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "답변 작성", description = "멘토가 질문에 답변을 작성합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QAAnswerResponse answer(@PathVariable Long qaId, @RequestBody PostAnswerRequest request) {
        // TODO: Get mentorAccountId from security context
        Long mentorAccountId = 2L;
        QAAnswer answer = answerInstantQAUseCase.answer(mentorAccountId, qaId, request.content());
        return QAAnswerResponse.from(answer);
    }

    @Operation(summary = "답변 수정", description = "멘토가 자신이 작성한 답변을 수정합니다.")
    @PatchMapping("/{answerId}")
    public QAAnswerResponse update(
            @PathVariable Long qaId,
            @PathVariable Long answerId,
            @RequestBody PostAnswerRequest request) {
        // TODO: Get mentorAccountId from security context
        Long mentorAccountId = 2L;
        QAAnswer answer = updateQAAnswerUseCase.update(mentorAccountId, qaId, answerId, request.content());
        return QAAnswerResponse.from(answer);
    }

    public record PostAnswerRequest(String content) {}

    public record QAAnswerResponse(
            Long id,
            Long mentorAccountId,
            String content,
            LocalDateTime answeredAt,
            LocalDateTime updatedAt
    ) {
        public static QAAnswerResponse from(QAAnswer domain) {
            return new QAAnswerResponse(
                    domain.getId(),
                    domain.getMentorAccountId(),
                    domain.getContent(),
                    domain.getAnsweredAt(),
                    domain.getUpdatedAt()
            );
        }
    }
}
