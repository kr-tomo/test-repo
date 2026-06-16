package com.organization.app.mentor.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CareerEntryTest {

    @Test
    @DisplayName("경력 생성 시 초기 상태는 PENDING이어야 함")
    void createCareerEntry() {
        CareerEntry entry = CareerEntry.create(1L, "Content");
        assertEquals(RegistrationStatus.PENDING, entry.getStatus());
        assertNull(entry.getVisible());
    }

    @Test
    @DisplayName("PENDING 상태에서만 내용 수정 가능")
    void updateContent() {
        CareerEntry entry = CareerEntry.create(1L, "Content");
        entry.updateContent("Updated Content");
        assertEquals("Updated Content", entry.getContent());

        entry.approve();
        assertThrows(IllegalStateException.class, () -> entry.updateContent("New Content"));
    }

    @Test
    @DisplayName("PENDING 상태에서만 철회 가능")
    void withdraw() {
        CareerEntry entry = CareerEntry.create(1L, "Content");
        entry.withdraw();
        assertEquals(RegistrationStatus.WITHDRAWN, entry.getStatus());

        CareerEntry entry2 = CareerEntry.create(1L, "Content");
        entry2.approve();
        assertThrows(IllegalStateException.class, entry2::withdraw);
    }

    @Test
    @DisplayName("APPROVED 상태에서만 노출 설정 변경 가능")
    void updateVisibility() {
        CareerEntry entry = CareerEntry.create(1L, "Content");
        assertThrows(IllegalStateException.class, () -> entry.updateVisibility(false));

        entry.approve();
        entry.updateVisibility(false);
        assertFalse(entry.getVisible());
    }

    @Test
    @DisplayName("승인 시 상태는 APPROVED가 되고 visible은 true가 됨")
    void approve() {
        CareerEntry entry = CareerEntry.create(1L, "Content");
        entry.approve();
        assertEquals(RegistrationStatus.APPROVED, entry.getStatus());
        assertTrue(entry.getVisible());
    }
}
