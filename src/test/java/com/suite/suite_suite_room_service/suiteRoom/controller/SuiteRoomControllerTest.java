package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.*;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
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

import java.util.Arrays;
import java.util.Date;
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
    @Value("${token.DR}")
    private String DR_JWT;

    @Value("${jwt.access.key}")
    private String accessSecretKey;
    @Value("${jwt.refresh.key}")
    private String refreshSecretKey;
    @Value("${jwt.expiring.key}")
    private String expiringSecretKey;
    @Value("${jwt.non-expiring.key}")
    private String nonExpiringSecretKey;
    @Value("${jwt.access.validtime}")
    private long accessTokenValidTime;
    @Value("${jwt.refresh.validtime}")
    private long refreshTokenValidTime;
    @Value("${jwt.expiring.validtime}")
    private long expiringTokenValidTime;
    @Value("${jwt.non-expiring.validtime}")
    private long nonExpiringTokenValidTime;

    private final SuiteRoom suiteRoom = MockSuiteRoom.getMockSuiteRoom("test", true, false).toSuiteRoomEntity();
    private final Participant participantHost = MockParticipant.getMockParticipant(true, MockAuthorizer.YH());
    private MockJwtCreator mockJwtCreator;
    private MockJwtCreator mockExpiringJwtCreator;
    private MockJwtCreator mockNonExpiringJwtCreator;
    @BeforeEach
    public void setUp() {
        mockJwtCreator = new MockJwtCreator(accessSecretKey, refreshSecretKey, accessTokenValidTime, refreshTokenValidTime);
        mockExpiringJwtCreator = new MockJwtCreator(expiringSecretKey, refreshSecretKey, expiringTokenValidTime, refreshTokenValidTime);
        mockNonExpiringJwtCreator = new MockJwtCreator(nonExpiringSecretKey, refreshSecretKey, nonExpiringTokenValidTime, refreshTokenValidTime);

        suiteRoom.addParticipant(participantHost);
        suiteRoomRepository.save(suiteRoom);
        participantRepository.save(participantHost);
    }

    @Test
    @DisplayName("스위트룸 생성")
    public void createSuiteRoom() throws Exception {
        //given
        ReqSuiteRoomCreationDto reqSuiteRoomCreationDto = MockSuiteRoom.getMockSuiteRoom("title darren", true, false);
        String body = mapper.writeValueAsString(reqSuiteRoomCreationDto);
        //when
        String responseBody = postRequest("/suite/suiteroom/registration", DR_JWT, body);
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
        SuiteRoom createSuiteRoom = MockSuiteRoom.getMockSuiteRoom("test test",true, true).toSuiteRoomEntity();
        createSuiteRoom.addParticipant(participantHost);
        suiteRoomRepository.save(createSuiteRoom);
        final String url = "/suite/suiteroom?page=0&size=5";

        String body = mapper.writeValueAsString(ReqListUpSuiteRoomDto.builder().subjects(Arrays.asList(StudyCategory.TOEIC)).build());

        //when
        String responseBody = postRequest(url, YH_JWT, body);

        Message message = mapper.readValue(responseBody, new TypeReference<Message<List<ResSuiteRoomListDto>>>() {
        });
        List<ResSuiteRoomListDto> result = (List<ResSuiteRoomListDto>) message.getData();

        //then
        Assertions.assertAll(
                () -> assertThat(result.get(0).getTitle()).isEqualTo(createSuiteRoom.getTitle()),
                () -> assertThat(message.getStatusCode()).isEqualTo(200)
        );

    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인 - 토큰 재발급 O")
    void listUpSuiteRoomsRenewalToken() {
        //given
        // 방장의 AuthorizerDto 정보
        AuthorizerDto mockAuthorizer = MockAuthorizer.YH();

        MockToken expiringMockToken = mockExpiringJwtCreator.createToken(mockAuthorizer);
        Jws<Claims> expiringClaims = Jwts.parser().setSigningKey(expiringSecretKey.getBytes()).parseClaimsJws(expiringMockToken.getAccessToken());
        //when
        Date now = new Date();
        MockToken assertionToken;
        if (expiringClaims.getBody().getExpiration().getTime() < now.getTime() + 86400000) {
            assertionToken = mockJwtCreator.createToken(mockAuthorizer);
        } else {
            assertionToken = expiringMockToken;
        }
        Jws<Claims> assertionClaims = Jwts.parser().setSigningKey(accessSecretKey.getBytes()).parseClaimsJws(assertionToken.getAccessToken());
        //then
        Assertions.assertAll(
                ()-> assertThat(assertionClaims.getBody().getExpiration().getTime()).isGreaterThanOrEqualTo(expiringClaims.getBody().getExpiration().getTime())
        );
    }
    @Test
    @DisplayName("스위트룸 그룹 목록 확인 - 토큰 재발급 X")
    void listUpSuiteRoomsHoldToken() {
        //given
        // 방장의 AuthorizerDto 정보
        AuthorizerDto mockAuthorizer = MockAuthorizer.YH();

        MockToken nonExpiringMockToken = mockNonExpiringJwtCreator.createToken(mockAuthorizer);
        Jws<Claims> nonExpiringClaims = Jwts.parser().setSigningKey(nonExpiringSecretKey.getBytes()).parseClaimsJws(nonExpiringMockToken.getAccessToken());
        //when
        Date now = new Date();
        MockToken assertionToken;
        if (nonExpiringClaims.getBody().getExpiration().getTime() < now.getTime() + 86400000) {
            assertionToken = mockJwtCreator.createToken(mockAuthorizer);
        } else {
            assertionToken = nonExpiringMockToken;
        }
        Jws<Claims> assertionClaims = Jwts.parser().setSigningKey(nonExpiringSecretKey.getBytes()).parseClaimsJws(assertionToken.getAccessToken());
        //then
        Assertions.assertAll(
                ()-> assertThat(assertionClaims.getBody().getExpiration().getTime()).isEqualTo(nonExpiringClaims.getBody().getExpiration().getTime())
        );
    }

    @Test
    @DisplayName("스위트룸 모집글 확인")
    public void listUpSuiteRoom() throws Exception {
        //given
        final String url = "/suite/suiteroom/detail/" + suiteRoom.getSuiteRoomId();

        //when
        String responseBody = getRequest(url, YH_JWT);
        Message message = mapper.readValue(responseBody, new TypeReference<Message<ResSuiteRoomListDto>>() {
        });
        ResSuiteRoomListDto result = (ResSuiteRoomListDto) message.getData();

        //then
        Assertions.assertAll(
                () -> assertThat(result.getRecruitmentLimit()).isEqualTo(suiteRoom.getRecruitmentLimit()),
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