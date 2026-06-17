package com.organization.app.qa.adapter.in.web;

import com.organization.app.qa.domain.model.InstantQA;
import com.organization.app.qa.domain.port.in.GetInstantQAsUseCase;
import com.organization.app.qa.domain.port.in.GetMyInstantQAsUseCase;
import com.organization.app.qa.domain.port.in.PostInstantQAUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Instant QA", description = "인스턴스 Q&A API")
@RestController
@RequestMapping("/instant-qas")
@RequiredArgsConstructor
public class InstantQAController {

    private final PostInstantQAUseCase postInstantQAUseCase;
    private final GetInstantQAsUseCase getInstantQAsUseCase;
    private final GetMyInstantQAsUseCase getMyInstantQAsUseCase;

    @Operation(summary = "Q&A 등록", description = "멘티가 새로운 질문을 등록합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InstantQAResponse post(@RequestBody PostInstantQARequest request) {
        // TODO: Get menteeAccountId from security context
        Long menteeAccountId = 1L; 
        InstantQA instantQA = postInstantQAUseCase.post(menteeAccountId, request.fieldId(), request.content());
        return InstantQAResponse.from(instantQA);
    }

    @Operation(summary = "Q&A 목록 조회 (멘토용)", description = "멘토가 자신의 전문 분야에 해당하는 질문 목록을 조회합니다.")
    @GetMapping
    public List<InstantQAResponse> getForMentor() {
        // TODO: Get mentorAccountId from security context
        Long mentorAccountId = 2L;
        return getInstantQAsUseCase.getInstantQAsForMentor(mentorAccountId).stream()
                .map(InstantQAResponse::from)
                .collect(Collectors.toList());
    }

    @Operation(summary = "내 Q&A 목록 조회 (멘티용)", description = "멘티가 자신이 등록한 질문 목록을 조회합니다.")
    @GetMapping("/me")
    public List<InstantQAResponse> getMyQAs() {
        // TODO: Get menteeAccountId from security context
        Long menteeAccountId = 1L;
        return getMyInstantQAsUseCase.getMyInstantQAs(menteeAccountId).stream()
                .map(InstantQAResponse::from)
                .collect(Collectors.toList());
    }

    public record PostInstantQARequest(Long fieldId, String content) {}

    public record InstantQAResponse(
            Long id,
            Long fieldId,
            String content,
            LocalDateTime postedAt
    ) {
        public static InstantQAResponse from(InstantQA domain) {
            return new InstantQAResponse(domain.getId(), domain.getFieldId(), domain.getContent(), domain.getPostedAt());
        }
    }
}
