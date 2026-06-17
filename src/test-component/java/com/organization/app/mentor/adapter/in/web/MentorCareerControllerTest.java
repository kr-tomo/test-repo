package com.organization.app.mentor.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.organization.app.mentor.domain.model.CareerEntry;
import com.organization.app.mentor.domain.model.RegistrationStatus;
import com.organization.app.mentor.domain.port.in.*;
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
class MentorCareerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegisterCareerUseCase registerCareerUseCase;

    @MockBean
    private ModifyCareerUseCase modifyCareerUseCase;

    @MockBean
    private ReviewCareerUseCase reviewCareerUseCase;

    @MockBean
    private ReviewMentorFieldUseCase reviewMentorFieldUseCase;

    @MockBean
    private RegisterFieldUseCase registerFieldUseCase;

    @MockBean
    private ModifyMentorFieldUseCase modifyMentorFieldUseCase;

    @Test
    @DisplayName("[TC-MP-001] 경력 등록 요청 성공 → PENDING 생성")
    @WithMockUser(authorities = "ROLE_MENTOR")
    void registerCareerSuccess() throws Exception {
        // given
        RegisterCareerRequest request = new RegisterCareerRequest("카카오 백엔드 개발자 3년");
        CareerEntry entry = CareerEntry.builder()
                .id(1L)
                .content(request.content())
                .status(RegistrationStatus.PENDING)
                .build();
        given(registerCareerUseCase.registerCareer(any())).willReturn(entry);

        // when & then
        mockMvc.perform(post("/mentor-profiles/me/careers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("카카오 백엔드 개발자 3년"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @DisplayName("[TC-MP-002] 경력 등록 — content 누락")
    @WithMockUser(authorities = "ROLE_MENTOR")
    void registerCareerContentRequired() throws Exception {
        // given
        String request = "{}";

        // when & then
        mockMvc.perform(post("/mentor-profiles/me/careers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }
}
