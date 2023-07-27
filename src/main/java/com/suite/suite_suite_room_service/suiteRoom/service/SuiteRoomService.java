package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;

import java.util.List;
import java.util.Optional;

public interface SuiteRoomService {
    List<SuiteRoomDto> getAllSuiteRooms();

    Optional<SuiteRoom> getSuiteRoom();
    Optional<List<SuiteRoom>> getAllProgressRooms();
    Optional<List<SuiteRoom>> getAllCompletionRooms();
    SuiteRoom createSuiteRoom(SuiteRoom suiteRoom);
    Optional<SuiteRoom> joinRoom();
    Optional<SuiteRoom> deleteRoom();
    Optional<SuiteRoom> renewalRoom();
    Optional<?> commitPaymentStatus();
}
