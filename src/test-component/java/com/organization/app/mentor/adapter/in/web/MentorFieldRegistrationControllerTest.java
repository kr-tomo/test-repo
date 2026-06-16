package com.organization.app.mentor.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.organization.app.mentor.domain.model.FieldRegistration;
import com.organization.app.mentor.domain.model.RegistrationStatus;
import com.organization.app.mentor.domain.port.in.ModifyFieldRegistrationUseCase;
import com.organization.app.mentor.domain.port.in.RegisterFieldUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("component")
@SpringBootTest
@AutoConfigureMockMvc
class MentorFieldRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterFieldUseCase registerFieldUseCase;

    @MockBean
    private ModifyFieldRegistrationUseCase modifyFieldRegistrationUseCase;

    @Test
    @DisplayName("[TC-MP-023] 분야 등록 요청 성공 → PENDING 생성")
    @WithMockUser(authorities = "ROLE_MENTOR")
    void registerFieldSuccess() throws Exception {
        // given
        RegisterFieldRequest request = new RegisterFieldRequest(10L);
        FieldRegistration registration = FieldRegistration.builder()
                .id(1L)
                .fieldId(10L)
                .status(RegistrationStatus.PENDING)
                .build();
        given(registerFieldUseCase.registerField(any())).willReturn(registration);

        // when & then
        mockMvc.perform(post("/mentor-profiles/me/field-registrations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.fieldId").value(10))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}
