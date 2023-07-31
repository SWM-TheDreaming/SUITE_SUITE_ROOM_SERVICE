package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class SuiteRoomControllerTest {

    @Autowired ObjectMapper mapper;

    @Autowired private MockMvc mockMvc;
    public static final String JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb3BhaG4yQGdtYWlsLmNvbSIsIklEIjoiMSIsIk5BTUUiOiLrsJjsmIHtmZgiLCJOSUNLTkFNRSI6Imh3YW55OTkiLCJBQ0NPVU5UU1RBVFVTIjoiQUNUSVZBVEUiLCJST0xFIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjkwNzgwOTIyLCJleHAiOjE2OTEzODU3MjJ9.fejoJ5Hr0P6O-Kf0TQH_eAqnbd-95E6SxBBlQyH65jo";

    @Test
    @DisplayName("스위트룸 생성")
    public void createSuiteRoom() throws Exception {
        //given
        ReqSuiteRoomDto reqSuiteRoomDto = MockSuiteRoom.getMockSuiteRoom("title2", true);
        String body = mapper.writeValueAsString(reqSuiteRoomDto);
        //when
        String responseBody = postRequest("/suite/suiteroom/registration", JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );

        System.out.println(responseBody);

    }

    private String postRequest(String url, String jwt, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                        .content(body) //HTTP body에 담는다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getContentAsString();
    }

}