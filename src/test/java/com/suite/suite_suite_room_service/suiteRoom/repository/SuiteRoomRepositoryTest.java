package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@ExtendWith(SpringExtension.class)
@DataJpaTest
class SuiteRoomRepositoryTest {
    @Autowired
    private SuiteRoomRepository suiteRoomRepository;
    
    @Test
    @DisplayName("스위트룸 생성")
    void saveSuiteRoom() {


    }

    private Timestamp getTimeStamp(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse("2023-08-23 12:57:23", formatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Seoul"));
        return Timestamp.from(zonedDateTime.toInstant());
    }

}