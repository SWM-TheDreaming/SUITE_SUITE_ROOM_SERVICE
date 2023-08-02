package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(Long suiteRoomId, Long memberId, boolean ishost);
    Optional<Participant> findBySuiteRoom_SuiteRoomIdAndMemberId(Long suiteRoomId, Long memberId);
    List<Participant> findBySuiteRoom_SuiteRoomId(Long suiteRoomId);

    Long countBySuiteRoom_SuiteRoomId(Long suiteRoomId);

    Boolean existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(Long suiteRoomId, Long memberId, boolean ishost);
}
