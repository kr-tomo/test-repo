package com.organization.app.matching.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.organization.app.matching.domain.model.MatchRequest;
import com.organization.app.matching.domain.model.MatchRequestStatus;
import com.organization.app.matching.domain.port.in.CreateMatchRequestUseCase;
import com.organization.app.matching.domain.port.in.GetMyMatchRequestsQuery;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("component")
@SpringBootTest
@AutoConfigureMockMvc
class MatchRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateMatchRequestUseCase createMatchRequestUseCase;

    @MockBean
    private GetMyMatchRequestsQuery getMyMatchRequestsQuery;

    @Test
    @DisplayName("[TC-MR-001] 매칭 요청 생성 성공")
    @WithMockUser(authorities = "ROLE_MENTEE")
    void createSuccess() throws Exception {
        // given
        MatchRequestController.CreateMatchRequest request = new MatchRequestController.CreateMatchRequest(10L, null);
        MatchRequest mr = MatchRequest.builder()
                .id(200L)
                .menteeAccountId(1L)
                .fieldId(10L)
                .status(MatchRequestStatus.REQUESTED)
                .requestedAt(LocalDateTime.now())
                .build();
        given(createMatchRequestUseCase.create(any(), eq(10L), any())).willReturn(mr);

        // when & then
        mockMvc.perform(post("/match-requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(200))
                .andExpect(jsonPath("$.fieldId").value(10))
                .andExpect(jsonPath("$.status").value("REQUESTED"));
    }

    @Test
    @DisplayName("[TC-MR-008] 매칭 요청 — 멘토 계정으로 시도 시 Forbidden")
    @WithMockUser(authorities = "ROLE_MENTOR")
    void createMentorForbidden() throws Exception {
        // given
        MatchRequestController.CreateMatchRequest request = new MatchRequestController.CreateMatchRequest(10L, null);

        // when & then
        mockMvc.perform(post("/match-requests")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
