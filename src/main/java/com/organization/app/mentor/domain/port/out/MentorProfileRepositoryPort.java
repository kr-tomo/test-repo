package com.organization.app.mentor.domain.port.out;

import com.organization.app.mentor.domain.model.CareerEntry;
import com.organization.app.mentor.domain.model.MentorField;
import com.organization.app.mentor.domain.model.MentorProfile;
import com.organization.app.mentor.domain.model.RegistrationStatus;
import java.util.List;
import java.util.Optional;

public interface MentorProfileRepositoryPort {
    Optional<MentorProfile> findByAccountId(Long accountId);
    MentorProfile save(MentorProfile mentorProfile);

    Optional<CareerEntry> findCareerById(Long careerId);
    CareerEntry saveCareer(CareerEntry careerEntry);

    Optional<MentorField> findFieldRegistrationById(Long registrationId);
    MentorField saveFieldRegistration(MentorField fieldRegistration);

    boolean existsFieldRegistrationByMentorProfileIdAndFieldIdAndStatusIn(
            Long mentorProfileId, Long fieldId, List<RegistrationStatus> statuses);
}
