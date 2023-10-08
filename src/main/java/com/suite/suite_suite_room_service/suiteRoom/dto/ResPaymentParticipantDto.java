package com.suite.suite_suite_room_service.suiteRoom.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResPaymentParticipantDto {
    private Long memberId;
    private String nickName;
    private String email;
    private String name;
    private SuiteStatus status;
    private boolean isHost;
    @Builder
    public ResPaymentParticipantDto(Long memberId, String nickName, String email, String name, SuiteStatus status, boolean isHost) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.email = email;
        this.name = name;
        this.status = status;
        this.isHost = isHost;
    }
}
