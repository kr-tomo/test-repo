package com.organization.app.matching.adapter.out.persistence;

import com.organization.app.matching.domain.model.MatchRequest;
import com.organization.app.matching.domain.port.out.MatchRequestRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MatchRequestPersistenceAdapter implements MatchRequestRepositoryPort {

    private final JpaMatchRequestRepository matchRequestRepository;

    @Override
    public MatchRequest save(MatchRequest matchRequest) {
        MatchRequestEntity entity = toEntity(matchRequest);
        MatchRequestEntity saved = matchRequestRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<MatchRequest> findById(Long id) {
        return matchRequestRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<MatchRequest> findByMenteeAccountId(Long menteeAccountId) {
        return matchRequestRepository.findByMenteeAccountIdOrderByRequestedAtDesc(menteeAccountId).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private MatchRequest toDomain(MatchRequestEntity entity) {
        return MatchRequest.builder()
                .id(entity.getId())
                .menteeAccountId(entity.getMenteeAccountId())
                .mentorAccountId(entity.getMentorAccountId())
                .fieldId(entity.getFieldId())
                .status(entity.getStatus())
                .requestedAt(entity.getRequestedAt())
                .build();
    }

    private MatchRequestEntity toEntity(MatchRequest domain) {
        return MatchRequestEntity.builder()
                .id(domain.getId())
                .menteeAccountId(domain.getMenteeAccountId())
                .mentorAccountId(domain.getMentorAccountId())
                .fieldId(domain.getFieldId())
                .status(domain.getStatus())
                .requestedAt(domain.getRequestedAt())
                .build();
    }
}
