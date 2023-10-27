package com.suite.suite_suite_room_service.suiteRoom.kafka.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResPaymentParticipantDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteRoomProducer {

    @Value("${topic.SUITEROOM_CONTRACT_CREATION}") private String SUITEROOM_CONTRACT_CREATION;
    @Value("${topic.SUITEROOM_CANCELJOIN}") private String SUITEROOM_CANCELJOIN;
    @Value("${topic.DEPOSIT_PAYMENT}") private String DEPOSIT_PAYMENT;
    @Value("${topic.SUITEROOM_START}") private String SUITEROOM_START;
    @Value("${topic.SUITEROOM_TERMINATE}") private String SUITEROOM_TERMINATE;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public void sendPaymentMessage(SuiteRoom suiteRoom, Long hostMemberId, AuthorizerDto authorizerDto, boolean isHost, boolean isPay) {
        Map<String, Object> data = payDepositAmount(suiteRoom, hostMemberId, authorizerDto, isHost);
        log.info("SuiteRoom-Join message : {}", data);
        this.kafkaTemplate.send(isPay ? DEPOSIT_PAYMENT : SUITEROOM_CANCELJOIN, makeMessage(data));
    }

    public void suiteRoomContractCreationProducer(Long suiteRoomId, List<Participant> participants, SuiteRoom suiteRoom) {
        Map<String, Object> data = updateStatusReadyToStart(suiteRoomId, participants, suiteRoom);
        log.info("SuiteRoom-Contract-Creation message : {}", data);
        this.kafkaTemplate.send(SUITEROOM_CONTRACT_CREATION, makeMessage(data));
    }

    public void suiteRoomStartProducer(SuiteRoom suiteRoom, List<ResPaymentParticipantDto> resPaymentParticipantDtos) {
        Map<String, Object> data = startSuiteRoom(suiteRoom, resPaymentParticipantDtos);
        log.info("SuiteRoom-Start message : {}", data);
        this.kafkaTemplate.send(SUITEROOM_START, makeMessage(data));
    }

    public void suiteRoomTerminateProducer(Long suiteRoomId, String title, int depositAmount, List<ResPaymentParticipantDto> resPaymentParticipantDtos) {
        Map<String, Object> data = terminateSuiteRoom(suiteRoomId, title, depositAmount, resPaymentParticipantDtos);
        log.info("SuiteRoom-Start message : {}", data);
        this.kafkaTemplate.send(SUITEROOM_TERMINATE, makeMessage(data));
    }

    private Map<String, Object> terminateSuiteRoom(Long suiteRoomId, String title, int depositAmount, List<ResPaymentParticipantDto> resPaymentParticipantDtos) {
        try {
            Map<String, Object> map = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parsedArrayJson = (JSONArray) JSONValue.parse(objectMapper.writeValueAsString(resPaymentParticipantDtos));
            map.put("suiteRoomId", suiteRoomId);
            map.put("title", title);
            map.put("depositAmount", depositAmount);
            map.put("participants", parsedArrayJson);
            return map;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> startSuiteRoom(SuiteRoom suiteRoom, List<ResPaymentParticipantDto> resPaymentParticipantDtos) {
        try {
            Map<String, Object> map = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parsedObject = (JSONArray) JSONValue.parse(objectMapper.writeValueAsString(resPaymentParticipantDtos));
            map.put("suiteRoomId", suiteRoom.getSuiteRoomId());
            map.put("suiteRoomTitle", suiteRoom.getTitle());
            map.put("depositAmount", suiteRoom.getDepositAmount());
            map.put("minAttendanceRate", suiteRoom.getMinAttendanceRate());
            map.put("minMissionCompleteRate", suiteRoom.getMinMissionCompleteRate());
            map.put("participants", parsedObject);
            return map;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    private Map<String, Object> payDepositAmount(SuiteRoom suiteRoom, Long hostMemberId, AuthorizerDto authorizerDto, boolean isHost) {
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject parsedObject = (JSONObject) JSONValue.parse(authorizerDto.toJSONString());
            map.put("suiteRoomId", suiteRoom.getSuiteRoomId());
            map.put("hostMemberId", hostMemberId);
            map.put("suiteRoomTitle", suiteRoom.getTitle());
            map.put("authorizerDto", parsedObject);
            map.put("depositAmount", suiteRoom.getDepositAmount());
            map.put("isHost", isHost);
            return map;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> updateStatusReadyToStart(Long suiteRoomId, List<Participant> participants, SuiteRoom suiteRoom) {
        Map<String, Object> map = new HashMap<>();
        List<String> participantsIds = new ArrayList<>();
        List<String> participantsNames = new ArrayList<>();
        List<String> signatures = new ArrayList<>();

        Long participantCount = convertParticipantsToDto(participants, participantsIds, participantsNames, signatures, map).stream().count();

        ResSuiteRoomDto resSuiteRoomDto = suiteRoom.toResSuiteRoomDto(participantCount, false);
        map.put("participant_ids", participantsIds);
        map.put("signatures", signatures);
        map.put("participant_names", participantsNames);
        map.put("suite_room_id", suiteRoomId);
        map.put("title", resSuiteRoomDto.getTitle());
        map.put("group_capacity", participantCount);
        map.put("group_deposit_per_person", resSuiteRoomDto.getDepositAmount());
        map.put("group_period", daysDiffCalculatorTimeStamp(resSuiteRoomDto.getStudyDeadline()));
        map.put("recruitment_period", daysDiffCalculatorTimeStamp(resSuiteRoomDto.getCreatedAt()));
        map.put("group_created_at", daysConvertToStringFormat(resSuiteRoomDto.getCreatedAt()));
        map.put("minimum_attendance", resSuiteRoomDto.getMinAttendanceRate());
        map.put("minimum_mission_completion", resSuiteRoomDto.getMinMissionCompleteRate());
        return map;
    }

    private List<ResPaymentParticipantDto> convertParticipantsToDto(List<Participant> participants, List<String> participantsIds, List<String> participantsNames, List<String> signatures, Map<String, Object> map) {
        return participants
                .stream()
                .map(
                        p -> {
                            if(p.getStatus() == SuiteStatus.PLAIN) throw new CustomException(StatusCode.PLAIN_USER_EXIST);
                            if(p.getIsHost()) {
                                map.put("leader_id", p.getEmail());
                                map.put("leader_name", p.getName());
                            }
                            participantsIds.add(p.getEmail());
                            participantsNames.add(p.getName());
                            signatures.add(p.getEmail().split("@")[0] +"는 계약서의 모든 조항에 대해 동의합니다.");
                            p.updateStatus(SuiteStatus.START);
                            return p.toResPaymentParticipantDto();
                        }
                ).collect(Collectors.toList());
    }

    private long daysDiffCalculatorTimeStamp(Timestamp targetTime) {
        Timestamp nowTimestamp = new Timestamp(System.currentTimeMillis());

        LocalDate nowDate = nowTimestamp.toLocalDateTime().toLocalDate();
        LocalDate targetDate = targetTime.toLocalDateTime().toLocalDate();

        return Math.abs(ChronoUnit.DAYS.between(targetDate, nowDate));
    }


    private String daysConvertToStringFormat(Timestamp timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yy년 M월 d일");
        return sdf.format(timestamp);
    }

    private String makeMessage(Map<String, Object> data) {
        JSONObject obj = new JSONObject();
        obj.put("uuid", "SuiteRoomProducer/" + Instant.now().toEpochMilli());
        obj.put("data", data);
        return obj.toJSONString();
    }

}
