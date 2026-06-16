package com.organization.app.mentor.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaMentorProfileRepository extends JpaRepository<MentorProfileEntity, Long> {
    Optional<MentorProfileEntity> findByAccountId(Long accountId);
}
