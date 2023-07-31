package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockAuthorizer;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
class SuiteRoomControllerTest {

    @Autowired ObjectMapper mapper;

    @Autowired private MockMvc mockMvc;
    @MockBean
    SuiteRoomService suiteRoomService;
    public static final String JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJsb3BhaG4yQGdtYWlsLmNvbSIsIklEIjoiMSIsIk5BTUUiOiLrsJjsmIHtmZgiLCJOSUNLTkFNRSI6Imh3YW55OTkiLCJBQ0NPVU5UU1RBVFVTIjoiQUNUSVZBVEUiLCJST0xFIjoiUk9MRV9VU0VSIiwiaWF0IjoxNjkwNzgwOTIyLCJleHAiOjE2OTEzODU3MjJ9.fejoJ5Hr0P6O-Kf0TQH_eAqnbd-95E6SxBBlQyH65jo";

    @Test
    @DisplayName("스위트룸 생성")
    public void createSuiteRoom() throws Exception {
        //given
        ReqSuiteRoomDto reqSuiteRoomDto = MockSuiteRoom.getMockSuiteRoom("title2", true);
        String body = mapper.writeValueAsString(reqSuiteRoomDto);
        //when
        String responseBody = postRequest("/suite/suiteroom/registration", this.JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );

        System.out.println(responseBody);

    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인")
    public void listUpSuiteRooms() throws Exception {
        //given
        final String url = "/suite/suiteroom/";

        ReqSuiteRoomDto reqSuiteRoomDto1 = MockSuiteRoom.getMockSuiteRoom("hwany", true);
        ReqSuiteRoomDto reqSuiteRoomDto2 = MockSuiteRoom.getMockSuiteRoom("mini", true);
        suiteRoomService.createSuiteRoom(reqSuiteRoomDto1, MockAuthorizer.getMockAuthorizer("hwany"));
        suiteRoomService.createSuiteRoom(reqSuiteRoomDto2, MockAuthorizer.getMockAuthorizer("hwany"));

        List<ResSuiteRoomDto> getAllSuiteRooms = suiteRoomService.getAllSuiteRooms();
        //when
        String responseBody = getRequest(url, this.JWT);
        Message message = mapper.readValue(responseBody, Message.class);

        //then
        Assertions.assertAll(
                () -> assertThat(message.getData()).isEqualTo(getAllSuiteRooms)
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

    private String getRequest(String url, String jwt) throws Exception {
        MvcResult result = mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + jwt)
                )
                .andExpect(status().isOk()).andReturn();

        return result.getResponse().getContentAsString();
    }

}