package com.organization.app.matching.adapter.in.web;

import com.organization.app.matching.domain.model.MatchRequest;
import com.organization.app.matching.domain.port.in.CreateMatchRequestUseCase;
import com.organization.app.matching.domain.port.in.GetMyMatchRequestsQuery;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Match Request", description = "매칭 요청 API")
@RestController
@RequestMapping("/match-requests")
@RequiredArgsConstructor
public class MatchRequestController {

    private final CreateMatchRequestUseCase createMatchRequestUseCase;
    private final GetMyMatchRequestsQuery getMyMatchRequestsQuery;

    @Operation(summary = "매칭 요청 생성", description = "멘티가 분야를 지정하거나 특정 멘토를 지정하여 매칭을 요청합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MatchRequestResponse create(@RequestBody CreateMatchRequest request) {
        // TODO: Get menteeAccountId from security context
        Long menteeAccountId = 1L;
        MatchRequest matchRequest = createMatchRequestUseCase.create(menteeAccountId, request.fieldId(), request.mentorAccountId());
        return MatchRequestResponse.from(matchRequest);
    }

    @Operation(summary = "내 매칭 요청 목록 조회", description = "멘티가 자신이 요청한 매칭 목록을 조회합니다.")
    @GetMapping("/me")
    public List<MatchRequestResponse> getMyRequests() {
        // TODO: Get menteeAccountId from security context
        Long menteeAccountId = 1L;
        return getMyMatchRequestsQuery.getMyRequests(menteeAccountId).stream()
                .map(MatchRequestResponse::from)
                .collect(Collectors.toList());
    }

    public record CreateMatchRequest(Long fieldId, Long mentorAccountId) {}

    public record MatchRequestResponse(
            Long id,
            Long fieldId,
            Long mentorAccountId,
            String status,
            String requestedAt
    ) {
        public static MatchRequestResponse from(MatchRequest domain) {
            return new MatchRequestResponse(
                    domain.getId(),
                    domain.getFieldId(),
                    domain.getMentorAccountId(),
                    domain.getStatus().name(),
                    domain.getRequestedAt().toString()
            );
        }
    }
}
