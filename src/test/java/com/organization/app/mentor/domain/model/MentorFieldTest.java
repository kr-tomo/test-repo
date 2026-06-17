package com.organization.app.mentor.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MentorFieldTest {

    @Test
    @DisplayName("분야 등록 생성 시 초기 상태는 PENDING이어야 함")
    void createFieldRegistration() {
        MentorField registration = MentorField.create(1L, 10L);
        assertEquals(RegistrationStatus.PENDING, registration.getStatus());
        assertNull(registration.getVisible());
    }

    @Test
    @DisplayName("PENDING 상태에서만 분야 수정 가능")
    void updateField() {
        MentorField registration = MentorField.create(1L, 10L);
        registration.updateField(11L);
        assertEquals(11L, registration.getFieldId());

        registration.approve();
        assertThrows(IllegalStateException.class, () -> registration.updateField(12L));
    }

    @Test
    @DisplayName("PENDING 상태에서만 철회 가능")
    void withdraw() {
        MentorField registration = MentorField.create(1L, 10L);
        registration.withdraw();
        assertEquals(RegistrationStatus.WITHDRAWN, registration.getStatus());

        MentorField registration2 = MentorField.create(1L, 10L);
        registration2.approve();
        assertThrows(IllegalStateException.class, registration2::withdraw);
    }

    @Test
    @DisplayName("APPROVED 상태에서만 노출 설정 변경 가능")
    void updateVisibility() {
        MentorField registration = MentorField.create(1L, 10L);
        assertThrows(IllegalStateException.class, () -> registration.updateVisibility(false));

        registration.approve();
        registration.updateVisibility(false);
        assertFalse(registration.getVisible());
    }

    @Test
    @DisplayName("승인 시 상태는 APPROVED가 되고 visible은 true가 됨")
    void approve() {
        MentorField registration = MentorField.create(1L, 10L);
        registration.approve();
        assertEquals(RegistrationStatus.APPROVED, registration.getStatus());
        assertTrue(registration.getVisible());
    }
}
