package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResAnpOfMemberDto {
    private Long memberId;
    private Integer point;

    @Builder
    public ResAnpOfMemberDto(Long memberId, Integer point) {
        this.memberId = memberId;
        this.point = point;
    }
}
