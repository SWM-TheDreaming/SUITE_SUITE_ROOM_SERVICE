package com.suite.suite_suite_room_service.suiteRoom.service;


import com.suite.suite_suite_room_service.suiteRoom.dto.ReqUpdateSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.*;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Assertions;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.lang.reflect.Member;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;



@Transactional
@DataJpaTest
class SuiteRoomServiceTest {

    @Autowired private SuiteRoomRepository suiteRoomRepository;
    @Autowired private ParticipantRepository participantRepository;

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

    private final SuiteRoom  suiteRoom = MockSuiteRoom.getMockSuiteRoom("test", true).toSuiteRoomEntity();
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
    public void createSuiteRoom() {
        //given
        SuiteRoom createSuiteRoom = MockSuiteRoom.getMockSuiteRoom("test 2",true).toSuiteRoomEntity();
        Participant createdHost = MockParticipant.getMockParticipant(true, MockAuthorizer.YH());
        //when
        createSuiteRoom.addParticipant(createdHost);
        SuiteRoom result_suiteRoom = suiteRoomRepository.save(createSuiteRoom);
        Participant result_participant = participantRepository.save(createdHost);
        //then
        Assertions.assertAll(
                () -> assertThat(result_suiteRoom.getTitle()).isEqualTo(createSuiteRoom.getTitle()),
                () -> assertThat(result_suiteRoom).isEqualTo(result_participant.getSuiteRoom())
        );
    }

    @Test
    @DisplayName("스위트룸 비공개생성")
    public void createSecretSuiteRoom() {
        //given
        SuiteRoom createSuiteRoom = MockSuiteRoom.getMockSuiteRoom("test security",false).toSuiteRoomEntity();
        Participant createdHost = MockParticipant.getMockParticipant(true, MockAuthorizer.YH());
        //when
        createSuiteRoom.addParticipant(createdHost);
        SuiteRoom result_suiteRoom = suiteRoomRepository.save(createSuiteRoom);
        Participant result_participant = participantRepository.save(createdHost);

        //then
        Assertions.assertAll(
                () -> assertThat(result_suiteRoom.getIsPublic()).isEqualTo(createSuiteRoom.getIsPublic()),
                () -> assertThat(result_suiteRoom.getPassword()).isEqualTo(createSuiteRoom.getPassword()),
                () -> assertThat(result_suiteRoom).isEqualTo(result_participant.getSuiteRoom())
        );

    }

    @Test
    @DisplayName("스위트룸 수정")
    public void updateSuiteRoom() {
        //given
        Long targetSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto YH = MockAuthorizer.YH();
        //when
        Optional<Participant> member = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(targetSuiteRoomId, YH.getMemberId(), true);
        if(member.isEmpty())
            assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.FORBIDDEN); });

        ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto = ReqUpdateSuiteRoomDto.builder()
                .content("updated content")
                .channelLink("http://www.naver.com").build();

        suiteRoom.updateSuiteRoom(reqUpdateSuiteRoomDto);
        suiteRoomRepository.save(suiteRoom);
        //then
        Assertions.assertAll(
                () -> assertThat(suiteRoom.getContent()).isEqualTo(reqUpdateSuiteRoomDto.getContent()),
                () -> assertThat(suiteRoom.getChannelLink()).isEqualTo(reqUpdateSuiteRoomDto.getChannelLink())
        );
    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인")
    void getAllSuiteRooms() {
        //given
        SuiteRoom createSuiteRoom = MockSuiteRoom.getMockSuiteRoom("test test",true).toSuiteRoomEntity();
        createSuiteRoom.addParticipant(participantHost);
        suiteRoomRepository.save(createSuiteRoom);


        List<SuiteRoom> suiteRooms = suiteRoomRepository.findAll();
        List<ResSuiteRoomDto> assertionSuiteRooms = suiteRooms.stream().map(
                suiteRoom -> suiteRoom.toResSuiteRoomDto(
                        participantRepository.countBySuiteRoom_SuiteRoomId(suiteRoom.getSuiteRoomId()),
                        participantRepository.existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoom.getSuiteRoomId(), MockAuthorizer.YH().getMemberId(), true)
                )
        ).collect(Collectors.toList());


        //then
        Assertions.assertAll(
                ()-> assertThat(assertionSuiteRooms.get(0).getClass()).isEqualTo(ResSuiteRoomDto.class),
                ()-> assertThat(assertionSuiteRooms.size()).isGreaterThanOrEqualTo(2)
        );

    }

    @Test
    @DisplayName("스위트룸 그룹 목록 확인 - 토큰 재발급 O")
    void getAllSuiteRoomsRenewalToken() {
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
    void getAllSuiteRoomsHoldToken() {
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
    public void getSuiteRoom() {
        //given
        Long targetSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto DH = MockAuthorizer.DH();
        //when
        Optional<SuiteRoom> findSuiteRoomBySuiteRoomIdResult = suiteRoomRepository.findById(targetSuiteRoomId);
        findSuiteRoomBySuiteRoomIdResult.orElseThrow(
                () -> {
                    return assertThrows(CustomException.class, () -> {
                        throw new CustomException(StatusCode.SUITE_ROOM_NOT_FOUND);
                    });
                }
        );
        ResSuiteRoomDto resSuiteRoomDto = findSuiteRoomBySuiteRoomIdResult.get().toResSuiteRoomDto(
                participantRepository.countBySuiteRoom_SuiteRoomId(targetSuiteRoomId),
                participantRepository.existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(targetSuiteRoomId,DH.getMemberId(), true)
        );
        //then
        assertAll(
                () -> assertThat(resSuiteRoomDto.getContent()).isEqualTo(suiteRoom.getContent()),
                () -> assertThat(resSuiteRoomDto.getDepositAmount()).isEqualTo(suiteRoom.getDepositAmount())
        );

    }
    /**
     * @수정 요구사항
     * 포인트 환급에 대한 처리가 필요. 취소가 아닌 파투이므로 강제 환급이 필요함.
     * */
    @Test
    @DisplayName("스터디 파투")
    public void deleteSuiteRoom() {
        //given
        Long targetSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto YH = MockAuthorizer.YH();
        //when
        Optional<Participant> host = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(suiteRoom.getSuiteRoomId(), YH.getMemberId(), true);
        if(host.isEmpty()) {
            assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.FORBIDDEN); });
        }

        if(!host.get().getStatus().equals(SuiteStatus.START)) {
            suiteRoomRepository.deleteBySuiteRoomId(targetSuiteRoomId);
            //환급 카프카
        }else throw new CustomException(StatusCode.NOT_DELETE_SUITE_ROOM);

        //then
        List<Participant> result = participantRepository.findBySuiteRoom_SuiteRoomId(targetSuiteRoomId);
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(0)
        );
    }



}