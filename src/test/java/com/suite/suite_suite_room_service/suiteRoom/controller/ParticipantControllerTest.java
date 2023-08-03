package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockAuthorizer;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.service.ParticipantServiceImpl;
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

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ParticipantControllerTest {

    @Autowired private ObjectMapper mapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private ParticipantServiceImpl participantService;
    @Autowired private SuiteRoomServiceImpl suiteRoomService;

    public static final String YH_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb3BhaG4yQGdtYWlsLmNvbSIsIklEIjoiMSIsIk5BTUUiOiLrsJjsmIHtmZgiLCJOSUNLTkFNRSI6Imh3YW55OTkiLCJBQ0NPVU5UU1RBVFVTIjoiQUNUSVZBVEUiLCJST0xFIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjkwODE0MDk5LCJleHAiOjE2OTE0MTg4OTl9.xlbiPnD366KP49g5v6X_F4yEZRvu6rbf3ph6j-AWxs8";
    public static final String DR_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6eHo0NjQxQGdtYWlsLmNvbSIsIklEIjoiMiIsIk5BTUUiOiLquYDrjIDtmIQiLCJOSUNLTkFNRSI6IkRhcnJlbiIsIkFDQ09VTlRTVEFUVVMiOiJBQ1RJVkFURSIsIlJPTEUiOiJST0xFX1VTRVIiLCJpYXQiOjE2OTA4MTQxMzUsImV4cCI6MTY5MTQxODkzNX0.ob5s7qQxdALJHpxb28pyYFiAqdeifGKfxtGx9MD3KQ0";

    @Test
    @DisplayName("스위트룸 참가하기")
    public void joinSuiteRoom() throws Exception {
        //given
        ReqSuiteRoomDto reqSuiteRoomDto = MockSuiteRoom.getMockSuiteRoom("title2", true);
        suiteRoomService.createSuiteRoom(reqSuiteRoomDto, MockAuthorizer.getMockAuthorizer("hwany"));
        Map<String, Long> suiteRoomId = new HashMap<String, Long>();
        suiteRoomId.put("suiteRoomId", Long.parseLong("1"));
        String body = mapper.writeValueAsString(suiteRoomId);
        //when
        String responseBody = postRequest("/suite/suiteroom/attend", DR_JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    @DisplayName("스위트룸 참가 취소")
    public void cancelSuiteRoom() throws Exception {
        //given
        ReqSuiteRoomDto reqSuiteRoomDto = MockSuiteRoom.getMockSuiteRoom("title2", true);
        suiteRoomService.createSuiteRoom(reqSuiteRoomDto, MockAuthorizer.getMockAuthorizer("hwany"));
        Map<String, Long> suiteRoomId = new HashMap<String, Long>();
        suiteRoomId.put("suiteRoomId", Long.parseLong("1"));
        String body = mapper.writeValueAsString(suiteRoomId);
        //when
        String responseBody = postRequest("/suite/suiteroom/attend/cancel", DR_JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );
    }



    private String postRequest(String url, String jwt, String body) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                        .content(body) //HTTP body에 담는다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getContentAsString();
    }

    private String patchRequest(String url, String jwt, String body) throws Exception {
        MvcResult result = mockMvc.perform(patch(url)
                        .content(body) //HTTP body에 담는다.
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getContentAsString();
    }

    private String getRequest(String url, String jwt) throws Exception {
        MvcResult result = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getContentAsString();
    }

    private String deleteRequest(String url, String jwt) throws Exception {
        MvcResult result = mockMvc.perform(delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getContentAsString();
    }

}