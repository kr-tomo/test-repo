package com.organization.app.field.adapter.out.persistence;

import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@Tag("component")
@DataJpaTest
@Import(FieldPersistenceAdapter.class)
class FieldRepositoryTest {

    @Autowired
    private FieldPersistenceAdapter fieldPersistenceAdapter;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("[TC-F-027] Repository — 하위 분야 일괄 상태 변경을 위한 하위 전체 조회")
    void findAllDescendants() {
        // given
        Field root = fieldPersistenceAdapter.save(Field.create("개발", null));
        Field child1 = fieldPersistenceAdapter.save(Field.create("백엔드", root.getId()));
        Field child2 = fieldPersistenceAdapter.save(Field.create("프론트엔드", root.getId()));
        Field grandChild = fieldPersistenceAdapter.save(Field.create("Java", child1.getId()));

        entityManager.flush();
        entityManager.clear();

        // when
        List<Field> descendants = fieldPersistenceAdapter.findAllDescendants(root.getId());

        // then
        assertEquals(3, descendants.size());
        assertTrue(descendants.stream().anyMatch(e -> e.getId().equals(child1.getId())));
        assertTrue(descendants.stream().anyMatch(e -> e.getId().equals(child2.getId())));
        assertTrue(descendants.stream().anyMatch(e -> e.getId().equals(grandChild.getId())));
    }

    @Test
    @DisplayName("[BR-FIELD-001] 분야 이름의 전역 고유성 검증")
    void existsByName() {
        // given
        fieldPersistenceAdapter.save(Field.create("개발", null));
        
        // when & then
        assertTrue(fieldPersistenceAdapter.existsByName("개발"));
        assertFalse(fieldPersistenceAdapter.existsByName("기획"));
    }
}
