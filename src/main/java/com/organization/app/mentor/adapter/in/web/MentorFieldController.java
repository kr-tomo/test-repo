package com.organization.app.mentor.adapter.in.web;

import com.organization.app.mentor.domain.model.MentorField;
import com.organization.app.mentor.domain.port.in.ModifyMentorFieldUseCase;
import com.organization.app.mentor.domain.port.in.RegisterFieldUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Mentor Field Registration", description = "멘토 분야 등록 관리 API")
@RestController
@RequestMapping("/mentor-profiles/me/field-registrations")
@RequiredArgsConstructor
public class MentorFieldController {

    private final RegisterFieldUseCase registerFieldUseCase;
    private final ModifyMentorFieldUseCase modifyFieldRegistrationUseCase;

    // TODO: In a real app, accountId would come from SecurityContext
    private static final Long DUMMY_ACCOUNT_ID = 1L;

    @Operation(summary = "분야 등록 요청", description = "새로운 분야를 등록 요청합니다. PENDING 상태로 생성됩니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MentorFieldResponse registerField(@RequestBody @Valid RegisterFieldRequest request) {
        MentorField registration = registerFieldUseCase.registerField(
                RegisterFieldUseCase.Command.builder()
                        .accountId(DUMMY_ACCOUNT_ID)
                        .fieldId(request.fieldId())
                        .build()
        );
        return MentorFieldResponse.from(registration);
    }

    @Operation(summary = "분야 수정", description = "검토 전 상태(PENDING)인 분야 등록을 수정합니다.")
    @PatchMapping("/{registrationId}")
    public MentorFieldResponse updateFieldRegistration(
            @PathVariable Long registrationId,
            @RequestBody @Valid RegisterFieldRequest request) {
        MentorField registration = modifyFieldRegistrationUseCase.modifyFieldRegistration(
                ModifyMentorFieldUseCase.Command.builder()
                        .accountId(DUMMY_ACCOUNT_ID)
                        .registrationId(registrationId)
                        .fieldId(request.fieldId())
                        .build()
        );
        return MentorFieldResponse.from(registration);
    }

    @Operation(summary = "분야 철회", description = "검토 전 상태(PENDING)인 분야 등록을 철회합니다.")
    @DeleteMapping("/{registrationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdrawFieldRegistration(@PathVariable Long registrationId) {
        modifyFieldRegistrationUseCase.withdrawFieldRegistration(DUMMY_ACCOUNT_ID, registrationId);
    }

    @Operation(summary = "분야 노출 설정", description = "승인된 상태(APPROVED)인 분야 등록의 노출 여부를 설정합니다.")
    @PatchMapping("/{registrationId}/visibility")
    public void updateVisibility(
            @PathVariable Long registrationId,
            @RequestBody @Valid UpdateVisibilityRequest request) {
        modifyFieldRegistrationUseCase.updateVisibility(DUMMY_ACCOUNT_ID, registrationId, request.visible());
    }
}
