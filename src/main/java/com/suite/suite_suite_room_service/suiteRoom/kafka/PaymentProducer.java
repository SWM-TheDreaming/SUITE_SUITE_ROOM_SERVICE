package com.suite.suite_suite_room_service.suiteRoom.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentProducer {

    @Value("${payment.topic}")
    private String paymentTopic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendPaymentMessage(String message) {
        log.info("paymentTopic message : {}", message);
        this.kafkaTemplate.send(paymentTopic, message);
    }

}
