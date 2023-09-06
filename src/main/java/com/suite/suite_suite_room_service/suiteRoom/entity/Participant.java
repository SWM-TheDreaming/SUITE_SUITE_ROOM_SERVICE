package com.suite.suite_suite_room_service.suiteRoom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.suite.suite_suite_room_service.baseTime.BaseTimeEntity;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResPaymentParticipantDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.SuiteStatus;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "participant")
public class Participant extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id", unique = true, nullable = false)
    private Long participantId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "email")
    private String email;

    private String name;

    private String nickname;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SuiteStatus status;

    @Column(name = "is_host")
    private Boolean isHost;

    @ManyToOne
    @JoinColumn(name = "suite_room_id")
    @JsonBackReference
    private SuiteRoom suiteRoom;


    @Builder
    public Participant(AuthorizerDto authorizerDto, SuiteStatus status, Boolean isHost) {
        this.memberId = authorizerDto.getMemberId();
        this.email = authorizerDto.getEmail();
        this.name = authorizerDto.getName();
        this.nickname = authorizerDto.getNickName();
        this.status = status;
        this.isHost = isHost;
    }

    public void addSuiteRoom(SuiteRoom suiteRoom) {
        this.suiteRoom = suiteRoom;
    }
    public void updateStatus(SuiteStatus suiteStatus) {
        this.status = suiteStatus;
    }

    public ResPaymentParticipantDto toResPaymentParticipantDto() {
        return ResPaymentParticipantDto.builder()
                .nickName(this.nickname)
                .email(this.email)
                .status(this.status)
                .isHost(this.isHost)
                .build();
    }
}
