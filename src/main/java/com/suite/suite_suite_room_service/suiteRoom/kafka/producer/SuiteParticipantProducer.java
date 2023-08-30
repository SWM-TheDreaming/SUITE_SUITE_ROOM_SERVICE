package com.suite.suite_suite_room_service.suiteRoom.kafka.producer;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SuiteParticipantProducer {
    @Value("${topic.SUITEROOM_CONTRACT_CREATION}")
    private String SUITEROOM_CONTRACT_CREATION;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void suiteRoomContractCreationProducer(String message) {
        log.info("SuiteRoom-Contract-Creation message : {}", message);
        this.kafkaTemplate.send(SUITEROOM_CONTRACT_CREATION, message);
    }

}