package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class NestedCheckInInfoDto {
    private Long suiteRoomId;
    private Long memberId;
}
