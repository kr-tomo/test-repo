package com.organization.app.matching.adapter.out.persistence;

import com.organization.app.matching.domain.model.MatchRequest;
import com.organization.app.matching.domain.model.MatchRequestStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("component")
@DataJpaTest
@Import(MatchRequestPersistenceAdapter.class)
class MatchRequestRepositoryTest {

    @Autowired
    private MatchRequestPersistenceAdapter persistenceAdapter;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("[TC-MR-026] 내 매칭 요청 목록 조회 — 최신순")
    void findByMenteeAccountId() {
        // given
        persistenceAdapter.save(MatchRequest.builder().menteeAccountId(1L).fieldId(10L).status(MatchRequestStatus.REQUESTED).requestedAt(LocalDateTime.now().minusHours(1)).build());
        persistenceAdapter.save(MatchRequest.builder().menteeAccountId(1L).fieldId(11L).status(MatchRequestStatus.REQUESTED).requestedAt(LocalDateTime.now()).build());
        persistenceAdapter.save(MatchRequest.builder().menteeAccountId(2L).fieldId(10L).status(MatchRequestStatus.REQUESTED).requestedAt(LocalDateTime.now()).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<MatchRequest> results = persistenceAdapter.findByMenteeAccountId(1L);

        // then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getFieldId()).isEqualTo(11L); // Latest
        assertThat(results.get(1).getFieldId()).isEqualTo(10L);
    }
}
