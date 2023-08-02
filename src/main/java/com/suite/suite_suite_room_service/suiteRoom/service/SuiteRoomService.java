package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqUpdateSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.SuiteRoom;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

import java.util.List;
import java.util.Optional;

public interface SuiteRoomService {
    List<ResSuiteRoomDto> getAllSuiteRooms(AuthorizerDto authorizerDto);
    ResSuiteRoomDto getSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto);
    Optional<List<SuiteRoom>> getAllProgressRooms();
    Optional<List<SuiteRoom>> getAllCompletionRooms();
    void createSuiteRoom(ReqSuiteRoomDto reqSuiteRoomDto, AuthorizerDto authorizerDto);
    Optional<SuiteRoom> joinRoom();
    void deleteSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto);
    void updateSuiteRoom(ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto, AuthorizerDto authorizerDto);
    Optional<?> commitPaymentStatus();
}
