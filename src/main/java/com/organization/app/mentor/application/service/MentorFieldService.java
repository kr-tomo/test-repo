package com.organization.app.mentor.application.service;

import com.organization.app.field.domain.exception.FieldException;
import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import com.organization.app.mentor.domain.exception.MentorProfileException;
import com.organization.app.mentor.domain.model.MentorField;
import com.organization.app.mentor.domain.model.MentorProfile;
import com.organization.app.mentor.domain.model.RegistrationStatus;
import com.organization.app.mentor.domain.port.in.ModifyMentorFieldUseCase;
import com.organization.app.mentor.domain.port.in.RegisterFieldUseCase;
import com.organization.app.mentor.domain.port.in.ReviewMentorFieldUseCase;
import com.organization.app.mentor.domain.port.out.MentorProfileRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorFieldService implements
        RegisterFieldUseCase,
        ModifyMentorFieldUseCase,
        ReviewMentorFieldUseCase {

    private final MentorProfileRepositoryPort mentorProfileRepositoryPort;
    private final FieldRepositoryPort fieldRepositoryPort;

    @Override
    public MentorField registerField(RegisterFieldUseCase.Command command) {
        MentorProfile profile = mentorProfileRepositoryPort.findByAccountId(command.accountId())
                .orElseThrow(MentorProfileException::mentorProfileNotFound);

        validateFieldActive(command.fieldId());
        validateDuplicateRegistration(profile.getId(), command.fieldId());

        MentorField registration = MentorField.create(profile.getId(), command.fieldId());
        return mentorProfileRepositoryPort.saveFieldRegistration(registration);
    }

    @Override
    public MentorField modifyFieldRegistration(ModifyMentorFieldUseCase.Command command) {
        MentorField registration = mentorProfileRepositoryPort.findFieldRegistrationById(command.registrationId())
                .orElseThrow(MentorProfileException::mentorFieldNotFound);

        MentorProfile profile = mentorProfileRepositoryPort.findByAccountId(command.accountId())
                .orElseThrow(MentorProfileException::mentorProfileNotFound);

        if (!registration.getMentorProfileId().equals(profile.getId())) {
            throw MentorProfileException.forbidden();
        }

        validateFieldActive(command.fieldId());
        validateDuplicateRegistration(profile.getId(), command.fieldId());

        try {
            registration.updateField(command.fieldId());
        } catch (IllegalStateException e) {
            throw MentorProfileException.modificationNotAllowed();
        }

        return mentorProfileRepositoryPort.saveFieldRegistration(registration);
    }

    @Override
    public void withdrawFieldRegistration(Long accountId, Long registrationId) {
        MentorField registration = mentorProfileRepositoryPort.findFieldRegistrationById(registrationId)
                .orElseThrow(MentorProfileException::mentorFieldNotFound);

        MentorProfile profile = mentorProfileRepositoryPort.findByAccountId(accountId)
                .orElseThrow(MentorProfileException::mentorProfileNotFound);

        if (!registration.getMentorProfileId().equals(profile.getId())) {
            throw MentorProfileException.forbidden();
        }

        try {
            registration.withdraw();
        } catch (IllegalStateException e) {
            throw MentorProfileException.withdrawalNotAllowed();
        }

        mentorProfileRepositoryPort.saveFieldRegistration(registration);
    }

    @Override
    public void updateVisibility(Long accountId, Long registrationId, boolean visible) {
        MentorField registration = mentorProfileRepositoryPort.findFieldRegistrationById(registrationId)
                .orElseThrow(MentorProfileException::mentorFieldNotFound);

        MentorProfile profile = mentorProfileRepositoryPort.findByAccountId(accountId)
                .orElseThrow(MentorProfileException::mentorProfileNotFound);

        if (!registration.getMentorProfileId().equals(profile.getId())) {
            throw MentorProfileException.forbidden();
        }

        try {
            registration.updateVisibility(visible);
        } catch (IllegalStateException e) {
            throw MentorProfileException.visibilityChangeNotAllowed();
        }

        mentorProfileRepositoryPort.saveFieldRegistration(registration);
    }

    @Override
    public MentorField reviewFieldRegistration(ReviewMentorFieldUseCase.Command command) {
        MentorField registration = mentorProfileRepositoryPort.findFieldRegistrationById(command.registrationId())
                .orElseThrow(MentorProfileException::mentorFieldNotFound);

        try {
            if (command.action() == ReviewMentorFieldUseCase.Action.APPROVE) {
                registration.approve();
            } else {
                registration.reject();
            }
        } catch (IllegalStateException e) {
            throw MentorProfileException.reviewTargetNotPending();
        }

        return mentorProfileRepositoryPort.saveFieldRegistration(registration);
    }

    private void validateFieldActive(Long fieldId) {
        Field field = fieldRepositoryPort.findById(fieldId)
                .orElseThrow(FieldException::fieldNotFound);
        if (field.getStatus() != FieldStatus.ACTIVE) {
            throw new MentorProfileException("FIELD_NOT_FOUND_OR_INACTIVE", "존재하지 않거나 비활성화된 분야입니다.", 422);
        }
    }

    private void validateDuplicateRegistration(Long mentorProfileId, Long fieldId) {
        if (mentorProfileRepositoryPort.existsFieldRegistrationByMentorProfileIdAndFieldIdAndStatusIn(
                mentorProfileId, fieldId, List.of(RegistrationStatus.PENDING, RegistrationStatus.APPROVED))) {
            throw MentorProfileException.mentorFieldDuplicate();
        }
    }
}
