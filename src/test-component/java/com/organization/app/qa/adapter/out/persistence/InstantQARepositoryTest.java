package com.organization.app.qa.adapter.out.persistence;

import com.organization.app.qa.domain.model.InstantQA;
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
@Import(InstantQAPersistenceAdapter.class)
class InstantQARepositoryTest {

    @Autowired
    private InstantQAPersistenceAdapter persistenceAdapter;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("[TC-QA-030] Repository — 분야 기반 Q&A 조회 쿼리 (복수 분야, 최신순)")
    void findByFieldIds() {
        // given
        persistenceAdapter.save(InstantQA.builder().menteeAccountId(1L).fieldId(10L).content("Q1").postedAt(LocalDateTime.now().minusHours(2)).build());
        persistenceAdapter.save(InstantQA.builder().menteeAccountId(1L).fieldId(10L).content("Q2").postedAt(LocalDateTime.now().minusHours(1)).build());
        persistenceAdapter.save(InstantQA.builder().menteeAccountId(1L).fieldId(11L).content("Q3").postedAt(LocalDateTime.now()).build());
        persistenceAdapter.save(InstantQA.builder().menteeAccountId(1L).fieldId(20L).content("Q4").postedAt(LocalDateTime.now()).build());

        entityManager.flush();
        entityManager.clear();

        // when
        List<InstantQA> results = persistenceAdapter.findByFieldIds(List.of(10L, 11L));

        // then
        assertThat(results).hasSize(3);
        assertThat(results.get(0).getContent()).isEqualTo("Q3");
        assertThat(results.get(1).getContent()).isEqualTo("Q2");
        assertThat(results.get(2).getContent()).isEqualTo("Q1");
    }
}
