package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReqListUpSuiteRoomDto {
    private List<StudyCategory> subjects;
    private String keyword;

    @Builder
    public ReqListUpSuiteRoomDto(List<StudyCategory> subjects, String keyword) {
        this.subjects = subjects;
        this.keyword = keyword;
    }
}
