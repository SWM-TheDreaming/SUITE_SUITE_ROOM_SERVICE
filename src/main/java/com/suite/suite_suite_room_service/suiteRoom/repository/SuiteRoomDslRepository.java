package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.dto.StudyCategory;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuiteRoomDslRepository {

    List<SuiteRoom> findOpenSuiteWithSearch(boolean isOpen, List<StudyCategory> subjects, String keyword, Pageable pageable);
}
