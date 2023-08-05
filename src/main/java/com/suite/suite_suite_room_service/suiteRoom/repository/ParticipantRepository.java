package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(Long suiteRoomId, Long memberId, boolean isHost);
    Optional<Participant> findBySuiteRoom_SuiteRoomIdAndMemberId(Long suiteRoomId, Long memberId);

    List<Participant> findAllBySuiteRoom_SuiteRoomIdAndStatusNot(Long suiteRoomId, SuiteStatus suiteStatus);
    List<Participant> findBySuiteRoom_SuiteRoomId(Long suiteRoomId);

    Long countBySuiteRoom_SuiteRoomId(Long suiteRoomId);

    void deleteBySuiteRoom_SuiteRoomIdAndMemberId(Long suiteRoomId, Long memberId);
    Boolean existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(Long suiteRoomId, Long memberId, boolean ishost);
}
