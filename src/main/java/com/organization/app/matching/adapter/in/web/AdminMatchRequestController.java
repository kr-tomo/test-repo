package com.organization.app.matching.adapter.in.web;

import com.organization.app.matching.domain.model.MatchRequest;
import com.organization.app.matching.domain.port.in.ApproveMatchRequestUseCase;
import com.organization.app.matching.domain.port.in.AssignMentorUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Match Request", description = "운영자 매칭 관리 API")
@RestController
@RequestMapping("/admin/match-requests")
@RequiredArgsConstructor
public class AdminMatchRequestController {

    private final AssignMentorUseCase assignMentorUseCase;
    private final ApproveMatchRequestUseCase approveMatchRequestUseCase;

    @Operation(summary = "멘토 지정", description = "운영자가 매칭 요청에 멘토를 지정합니다.")
    @PatchMapping("/{requestId}/mentor")
    public MatchRequestController.MatchRequestResponse assignMentor(
            @PathVariable Long requestId,
            @RequestBody AssignMentorRequest request) {
        MatchRequest matchRequest = assignMentorUseCase.assignMentor(requestId, request.mentorAccountId());
        return MatchRequestController.MatchRequestResponse.from(matchRequest);
    }

    @Operation(summary = "매칭 승인", description = "운영자가 매칭 요청을 승인합니다.")
    @PostMapping("/{requestId}/approve")
    public MatchRequestController.MatchRequestResponse approve(@PathVariable Long requestId) {
        MatchRequest matchRequest = approveMatchRequestUseCase.approve(requestId);
        return MatchRequestController.MatchRequestResponse.from(matchRequest);
    }

    public record AssignMentorRequest(Long mentorAccountId) {}
}
