package com.suite.suite_suite_room_service.suiteRoom.repository;

import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Optional<Participant> findBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(Long suiteRoomId, Long memberId, boolean isHost);
    Optional<Participant> findBySuiteRoom_SuiteRoomIdAndMemberId(Long suiteRoomId, Long memberId);
    Optional<Participant>  findBySuiteRoom_SuiteRoomIdAndIsHost(Long suiteRoomId, boolean isHost);

    List<Participant> findAllBySuiteRoom_SuiteRoomIdAndStatusNot(Long suiteRoomId, SuiteStatus suiteStatus);

    List<Participant> findAllBySuiteRoom_SuiteRoomIdAndStatus(Long suiteRoomId, SuiteStatus suiteStatus);
    List<Participant> findBySuiteRoom_SuiteRoomId(Long suiteRoomId);

    Long countBySuiteRoom_SuiteRoomId(Long suiteRoomId);

    void deleteBySuiteRoom_SuiteRoomIdAndMemberId(Long suiteRoomId, Long memberId);
    Boolean existsBySuiteRoom_SuiteRoomIdAndMemberIdAndIsHost(Long suiteRoomId, Long memberId, boolean ishost);

    List<Participant> findByMemberIdAndStatus(Long memberId, SuiteStatus suiteStatus);

    List<Participant> findByMemberIdAndStatusNot(Long memberId, SuiteStatus suiteStatus);
}
