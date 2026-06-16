package com.organization.app.mentor.adapter.out.persistence;

import com.organization.app.mentor.domain.model.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;

public interface JpaFieldRegistrationRepository extends JpaRepository<FieldRegistrationEntity, Long> {
    boolean existsByMentorProfileIdAndFieldIdAndStatusIn(Long mentorProfileId, Long fieldId, Collection<RegistrationStatus> statuses);
}
