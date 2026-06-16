package com.organization.app.mentor.adapter.in.web;

import com.organization.app.mentor.domain.model.CareerEntry;
import com.organization.app.mentor.domain.model.FieldRegistration;
import com.organization.app.mentor.domain.port.in.ReviewCareerUseCase;
import com.organization.app.mentor.domain.port.in.ReviewFieldRegistrationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Mentor Profile Review", description = "운영자 멘토 프로필 검토 API")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminMentorProfileReviewController {

    private final ReviewCareerUseCase reviewCareerUseCase;
    private final ReviewFieldRegistrationUseCase reviewFieldRegistrationUseCase;

    @Operation(summary = "경력 등록 요청 검토", description = "경력 등록 요청을 승인 또는 반려합니다.")
    @PostMapping("/careers/{careerId}/review")
    public CareerResponse reviewCareer(
            @PathVariable Long careerId,
            @RequestBody @Valid ReviewRequest request) {
        CareerEntry entry = reviewCareerUseCase.reviewCareer(
                ReviewCareerUseCase.Command.builder()
                        .careerId(careerId)
                        .action(ReviewCareerUseCase.Action.valueOf(request.action().name()))
                        .build()
        );
        return CareerResponse.from(entry);
    }

    @Operation(summary = "분야 등록 요청 검토", description = "분야 등록 요청을 승인 또는 반려합니다.")
    @PostMapping("/field-registrations/{registrationId}/review")
    public FieldRegistrationResponse reviewFieldRegistration(
            @PathVariable Long registrationId,
            @RequestBody @Valid ReviewRequest request) {
        FieldRegistration registration = reviewFieldRegistrationUseCase.reviewFieldRegistration(
                ReviewFieldRegistrationUseCase.Command.builder()
                        .registrationId(registrationId)
                        .action(ReviewFieldRegistrationUseCase.Action.valueOf(request.action().name()))
                        .build()
        );
        return FieldRegistrationResponse.from(registration);
    }
}
