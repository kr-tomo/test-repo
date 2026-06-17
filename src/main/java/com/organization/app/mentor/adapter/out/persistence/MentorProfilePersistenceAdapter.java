package com.organization.app.mentor.adapter.out.persistence;

import com.organization.app.mentor.domain.model.CareerEntry;
import com.organization.app.mentor.domain.model.MentorField;
import com.organization.app.mentor.domain.model.MentorProfile;
import com.organization.app.mentor.domain.model.RegistrationStatus;
import com.organization.app.mentor.domain.port.out.MentorProfileRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MentorProfilePersistenceAdapter implements MentorProfileRepositoryPort {

    private final JpaMentorProfileRepository mentorProfileRepository;
    private final JpaCareerEntryRepository careerEntryRepository;
    private final JpaMentorFieldRepository fieldRegistrationRepository;
    private final MentorFieldMapper fieldRegistrationMapper;

    @Override
    public Optional<MentorProfile> findByAccountId(Long accountId) {
        return mentorProfileRepository.findByAccountId(accountId)
                .map(this::toDomain);
    }

    @Override
    @Transactional
    public MentorProfile save(MentorProfile mentorProfile) {
        MentorProfileEntity entity = toEntity(mentorProfile);
        MentorProfileEntity saved = mentorProfileRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<CareerEntry> findCareerById(Long careerId) {
        return careerEntryRepository.findById(careerId)
                .map(this::toCareerDomain);
    }

    @Override
    @Transactional
    public CareerEntry saveCareer(CareerEntry careerEntry) {
        CareerEntryEntity entity = toCareerEntity(careerEntry);
        CareerEntryEntity saved = careerEntryRepository.save(entity);
        return toCareerDomain(saved);
    }

    @Override
    public Optional<MentorField> findFieldRegistrationById(Long registrationId) {
        return fieldRegistrationRepository.findById(registrationId)
                .map(fieldRegistrationMapper::toDomain);
    }

    @Override
    @Transactional
    public MentorField saveFieldRegistration(MentorField fieldRegistration) {
        MentorFieldEntity entity = fieldRegistrationMapper.toEntity(fieldRegistration);
        MentorFieldEntity saved = fieldRegistrationRepository.save(entity);
        return fieldRegistrationMapper.toDomain(saved);
    }

    @Override
    public boolean existsFieldRegistrationByMentorProfileIdAndFieldIdAndStatusIn(
            Long mentorProfileId, Long fieldId, List<RegistrationStatus> statuses) {
        return fieldRegistrationRepository.existsByMentorProfileIdAndFieldIdAndStatusIn(mentorProfileId, fieldId, statuses);
    }

    private MentorProfile toDomain(MentorProfileEntity entity) {
        return MentorProfile.builder()
                .id(entity.getId())
                .accountId(entity.getAccountId())
                .build();
    }

    private MentorProfileEntity toEntity(MentorProfile mentorProfile) {
        return MentorProfileEntity.builder()
                .id(mentorProfile.getId())
                .accountId(mentorProfile.getAccountId())
                .build();
    }

    private CareerEntry toCareerDomain(CareerEntryEntity entity) {
        return CareerEntry.builder()
                .id(entity.getId())
                .mentorProfileId(entity.getMentorProfileId())
                .content(entity.getContent())
                .status(entity.getStatus())
                .visible(entity.getVisible())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private CareerEntryEntity toCareerEntity(CareerEntry domain) {
        return CareerEntryEntity.builder()
                .id(domain.getId())
                .mentorProfileId(domain.getMentorProfileId())
                .content(domain.getContent())
                .status(domain.getStatus())
                .visible(domain.getVisible())
                .build();
    }
}
