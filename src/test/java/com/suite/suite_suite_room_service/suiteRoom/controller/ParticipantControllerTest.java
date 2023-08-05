package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockAuthorizer;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockCheckInInfo;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import com.suite.suite_suite_room_service.suiteRoom.service.ParticipantService;
import com.suite.suite_suite_room_service.suiteRoom.service.ParticipantServiceImpl;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
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
    @Autowired private ParticipantService participantService;
    @Autowired private SuiteRoomService suiteRoomService;
    @Autowired private ParticipantRepository participantRepository;
    @Autowired private SuiteRoomRepository suiteRoomRepository;



    public static final String YH_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJod2FueTkxODFAZ21haWwuY29tIiwiSUQiOiIxIiwiTkFNRSI6IuuwmOyYge2ZmCIsIk5JQ0tOQU1FIjoiaHdhbnk5OSIsIkFDQ09VTlRTVEFUVVMiOiJBQ1RJVkFURSIsIlJPTEUiOiJST0xFX1VTRVIiLCJpYXQiOjE2OTExMzQzMDEsImV4cCI6MTY5MTczOTEwMX0.ol8qYp-j1hre4bAyOnTmlHaFVoHL-rGTgA5YfVWuRa0";
    public static final String DR_JWT = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ6eHo0NjQxQGdtYWlsLmNvbSIsIklEIjoiMiIsIk5BTUUiOiLquYDrjIDtmIQiLCJOSUNLTkFNRSI6IkRhcnJlbiIsIkFDQ09VTlRTVEFUVVMiOiJBQ1RJVkFURSIsIlJPTEUiOiJST0xFX1VTRVIiLCJpYXQiOjE2OTA4MTQxMzUsImV4cCI6MTY5MTQxODkzNX0.ob5s7qQxdALJHpxb28pyYFiAqdeifGKfxtGx9MD3KQ0";
    @BeforeEach
    public void setUp() {
        ReqSuiteRoomDto reqSuiteRoomDto = MockSuiteRoom.getMockSuiteRoom("title2", true);
        suiteRoomService.createSuiteRoom(reqSuiteRoomDto, MockAuthorizer.getMockAuthorizer("hwany", 1L));
    }

    @Test
    @DisplayName("스위트룸 참가하기")
    public void joinSuiteRoom() throws Exception {
        //given
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
        AuthorizerDto participant = MockAuthorizer.getMockAuthorizer("Darren", 2L);
        participantService.addParticipant(1L, participant);
        Map<String, Long> suiteRoomId = new HashMap<String, Long>();
        suiteRoomId.put("suiteRoomId", 1L);
        String body = mapper.writeValueAsString(suiteRoomId);
        //when
        String responseBody = postRequest("/suite/suiteroom/attend/cancel", DR_JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    @DisplayName("스위트룸 체크인 완료")
    public void checkInSuiteRoom() throws Exception {
        //given
        final String url = "/suite/payment/completion";

        Map<String, MockCheckInInfo> messageCard = new HashMap<String, MockCheckInInfo>();
        MockCheckInInfo checkInInfo = new MockCheckInInfo(1L, 1L);
        messageCard.put("checkInInfo", checkInInfo);
        String body = mapper.writeValueAsString(messageCard);
        //when
        String responseBody = postRequest(url, YH_JWT, body);
        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );
    }

    @Test
    @DisplayName("스위트룸 체크인 목록 확인 - 납부자")
    @Transactional
    public void getCheckInList() throws Exception {
        //given
        final String url = "/suite/payment/ready/1";
        Participant participantGuest = MockParticipant.getMockParticipant(false, MockAuthorizer.getMockAuthorizer("darren", 2L));
        Participant participantHost = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(1L, 1L).orElseThrow(
                () -> Assertions.assertThrows(CustomException.class, ()->{throw new CustomException(StatusCode.NOT_FOUND);})
        );
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(1L).orElseThrow(
                () -> Assertions.assertThrows(CustomException.class, ()->{throw new CustomException(StatusCode.NOT_FOUND);})
        );

        participantHost.updateStatus(SuiteStatus.READY);
        suiteRoom.openSuiteRoom();

        saveParticipantWithTransaction(participantGuest, suiteRoom);
        //when
        String responseBody = getRequest(url, YH_JWT);
        Message message = mapper.readValue(responseBody, new TypeReference<Message<List<ResPaymentParticipantDto>>>() {
        });
        List<ResPaymentParticipantDto> result = (List<ResPaymentParticipantDto>) message.getData();
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200),
                () -> assertThat(result.get(0).getStatus()).isEqualTo(SuiteStatus.READY)
        );
    }

    @Test
    @DisplayName("스위트룸 체크인 목록 확인 - 미납부 신청자")
    @Transactional
    public void getNotYetCheckInList() throws Exception {
        //given
        final String url = "/suite/payment/plain/1";
        Participant participantGuest = MockParticipant.getMockParticipant(false, MockAuthorizer.getMockAuthorizer("darren", 2L));
        Participant participantHost = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(1L, 1L).orElseThrow(
                () -> Assertions.assertThrows(CustomException.class, ()->{throw new CustomException(StatusCode.NOT_FOUND);})
        );
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(1L).orElseThrow(
                () -> Assertions.assertThrows(CustomException.class, ()->{throw new CustomException(StatusCode.NOT_FOUND);})
        );

        participantHost.updateStatus(SuiteStatus.READY);
        suiteRoom.openSuiteRoom();

        saveParticipantWithTransaction(participantGuest, suiteRoom);

        //when
        String responseBody = getRequest(url, YH_JWT);
        Message message = mapper.readValue(responseBody, new TypeReference<Message<List<ResPaymentParticipantDto>>>() {
        });
        List<ResPaymentParticipantDto> result = (List<ResPaymentParticipantDto>) message.getData();
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200),
                () -> assertThat(result.get(0).getStatus()).isEqualTo(SuiteStatus.PLAIN)
        );
    }
    @Rollback(true)
    protected void saveParticipantWithTransaction(Participant participantGuest, SuiteRoom suiteRoom) {
        suiteRoom.addParticipant(participantGuest);
        participantRepository.save(participantGuest);
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