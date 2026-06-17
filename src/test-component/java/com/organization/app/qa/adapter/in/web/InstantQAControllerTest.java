package com.organization.app.qa.adapter.in.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.organization.app.qa.domain.model.InstantQA;
import com.organization.app.qa.domain.port.in.GetInstantQAsUseCase;
import com.organization.app.qa.domain.port.in.GetMyInstantQAsUseCase;
import com.organization.app.qa.domain.port.in.PostInstantQAUseCase;
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
class InstantQAControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostInstantQAUseCase postInstantQAUseCase;

    @MockBean
    private GetInstantQAsUseCase getInstantQAsUseCase;

    @MockBean
    private GetMyInstantQAsUseCase getMyInstantQAsUseCase;

    @Test
    @DisplayName("[TC-QA-001] Q&A 등록 성공")
    @WithMockUser(authorities = "ROLE_MENTEE")
    void postSuccess() throws Exception {
        // given
        InstantQAController.PostInstantQARequest request = new InstantQAController.PostInstantQARequest(10L, "질문 내용");
        InstantQA qa = InstantQA.builder()
                .id(100L)
                .fieldId(10L)
                .content("질문 내용")
                .postedAt(LocalDateTime.now())
                .build();
        given(postInstantQAUseCase.post(any(), eq(10L), eq("질문 내용"))).willReturn(qa);

        // when & then
        mockMvc.perform(post("/instant-qas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.fieldId").value(10))
                .andExpect(jsonPath("$.content").value("질문 내용"));
    }

    @Test
    @DisplayName("[TC-QA-009] Q&A 등록 — 멘토 계정으로 시도 시 Forbidden")
    @WithMockUser(authorities = "ROLE_MENTOR")
    void postMentorForbidden() throws Exception {
        // given
        InstantQAController.PostInstantQARequest request = new InstantQAController.PostInstantQARequest(10L, "질문 내용");

        // when & then
        mockMvc.perform(post("/instant-qas")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
