package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

import java.util.List;
import java.util.Optional;

public interface SuiteRoomService {
    List<SuiteRoomDto> getAllSuiteRooms();

    Optional<SuiteRoom> getSuiteRoom();
    Optional<List<SuiteRoom>> getAllProgressRooms();
    Optional<List<SuiteRoom>> getAllCompletionRooms();
    Message createSuiteRoom(ReqSuiteRoomDto reqSuiteRoomDto, AuthorizerDto authorizerDto);
    Optional<SuiteRoom> joinRoom();
    Optional<SuiteRoom> deleteRoom();
    Optional<SuiteRoom> renewalRoom();
    Optional<?> commitPaymentStatus();
}
