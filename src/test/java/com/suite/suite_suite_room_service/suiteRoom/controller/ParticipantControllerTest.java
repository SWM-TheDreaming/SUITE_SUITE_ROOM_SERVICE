package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;

import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockAuthorizer;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockCheckInInfo;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@Rollback
class ParticipantControllerTest {

    @Autowired private ObjectMapper mapper;
    @Autowired private MockMvc mockMvc;
    @Autowired private ParticipantRepository participantRepository;
    @Autowired private SuiteRoomRepository suiteRoomRepository;


    /**
     * @Rule
     * 방장은 YH로 생성
     * 게스트는 DR로 생성
     *
     * @Specific
     * 실제 토큰의 Claim 값과 동일하게 **_NAME, **_ID 를 선언해야 합니다.
     * 테스트시 에러가 발생합니다.
     * */

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
    @DisplayName("스위트룸 참가하기")
    public void joinSuiteRoom() throws Exception {
        //given
        Map<String, Long> suiteRoomId = new HashMap<String, Long>();
        suiteRoomId.put("suiteRoomId", suiteRoom.getSuiteRoomId());
        String body = mapper.writeValueAsString(suiteRoomId);
        //when
        String responseBody = postRequest("/suite/suiteroom/attend", DH_JWT, body);
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
        addGuest();

        Map<String, Long> suiteRoomId = new HashMap<String, Long>();
        suiteRoomId.put("suiteRoomId", suiteRoom.getSuiteRoomId());
        String body = mapper.writeValueAsString(suiteRoomId);

        //when
        String responseBody = postRequest("/suite/suiteroom/attend/cancel", DH_JWT, body);
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
        MockCheckInInfo checkInInfo = new MockCheckInInfo(suiteRoom.getSuiteRoomId(), participantHost.getMemberId());
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
    public void getCheckInList() throws Exception {
        //given
        final String url = "/suite/payment/ready/" + suiteRoom.getSuiteRoomId();
        addGuest();
        //when
        String responseBody = getRequest(url, DH_JWT);
        Message message = mapper.readValue(responseBody, new TypeReference<Message<List<ResPaymentParticipantDto>>>() {
        });
        List<ResPaymentParticipantDto> result = (List<ResPaymentParticipantDto>) message.getData();
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200),
                () -> assertThat(result).allMatch(dto -> dto.getStatus() == SuiteStatus.READY)
        );
    }
    @Test
    @DisplayName("스위트룸 체크인 목록 확인 - 미납부 신청자")
    public void getNotYetCheckInList() throws Exception {
        //given
        final String url = "/suite/payment/plain/" + String.valueOf(suiteRoom.getSuiteRoomId());
        addGuest();

        //when
        String responseBody = getRequest(url, DH_JWT);
        Message message = mapper.readValue(responseBody, new TypeReference<Message<List<ResPaymentParticipantDto>>>() {
        });
        List<ResPaymentParticipantDto> result = (List<ResPaymentParticipantDto>) message.getData();
        System.out.println(result.get(0).getStatus());
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200),
                () -> assertThat(result).allMatch(dto -> dto.getStatus() == SuiteStatus.PLAIN)
        );
    }

    @Test
    @DisplayName("스위트룸-스터디-시작")
    public void startSuiteRoom() throws Exception {
        //given
        final String url = "/suite/suiteroom/beginning";
        addGuest();
        //when
        Map<String, Long> suiteRoomId = new HashMap<String, Long>();
        suiteRoomId.put("suiteRoomId", suiteRoom.getSuiteRoomId());
        String body = mapper.writeValueAsString(suiteRoomId);

        String responseBody = postRequest(url, YH_JWT, body);

        Message message = mapper.readValue(responseBody, Message.class);
        //then
        Assertions.assertAll(
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );

    }

    protected void addGuest() {
        Participant participantGuest = MockParticipant.getMockParticipant(false, MockAuthorizer.DH());

        updateHostStatus();

        suiteRoom.addParticipant(participantGuest);
        participantRepository.save(participantGuest);
    }
    protected void updateHostStatus() {
        participantHost.updateStatus(SuiteStatus.READY);
        suiteRoom.openSuiteRoom();
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