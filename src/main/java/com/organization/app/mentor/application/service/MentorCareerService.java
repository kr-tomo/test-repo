package com.organization.app.mentor.application.service;

import com.organization.app.mentor.domain.exception.MentorProfileException;
import com.organization.app.mentor.domain.model.CareerEntry;
import com.organization.app.mentor.domain.model.MentorProfile;
import com.organization.app.mentor.domain.port.in.ModifyCareerUseCase;
import com.organization.app.mentor.domain.port.in.RegisterCareerUseCase;
import com.organization.app.mentor.domain.port.in.ReviewCareerUseCase;
import com.organization.app.mentor.domain.port.out.MentorProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorCareerService implements
        RegisterCareerUseCase,
        ModifyCareerUseCase,
        ReviewCareerUseCase {

    private final MentorProfileRepositoryPort repositoryPort;

    @Override
    public CareerEntry registerCareer(RegisterCareerUseCase.Command command) {
        MentorProfile profile = repositoryPort.findByAccountId(command.accountId())
                .orElseThrow(MentorProfileException::mentorProfileNotFound);
        
        CareerEntry entry = CareerEntry.create(profile.getId(), command.content());
        return repositoryPort.saveCareer(entry);
    }

    @Override
    public CareerEntry modifyCareer(ModifyCareerUseCase.Command command) {
        CareerEntry entry = repositoryPort.findCareerById(command.careerId())
                .orElseThrow(MentorProfileException::careerNotFound);
        
        MentorProfile profile = repositoryPort.findByAccountId(command.accountId())
                .orElseThrow(MentorProfileException::mentorProfileNotFound);

        if (!entry.getMentorProfileId().equals(profile.getId())) {
            throw MentorProfileException.forbidden();
        }

        try {
            entry.updateContent(command.content());
        } catch (IllegalStateException e) {
            throw MentorProfileException.modificationNotAllowed();
        }

        return repositoryPort.saveCareer(entry);
    }

    @Override
    public void withdrawCareer(Long accountId, Long careerId) {
        CareerEntry entry = repositoryPort.findCareerById(careerId)
                .orElseThrow(MentorProfileException::careerNotFound);
        
        MentorProfile profile = repositoryPort.findByAccountId(accountId)
                .orElseThrow(MentorProfileException::mentorProfileNotFound);

        if (!entry.getMentorProfileId().equals(profile.getId())) {
            throw MentorProfileException.forbidden();
        }

        try {
            entry.withdraw();
        } catch (IllegalStateException e) {
            throw MentorProfileException.withdrawalNotAllowed();
        }

        repositoryPort.saveCareer(entry);
    }

    @Override
    public void updateVisibility(Long accountId, Long careerId, boolean visible) {
        CareerEntry entry = repositoryPort.findCareerById(careerId)
                .orElseThrow(MentorProfileException::careerNotFound);
        
        MentorProfile profile = repositoryPort.findByAccountId(accountId)
                .orElseThrow(MentorProfileException::mentorProfileNotFound);

        if (!entry.getMentorProfileId().equals(profile.getId())) {
            throw MentorProfileException.forbidden();
        }

        try {
            entry.updateVisibility(visible);
        } catch (IllegalStateException e) {
            throw MentorProfileException.visibilityChangeNotAllowed();
        }

        repositoryPort.saveCareer(entry);
    }

    @Override
    public CareerEntry reviewCareer(ReviewCareerUseCase.Command command) {
        CareerEntry entry = repositoryPort.findCareerById(command.careerId())
                .orElseThrow(MentorProfileException::careerNotFound);

        try {
            if (command.action() == ReviewCareerUseCase.Action.APPROVE) {
                entry.approve();
            } else {
                entry.reject();
            }
        } catch (IllegalStateException e) {
            throw MentorProfileException.reviewTargetNotPending();
        }

        return repositoryPort.saveCareer(entry);
    }
}
