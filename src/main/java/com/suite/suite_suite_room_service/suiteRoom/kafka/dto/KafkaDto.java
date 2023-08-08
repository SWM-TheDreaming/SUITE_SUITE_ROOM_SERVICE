package com.suite.suite_suite_room_service.suiteRoom.kafka.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class KafkaDto {
    String yyyymmdd;
    String skuCd;
    String fieldName;
    int diff;

    @Builder
    public KafkaDto(String yyyymmdd, String skuCd, String fieldName, int diff) {
        this.yyyymmdd = yyyymmdd;
        this.skuCd = skuCd;
        this.fieldName = fieldName;
        this.diff = diff;
    }
}