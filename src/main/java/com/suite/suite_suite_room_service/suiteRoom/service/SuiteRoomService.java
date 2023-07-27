package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

import java.util.List;
import java.util.Optional;

public interface SuiteRoomService {
    Optional<List<SuiteRoom>> getAllSuiteRooms();

    Optional<SuiteRoom> getSuiteRoom();
    Optional<List<SuiteRoom>> getAllProgressRooms();
    Optional<List<SuiteRoom>> getAllCompletionRooms();
    Message createSuiteRoom(ReqSuiteRoom reqSuiteRoom, AuthorizerDto authorizerDto);
    Optional<SuiteRoom> joinRoom();
    Optional<SuiteRoom> deleteRoom();
    Optional<SuiteRoom> renewalRoom();
    Optional<?> commitPaymentStatus();
}
