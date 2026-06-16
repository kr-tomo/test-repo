package com.organization.app.field.application.service;

import com.organization.app.field.domain.exception.FieldException;
import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.in.CreateFieldUseCase;
import com.organization.app.field.domain.port.in.UpdateFieldUseCase;
import com.organization.app.field.domain.port.out.FieldRepositoryPort;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class FieldServiceTest {

    @Mock
    private FieldRepositoryPort fieldRepositoryPort;

    @InjectMocks
    private FieldService fieldService;

    @Test
    @DisplayName("[TC-F-007] 분야 생성 — name 중복 (전역)")
    void createFieldDuplicateName() {
        // given
        CreateFieldUseCase.Command command = new CreateFieldUseCase.Command("개발", null);
        given(fieldRepositoryPort.existsByName("개발")).willReturn(true);

        // when & then
        assertThrows(FieldException.class, () -> fieldService.createField(command));
    }

    @Test
    @DisplayName("[TC-F-009] 분야 생성 — 존재하지 않는 parentId")
    void createFieldParentNotFound() {
        // given
        CreateFieldUseCase.Command command = new CreateFieldUseCase.Command("백엔드", 9999L);
        given(fieldRepositoryPort.findById(9999L)).willReturn(Optional.empty());

        // when & then
        assertThrows(FieldException.class, () -> fieldService.createField(command));
    }

    @Test
    @DisplayName("[TC-F-010] 분야 생성 — INACTIVE 상위 분야 지정")
    void createFieldParentInactive() {
        // given
        Field parent = Field.builder().id(1L).name("개발").status(FieldStatus.INACTIVE).build();
        CreateFieldUseCase.Command command = new CreateFieldUseCase.Command("백엔드", 1L);
        given(fieldRepositoryPort.findById(1L)).willReturn(Optional.of(parent));

        // when & then
        FieldException exception = assertThrows(FieldException.class, () -> fieldService.createField(command));
        assertEquals("PARENT_FIELD_INACTIVE", exception.getCode());
    }

    @Test
    @DisplayName("[TC-F-019] 분야 비활성화 — 하위 분야 연쇄 INACTIVE")
    void deactivateFieldRecursive() {
        // given
        Field parent = Field.builder().id(1L).name("개발").status(FieldStatus.ACTIVE).build();
        Field child1 = Field.builder().id(10L).name("백엔드").parentId(1L).status(FieldStatus.ACTIVE).build();
        Field child2 = Field.builder().id(11L).name("프론트엔드").parentId(1L).status(FieldStatus.ACTIVE).build();

        given(fieldRepositoryPort.findById(1L)).willReturn(Optional.of(parent));
        given(fieldRepositoryPort.findAllDescendants(1L)).willReturn(List.of(child1, child2));

        // when
        fieldService.deactivateField(1L);

        // then
        assertEquals(FieldStatus.INACTIVE, parent.getStatus());
        assertEquals(FieldStatus.INACTIVE, child1.getStatus());
        assertEquals(FieldStatus.INACTIVE, child2.getStatus());
        verify(fieldRepositoryPort, times(1)).save(any());
        verify(fieldRepositoryPort, times(1)).saveAll(any());
    }

    @Test
    @DisplayName("[TC-F-021] 분야 비활성화 — 이미 INACTIVE")
    void deactivateFieldAlreadyInactive() {
        // given
        Field field = Field.builder().id(10L).name("백엔드").status(FieldStatus.INACTIVE).build();
        given(fieldRepositoryPort.findById(10L)).willReturn(Optional.of(field));

        // when & then
        FieldException exception = assertThrows(FieldException.class, () -> fieldService.deactivateField(10L));
        assertEquals("FIELD_ALREADY_INACTIVE", exception.getCode());
    }
}
