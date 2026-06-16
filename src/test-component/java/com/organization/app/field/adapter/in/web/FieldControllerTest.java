package com.organization.app.field.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.organization.app.config.SecurityConfig;
import com.organization.app.field.domain.model.Field;
import com.organization.app.field.domain.model.FieldStatus;
import com.organization.app.field.domain.port.in.CreateFieldUseCase;
import com.organization.app.field.domain.port.in.DeactivateFieldUseCase;
import com.organization.app.field.domain.port.in.GetFieldsUseCase;
import com.organization.app.field.domain.port.in.UpdateFieldUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("component")
@SpringBootTest
@AutoConfigureMockMvc
class FieldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateFieldUseCase createFieldUseCase;

    @MockBean
    private UpdateFieldUseCase updateFieldUseCase;

    @MockBean
    private DeactivateFieldUseCase deactivateFieldUseCase;

    @MockBean
    private GetFieldsUseCase getFieldsUseCase;

    @Test
    @DisplayName("[TC-F-001] 최상위 분야 생성 성공 — 운영자 권한")
    @WithMockUser(authorities = "ROLE_ACCOUNT_MANAGEMENT")
    void createFieldSuccess() throws Exception {
        // given
        CreateFieldRequest request = new CreateFieldRequest("개발", null);
        Field field = Field.builder().id(1L).name("개발").status(FieldStatus.ACTIVE).build();
        given(createFieldUseCase.createField(any())).willReturn(field);

        // when & then
        mockMvc.perform(post("/fields")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("개발"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("[TC-F-011] 분야 생성 — 권한 없는 운영자")
    @WithMockUser(authorities = "ROLE_MATCHING_MANAGEMENT")
    void createFieldForbidden() throws Exception {
        // given
        CreateFieldRequest request = new CreateFieldRequest("개발", null);

        // when & then
        mockMvc.perform(post("/fields")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[TC-F-012] 분야 생성 — 멘티 계정으로 시도")
    @WithMockUser(authorities = "ROLE_MENTEE")
    void createFieldMenteeForbidden() throws Exception {
        // given
        CreateFieldRequest request = new CreateFieldRequest("개발", null);

        // when & then
        mockMvc.perform(post("/fields")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("[TC-F-003] 분야 생성 — name 누락")
    @WithMockUser(authorities = "ROLE_ACCOUNT_MANAGEMENT")
    void createFieldNameRequired() throws Exception {
        // given
        String request = "{ \"parentId\": null }";

        // when & then
        mockMvc.perform(post("/fields")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("FIELD_NAME_REQUIRED"));
    }

    @Test
    @DisplayName("[TC-F-024] 분야 목록 조회 — 성공")
    void getFieldsSuccess() throws Exception {
        // when & then
        mockMvc.perform(get("/fields"))
                .andExpect(status().isOk());
    }
}
