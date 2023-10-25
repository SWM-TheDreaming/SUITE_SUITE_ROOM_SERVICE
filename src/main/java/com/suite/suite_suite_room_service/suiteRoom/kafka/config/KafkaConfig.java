package com.suite.suite_suite_room_service.suiteRoom.kafka.config;


import com.suite.suite_suite_room_service.suiteRoom.slack.SlackMessage;

import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.service.AnpService;



import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${slack.webhook-url}")
    private String slackWebhookUrl;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String,Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configs);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // auto.commit 설정을 수동으로 변경
        //configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); // 자동 커밋 비활성화

        // auto.offset.reset 설정을 수동으로 변경
        //configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 가장 초기 오프셋부터 시작
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(slackErrorHandler());
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerDefaultContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public DefaultErrorHandler slackErrorHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {
            log.error("[Error] topic = {}, key = {}, value = {}, error message = {}", consumerRecord.topic(),
                    consumerRecord.key(), consumerRecord.value(), exception.getMessage());

            String errorMessage = "*에러 발생*: _<!channel> " + consumerRecord.topic() + " 메시지 처리 중 예외 발생_\n" + exception.getMessage();
            String fullErrorMessage = errorMessage + "\n\n```\n" + exception + "\n```"; // 코드 블럭으로 감싸기
            slackMessage().sendNotification(fullErrorMessage);
        }, new FixedBackOff(1000L, 3)); // 1초 간격으로 최대 3번
        errorHandler.addNotRetryableExceptions(IllegalArgumentException.class);

        return errorHandler;
    }

    @Bean
    public DefaultErrorHandler errorHandler() {
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {
            log.error("[Error] topic = {}, key = {}, value = {}, offset = {}, error message = {}", consumerRecord.topic(),
                    consumerRecord.key(), consumerRecord.value(), consumerRecord.offset(), exception.getMessage());

        }, new FixedBackOff(1000L, 3)); // 1초 간격으로 최대 3번
        errorHandler.addNotRetryableExceptions(CustomException.class);

        return errorHandler;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SlackMessage slackMessage() {
        return new SlackMessage(restTemplate(), slackWebhookUrl);
    }
    @Bean
    public AnpService anpService(RestTemplate restTemplate) {
<<<<<<< HEAD
        String GET_POINT_URI = "http://api.suitestudy.com/anp/point/";
=======
        String GET_POINT_URI = "http://bongu.suitestudy.com/anp/point/";
>>>>>>> main
        return new AnpService(GET_POINT_URI, restTemplate);
    }


}
