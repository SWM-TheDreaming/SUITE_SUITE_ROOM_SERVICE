package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReqListUpSuiteRoomDto {
    private List<StudyCategory> subjects;
    private String keyword;

}
