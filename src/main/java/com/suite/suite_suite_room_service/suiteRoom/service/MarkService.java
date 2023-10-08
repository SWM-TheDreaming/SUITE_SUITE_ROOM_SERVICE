package com.suite.suite_suite_room_service.suiteRoom.service;


import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomListDto;

import java.util.List;

public interface MarkService {

    void mark(long memberId, long suiteRoomId);

    List<ResSuiteRoomListDto> getMarkOfSuiteRoom(long memberId);
}
