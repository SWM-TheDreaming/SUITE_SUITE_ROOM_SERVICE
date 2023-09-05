package com.suite.suite_suite_room_service.suiteRoom.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResPaymentParticipantDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.kafka.producer.SuiteParticipantProducer;
import com.suite.suite_suite_room_service.suiteRoom.kafka.producer.SuiteRoomProducer;
import com.suite.suite_suite_room_service.suiteRoom.repository.ParticipantRepository;
import com.suite.suite_suite_room_service.suiteRoom.repository.SuiteRoomRepository;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService{
    private final SuiteRoomRepository suiteRoomRepository;
    private final ParticipantRepository participantRepository;
    private final SuiteRoomProducer suiteRoomProducer;
    private final SuiteParticipantProducer suiteParticipantProducer;
    private final ObjectMapper objectMapper;
    private final AnpService anpService;

    @Override
    @Transactional
    public void addParticipant(Long suiteRoomId, AuthorizerDto authorizerDto) {
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId).orElseThrow(
                () -> new CustomException(StatusCode.NOT_FOUND));

        participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId()).ifPresent(
                member -> { throw new CustomException(StatusCode.ALREADY_EXISTS_PARTICIPANT); });

        if(anpService.getPoint(authorizerDto.getMemberId()) < suiteRoom.getDepositAmount())
            throw new CustomException(StatusCode.FAILED_PAY);

        suiteRoomProducer.sendPaymentMessage(suiteRoom, authorizerDto, false);
    }

    @Override
    @Transactional
    public void removeParticipant(Long suiteRoomId, AuthorizerDto authorizerDto) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId()).orElseThrow(
                () -> new CustomException(StatusCode.FAILED_REQUEST));

        if(participant.getStatus().equals(SuiteStatus.READY)) {

        }

        if(participant.getIsHost())
            throw new CustomException(StatusCode.CAN_NOT_CALCEL_SUITEROOM);
        participantRepository.deleteBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, authorizerDto.getMemberId());
    }

    @Override
    @Transactional
    public void updatePaymentParticipant(Long suiteRoomId, Long memberId) {
        Participant participant = participantRepository.findBySuiteRoom_SuiteRoomIdAndMemberId(suiteRoomId, memberId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });
        SuiteRoom suiteRoom = suiteRoomRepository.findBySuiteRoomId(suiteRoomId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); });
        if (participant.getIsHost())
            suiteRoom.openSuiteRoom();

        participant.updateStatus(SuiteStatus.READY);

        System.out.println("결제서비스 kafka 메시지 큐에 READY 성공 메시지를 넣습니다.");

    }

    @Override
    public List<ResPaymentParticipantDto> listUpPaymentParticipants(Long suiteRoomId) {
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.READY)
                .stream().map(
                        participant -> participant.toResPaymentParticipantDto()
                ).collect(Collectors.toList());

        return resPaymentParticipantDtos;
    }

    @Override
    public List<ResPaymentParticipantDto> listUpNotYetPaymentParticipants(Long suiteRoomId) {
        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.PLAIN)
                .stream().map(
                        participant -> participant.toResPaymentParticipantDto()
                ).collect(Collectors.toList());

        return resPaymentParticipantDtos;
    }

    @Override
    @Transactional
    public List<ResPaymentParticipantDto> updateParticipantsStatusReadyToStart(Long suiteRoomId) {
        Map<String, Object> map = new HashMap<>();

        List<String> participantsIds = new ArrayList<>();
        List<String> signatures = new ArrayList<>();


        List<ResPaymentParticipantDto> resPaymentParticipantDtos = participantRepository.findAllBySuiteRoom_SuiteRoomIdAndStatus(suiteRoomId, SuiteStatus.READY)
                .stream()
                .map(
                        participant -> {
                            if(participant.getStatus() == SuiteStatus.PLAIN) throw new CustomException(StatusCode.PLAIN_USER_EXIST);
                            if(participant.getIsHost()) map.put("leader_id", participant.getEmail());
                            participantsIds.add(participant.getEmail());
                            signatures.add(participant.getEmail().split("@")[0] +"는 계약서의 모든 조항에 대해 동의합니다.");

                            participant.updateStatus(SuiteStatus.START);
                            return participant.toResPaymentParticipantDto();
                        }
                ).collect(Collectors.toList());

        Long participantCount = resPaymentParticipantDtos.stream().count();

        ResSuiteRoomDto resSuiteRoomDto = suiteRoomRepository.findBySuiteRoomId(suiteRoomId)
                .orElseThrow(() -> { throw new CustomException(StatusCode.NOT_FOUND); })
                .toResSuiteRoomDto(participantCount, false);

        try {
            map.put("participant_ids", objectMapper.writeValueAsString(participantsIds));
            map.put("signatures", objectMapper.writeValueAsString(signatures));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        map.put("suite_room_id", suiteRoomId);
        map.put("title", resSuiteRoomDto.getTitle());
        map.put("group_capacity", participantCount);
        map.put("group_deposit_per_person", resSuiteRoomDto.getDepositAmount());
        map.put("group_period", daysDiffCalculator(resSuiteRoomDto.getStudyDeadline()));
        map.put("recruitment_period", daysDiffCalculator(resSuiteRoomDto.getRecruitmentDeadline()));
        map.put("minimum_attendance", resSuiteRoomDto.getMinAttendanceRate());
        map.put("minimum_mission_completion",resSuiteRoomDto.getMinMissionCompleteRate());

        suiteParticipantProducer.suiteRoomContractCreationProducer(generateJSONData(map));

        return resPaymentParticipantDtos;
    }

    private String generateJSONData(Object data) {
        JSONObject obj = new JSONObject();
        obj.put("uuid", "UserRegistrationProducer/" + Instant.now().toEpochMilli());
        obj.put("data", data);
        return obj.toJSONString();
    }

    private long daysDiffCalculator(Timestamp targetTime) {
        Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());

        LocalDate nowDate = nowTimestamp.toLocalDateTime().toLocalDate();
        LocalDate targetDate = targetTime.toLocalDateTime().toLocalDate();

        return ChronoUnit.DAYS.between(targetDate, nowDate);
    }

    private String listToJSONArray(List list) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(list);

        return jsonArray.toJSONString();
    }

}
