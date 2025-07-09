package com.grepp.spring.app.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = StudyController.class)
class StudyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("스터디 검색 - category = LANGUAGE, region = ONLINE")
    void searchStudies_withLanguageAndOnline_shouldReturnFiltered() throws Exception {
        // given
        Map<String, Object> request = Map.of(
            "category", "LANGUAGE",
            "region", "ONLINE"
        );

        // when & then
        mockMvc.perform(post("/api/v1/studies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(3)) // std1, std5, std6
            .andExpect(jsonPath("$.data[0].studyCategory").value("LANGUAGE"))
            .andExpect(jsonPath("$.data[0].region").value("ONLINE"));
    }

    @Test
    @DisplayName("스터디 검색 - title 포함 키워드 'node'")
    void searchStudies_withTitleNode_shouldReturnOnlyNodeStudy() throws Exception {
        Map<String, Object> request = Map.of(
            "title", "node"
        );

        mockMvc.perform(post("/api/v1/studies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data[0].name").value("node.js 마스터 스터디"));
    }

    @Test
    @DisplayName("스터디 검색 - 조건 없이 전체 반환")
    void searchStudies_withoutCondition_shouldReturnAll() throws Exception {
        Map<String, Object> request = Map.of(); // no filters

        mockMvc.perform(post("/api/v1/studies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("SUCCESS"))
            .andExpect(jsonPath("$.data.length()").value(6)); // std1 ~ std6
    }
}