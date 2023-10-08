package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.ResPaymentParticipantDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockAuthorizer;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockParticipant;
import com.suite.suite_suite_room_service.suiteRoom.mockEntity.MockSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
@Transactional
@Rollback
class ParticipantServiceTest {

    @Autowired private ParticipantRepository participantRepository;
    @Autowired private SuiteRoomRepository suiteRoomRepository;

    private final SuiteRoom suiteRoom = MockSuiteRoom.getMockSuiteRoom("test", true, false).toSuiteRoomEntity();
    private final Participant participantHost = MockParticipant.getMockParticipant(true, MockAuthorizer.YH());

    @BeforeEach
    public void setUp() {
        suiteRoom.addParticipant(participantHost);
        suiteRoomRepository.save(suiteRoom);
        participantRepository.save(participantHost);
    }


    /**
     * @수정 요구사항
     * 결제 서비스와 붙일 카프카 추가 ( 포인트 차감 )
     * 이미 시작된 스터디에 간발의 차로 신청을 누르는 버저비트상황에서 참여가 안되도록
     * Participant.getStatus == READY 일 때에만 참여 가능하게 수정
     * */
    @Test
    @DisplayName("스위트룸 참가하기")
    public void addParticipant() {
        //given
        Long targetSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto DH = MockAuthorizer.DH();
        //when
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(targetSuiteRoomId).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND); } )
        );
        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(targetSuiteRoomId, DH.getMemberId()).ifPresent(
                participant -> { assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.ALREADY_EXISTS_PARTICIPANT); } );}
        );
        Participant newParticipant = MockParticipant.getMockParticipant(false, DH);
        suiteRoom.addParticipant(newParticipant);
        participantRepository.save(newParticipant);
        List<Participant> result = participantRepository.findBySuiteRoom_SuiteRoomId(targetSuiteRoomId);
        //then
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(2)
        );
    }

    /**
     * @수정 요구사항
     * 결제 서비스와 붙일 카프카 추가 ( 포인트 환불 )
     * 방장은 그러면 스터디 파투를 할 때 포인트를 환급받을 백로그가 필요
     * */
    @Test
    @DisplayName("스위트룸 참가 취소")
    public void removeParticipant() {
        //given
        Long targetSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto authorizerDto = MockAuthorizer.YH();
        //when
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(targetSuiteRoomId, authorizerDto.getMemberId()).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.FAILED_REQUEST); }));

        if(participant.getIsHost())
            assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.CAN_NOT_CALCEL_SUITEROOM); });

        if(participant.getStatus().equals(SuiteStatus.READY))
            System.out.println("kafka");
        participantRepository.deleteBySuiteRoom_SuiteRoomIdAndMemberId(targetSuiteRoomId, authorizerDto.getMemberId());
        List<Participant> result = participantRepository.findBySuiteRoom_SuiteRoomId(targetSuiteRoomId);
        //then
        Assertions.assertAll(
                () -> assertThat(result.size()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("스위트룸 체크인 완료 - 방장")
    public void updatePayment() {
        //given
        Long consumedSuiteRoomId = suiteRoom.getSuiteRoomId();
        Long consumedMemberId = participantHost.getMemberId();
        //when
        Participant targetParticipant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(consumedSuiteRoomId, consumedMemberId, participantHost.getIsHost()).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND);})
        );
        SuiteRoom targetSuiteRoom = suiteRoomRepository.findBySuiteRoomId(consumedSuiteRoomId).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND);})
        );

        targetParticipant.updateStatus(SuiteStatus.READY);

        if (targetParticipant.getIsHost()) {
            targetSuiteRoom.openSuiteRoom();
        }

        System.out.println("결제서비스 kafka 메시지 큐에 READY 성공 메시지를 넣습니다.");

        //then
        Participant assertParticipant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(consumedSuiteRoomId, consumedMemberId, participantHost.getIsHost()).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND);})
        );
        SuiteRoom assertSuiteRoom = suiteRoomRepository.findBySuiteRoomId(consumedSuiteRoomId).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND);})
        );
        Assertions.assertAll(
                () -> assertThat(assertParticipant.getStatus()).isEqualTo(SuiteStatus.READY),
                () -> assertThat(assertSuiteRoom.getIsOpen()).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("스위트룸 체크인 완료 - 참가자")
    public void updatePaymentGuest() {
        //given
        Long consumedSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto DH = MockAuthorizer.DH();

        addParticipantForTest(DH, consumedSuiteRoomId, false);
        SuiteRoom targetSuiteRoom = suiteRoomRepository.findBySuiteRoomId(consumedSuiteRoomId).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND); } )
        );
        //when
        Participant targetParticipant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(consumedSuiteRoomId, DH.getMemberId(), false).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND);})
        );

        if (!targetSuiteRoom.getIsOpen())
            assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.IS_NOT_OPEN);});


        targetParticipant.updateStatus(SuiteStatus.READY);

        System.out.println("결제서비스 kafka 메시지 큐에 READY 성공 메시지를 넣습니다.");
        //then
        Participant assertParticipant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(consumedSuiteRoomId, DH.getMemberId(), false).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND);})
        );
        SuiteRoom assertSuiteRoom = suiteRoomRepository.findBySuiteRoomId(consumedSuiteRoomId).orElseThrow(
                () -> assertThrows(CustomException.class, () -> { throw new CustomException(StatusCode.NOT_FOUND);})
        );
        Assertions.assertAll(
                () -> assertThat(assertParticipant.getStatus()).isEqualTo(SuiteStatus.READY),
                () -> assertThat(assertSuiteRoom.getIsOpen()).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("스위트룸 체크인 목록 확인 - 납부자")
    //@Transactional // for update status
    public void listUpPaymentParticipants() {
        //given
        Long targetSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto DH = MockAuthorizer.DH();
        addParticipantForTest(DH, targetSuiteRoomId, true);

        Participant participantGuest = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(targetSuiteRoomId,DH.getMemberId(),false)
                .orElseThrow(() -> assertThrows(CustomException.class, () -> {throw new CustomException(StatusCode.NOT_FOUND);}));
        participantGuest.updateStatus(SuiteStatus.READY);
        //when
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatusNot(targetSuiteRoomId, SuiteStatus.PLAIN)
                .stream().map(
                        participant -> participant.toResPaymentParticipantDto()
                ).collect(Collectors.toList());
        //then
        Assertions.assertAll(
                () -> assertThat(resPaymentParticipantDtos.size()).isEqualTo(2),
                () -> assertThat(resPaymentParticipantDtos).allMatch(dto -> dto.getStatus() != SuiteStatus.PLAIN),
                () -> assertThat(resPaymentParticipantDtos).allMatch(dto -> dto.getClass() == ResPaymentParticipantDto.class)
        );
    }



    @Test
    @DisplayName("스터디 그룹 시작")
    public void updateParticipantsStatusReadyToStart() {
        //given
        Long targetSuiteRoomId = suiteRoom.getSuiteRoomId();
        AuthorizerDto DH = MockAuthorizer.DH();
        AuthorizerDto JH = MockAuthorizer.JH();
        addParticipantForTest(DH, targetSuiteRoomId, true);
        addParticipantForTest(JH, targetSuiteRoomId, true);
        //when
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(targetSuiteRoomId, SuiteStatus.READY)
                .stream()
                .map(
                        participant -> {
                            participant.updateStatus(SuiteStatus.START);
                            return participant.toResPaymentParticipantDto();
                        }
                ).collect(Collectors.toList());

        System.out.println("kafka 프로듀싱 to 블록체인 서비스");
        //then
        Assertions.assertAll(
                () -> assertThat(resPaymentParticipantDtos.size()).isEqualTo(3),
                () -> assertThat(resPaymentParticipantDtos).allMatch(dto -> dto.getStatus() == SuiteStatus.START),
                () -> assertThat(resPaymentParticipantDtos).allMatch(dto -> dto.getClass() == ResPaymentParticipantDto.class)
        );

    }

    private void addParticipantForTest(AuthorizerDto authorizerDto, Long suiteRoomId, Boolean updateStatus) {
        Participant participantGuest = MockParticipant.getMockParticipant(false, authorizerDto);
        SuiteRoom targetSuiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId)
                .orElseThrow(() -> assertThrows(CustomException.class, () -> {throw new CustomException(StatusCode.NOT_FOUND);}));

        participantHost.updateStatus(SuiteStatus.READY);
        targetSuiteRoom.openSuiteRoom();

        if(updateStatus) participantGuest.updateStatus(SuiteStatus.READY);

        targetSuiteRoom.addParticipant(participantGuest);

        participantRepository.save(participantGuest);

    }


}