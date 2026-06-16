package com.organization.app.field.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class FieldTest {

    @Test
    @DisplayName("[TC-F-001] 최상위 분야 생성 성공")
    void createRootFieldSuccess() {
        Field field = Field.create("개발", null);

        assertEquals("개발", field.getName());
        assertNull(field.getParentId());
        assertEquals(FieldStatus.ACTIVE, field.getStatus());
    }

    @Test
    @DisplayName("[TC-F-002] 하위 분야 생성 성공")
    void createChildFieldSuccess() {
        Field field = Field.create("백엔드", 1L);

        assertEquals("백엔드", field.getName());
        assertEquals(1L, field.getParentId());
        assertEquals(FieldStatus.ACTIVE, field.getStatus());
    }

    @Test
    @DisplayName("[TC-F-003, TC-F-004] 분야 생성 — name 누락 및 공백")
    void createFieldInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> Field.create(null, null));
        assertThrows(IllegalArgumentException.class, () -> Field.create("", null));
        assertThrows(IllegalArgumentException.class, () -> Field.create("   ", null));
    }

    @Test
    @DisplayName("[TC-F-005] 분야 생성 — name 100자 초과")
    void createFieldTooLongName() {
        String longName = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> Field.create(longName, null));
    }

    @Test
    @DisplayName("[TC-F-006] 분야 생성 — name 정확히 100자")
    void createFieldBoundaryName() {
        String boundaryName = "a".repeat(100);
        Field field = Field.create(boundaryName, null);
        assertEquals(100, field.getName().length());
    }

    @Test
    @DisplayName("[TC-F-013] 분야 수정 성공")
    void updateNameSuccess() {
        Field field = Field.create("개발", null);
        field.updateName("IT");
        assertEquals("IT", field.getName());
    }

    @Test
    @DisplayName("[TC-F-018] 분야 비활성화")
    void deactivateSuccess() {
        Field field = Field.create("개발", null);
        field.deactivate();
        assertEquals(FieldStatus.INACTIVE, field.getStatus());
    }
}
