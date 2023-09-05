package com.suite.suite_suite_room_service.suiteRoom.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResPaymentParticipantDto {
    private String nickName;
    private String email;
    private SuiteStatus status;
    private boolean isHost;
    @Builder
    public ResPaymentParticipantDto(String nickName, String email, SuiteStatus status, boolean isHost) {
        this.nickName = nickName;
        this.email = email;
        this.status = status;
        this.isHost = isHost;
    }
}
