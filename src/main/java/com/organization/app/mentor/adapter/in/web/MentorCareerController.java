package com.organization.app.mentor.adapter.in.web;

import com.organization.app.mentor.domain.model.CareerEntry;
import com.organization.app.mentor.domain.port.in.ModifyCareerUseCase;
import com.organization.app.mentor.domain.port.in.RegisterCareerUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mentor Career", description = "멘토 경력 관리 API")
@RestController
@RequestMapping("/mentor-profiles/me/careers")
@RequiredArgsConstructor
public class MentorCareerController {

    private final RegisterCareerUseCase registerCareerUseCase;
    private final ModifyCareerUseCase modifyCareerUseCase;

    // TODO: In a real app, accountId would come from SecurityContext
    private static final Long DUMMY_ACCOUNT_ID = 1L;

    @Operation(summary = "경력 등록 요청", description = "새로운 경력을 등록 요청합니다. PENDING 상태로 생성됩니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CareerResponse registerCareer(@RequestBody @Valid RegisterCareerRequest request) {
        CareerEntry entry = registerCareerUseCase.registerCareer(
                RegisterCareerUseCase.Command.builder()
                        .accountId(DUMMY_ACCOUNT_ID)
                        .content(request.content())
                        .build()
        );
        return CareerResponse.from(entry);
    }

    @Operation(summary = "경력 수정", description = "검토 전 상태(PENDING)인 경력을 수정합니다.")
    @PatchMapping("/{careerId}")
    public CareerResponse updateCareer(
            @PathVariable Long careerId,
            @RequestBody @Valid RegisterCareerRequest request) {
        CareerEntry entry = modifyCareerUseCase.modifyCareer(
                ModifyCareerUseCase.Command.builder()
                        .accountId(DUMMY_ACCOUNT_ID)
                        .careerId(careerId)
                        .content(request.content())
                        .build()
        );
        return CareerResponse.from(entry);
    }

    @Operation(summary = "경력 철회", description = "검토 전 상태(PENDING)인 경력을 철회합니다.")
    @DeleteMapping("/{careerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawCareer(@PathVariable Long careerId) {
        modifyCareerUseCase.withdrawCareer(DUMMY_ACCOUNT_ID, careerId);
    }

    @Operation(summary = "경력 노출 설정", description = "승인된 상태(APPROVED)인 경력의 노출 여부를 설정합니다.")
    @PatchMapping("/{careerId}/visibility")
    public void updateVisibility(
            @PathVariable Long careerId,
            @RequestBody @Valid UpdateVisibilityRequest request) {
        modifyCareerUseCase.updateVisibility(DUMMY_ACCOUNT_ID, careerId, request.visible());
    }
}
