package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqUpdateSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockAuthorizer;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class SuiteRoomControllerTest {

    @Autowired private ObjectMapper mapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private ParticipantRepository participantRepository;
    @Autowired private SuiteRoomRepository suiteRoomRepository;

    @Value("${token.YH}")
    private String YH_JWT;
    @Value("${token.DH}")
    private String DH_JWT;

    private final SuiteRoom suiteRoom = MockSuiteRoom.getMockSuiteRoom("test", true).toSuiteRoomEntity();
    private final Participant participantHost = MockParticipant.getMockParticipant(true, MockAuthorizer.YH());

    @BeforeEach
    public void setUp() {
        suiteRoom.addParticipant(participantHost);
        suiteRoomRepository.save(suiteRoom);
        participantRepository.save(participantHost);
    }

    @Test
    @DisplayName("스위트룸 생성")
    public void createSuiteRoom() throws Exception {
        //given
        ReqSuiteRoomDto reqSuiteRoomDto = MockSuiteRoom.getMockSuiteRoom("title darren", true);
        String body = mapper.writeValueAsString(reqSuiteRoomDto);
        //when
        String responseBody = postRequest("/suite/suiteroom/registration", DH_JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );

        System.out.println(responseBody);

    }

    @Test
    @DisplayName("스위트룸 수정")
    public void renewalSuiteRoom() throws Exception {
        //given
        final String url = "/suite/suiteroom/update";
        Long suiteRoomId = suiteRoom.getSuiteRoomId();

        String body = mapper.writeValueAsString(ReqUpdateSuiteRoomDto.builder()
                .suiteRoomId(suiteRoomId)
                .content("updated content")
                .channelLink("http://www.naver.com").build());
        //when
        String responseBody = patchRequest(url, YH_JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인")
    public void listUpSuiteRooms() throws Exception {
        //given
        final String url = "/suite/suiteroom/";

        //when
        String responseBody = getRequest(url, YH_JWT);
        Message message = mapper.readValue(responseBody, new TypeReference<Message<List<ResSuiteRoomDto>>>() {
        });
        List<ResSuiteRoomDto> result = (List<ResSuiteRoomDto>) message.getData();

        //then
        Assertions.assertAll(
                () -> assertThat(result.get(0).getTitle()).isEqualTo(suiteRoom.getTitle()),
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );


    }

    @Test
    @DisplayName("스위트룸 모집글 확인")
    public void listUpSuiteRoom() throws Exception {
        //given
        final String url = "/suite/suiteroom/detail/" + suiteRoom.getSuiteRoomId();

        //when
        String responseBody = getRequest(url, YH_JWT);
        Message message = mapper.readValue(responseBody, new TypeReference<Message<ResSuiteRoomDto>>() {
        });
        ResSuiteRoomDto result = (ResSuiteRoomDto) message.getData();

        //then
        Assertions.assertAll(
                () -> assertThat(result.getContent()).isEqualTo(suiteRoom.getContent()),
                () -> assertThat(result.getTitle()).isEqualTo(suiteRoom.getTitle()),
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );


    }

    @Test
    @DisplayName("스터디 파투")
    public void deleteSuiteRoom() throws Exception {
        //given
        final String url = "/suite/suiteroom/delete/" + suiteRoom.getSuiteRoomId();
        //when
        String responseBody = deleteRequest(url, YH_JWT);
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