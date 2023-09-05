package com.suite.suite_suite_room_service.suiteRoom.kafka.producer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteRoomProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${topic.DEPOSIT_PAYMENT}") private String DEPOSIT_PAYMENT;

    public void sendPaymentMessage(SuiteRoom suiteRoom, AuthorizerDto authorizerDto, boolean isHost) {
        Map<String, Object> data = payDepositAmount(suiteRoom, authorizerDto, isHost);
        log.info("SuiteRoom-Join message : {}", data);
        JSONObject obj = new JSONObject();
        obj.put("uuid", "SuiteRoomProducer/" + Instant.now().toEpochMilli());
        obj.put("data", data);
        this.kafkaTemplate.send(DEPOSIT_PAYMENT, obj.toJSONString());
    }

    private Map<String, Object> payDepositAmount(SuiteRoom suiteRoom, AuthorizerDto authorizerDto, boolean isHost) {
        try {
            Map<String, Object> map = new HashMap<>();
            JSONObject parsedObject = (JSONObject) JSONValue.parse(authorizerDto.toJSONString());
            map.put("suiteRoomId", suiteRoom.getSuiteRoomId());
            map.put("authorizerDto", parsedObject);
            map.put("depositAmount", suiteRoom.getDepositAmount());
            map.put("isHost", isHost);
            return map;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
