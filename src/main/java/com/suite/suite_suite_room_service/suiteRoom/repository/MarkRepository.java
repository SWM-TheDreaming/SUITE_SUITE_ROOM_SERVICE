package com.suite.suite_suite_room_service.suiteRoom.repository;


import com.suite.suite_suite_room_service.suiteRoom.entity.Mark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MarkRepository extends JpaRepository<Mark, Long> {

    Optional<Mark> findByMemberIdAndSuiteRoom_SuiteRoomId(Long memberId, Long suiteRoomId);

    List<Mark> findByMemberId(Long memberId);

    Long countBySuiteRoom_SuiteRoomId(Long suiteRoomId);

    void deleteByMemberIdAndSuiteRoom_SuiteRoomId(Long memberId, Long suiteRoomId);

    Boolean existsBySuiteRoom_SuiteRoomIdAndMemberId(Long suiteRoomId, Long memberId);
}
