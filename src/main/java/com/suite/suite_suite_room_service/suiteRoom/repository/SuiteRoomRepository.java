package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface SuiteRoomRepository extends JpaRepository<SuiteRoom, Long> {

    Optional<SuiteRoom> findByTitle(String title);
    Optional<SuiteRoom> findBySuiteRoomId(Long suiteRoomId);
}
