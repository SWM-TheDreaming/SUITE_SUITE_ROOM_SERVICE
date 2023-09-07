package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.dto.StudyCategory;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface SuiteRoomRepository extends JpaRepository<SuiteRoom, Long> {

    Page<SuiteRoom> findByIsOpenAndSubjectInOrderByCreatedDateDesc(boolean isOpen, List<StudyCategory> subject, Pageable pageable);
    Page<SuiteRoom> findByIsOpenOrderByCreatedDateDesc(boolean isOpen, Pageable pageable);
    Optional<SuiteRoom> findByTitle(String title);
    Optional<SuiteRoom> findBySuiteRoomId(Long suiteRoomId);
    void deleteBySuiteRoomId(Long suiteRoomId);

}
