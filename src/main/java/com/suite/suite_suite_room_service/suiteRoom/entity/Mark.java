package com.suite.suite_suite_room_service.suiteRoom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomListDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mark_id", nullable = false)
    private Long markId;

    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "suite_room_id")
    @JsonBackReference
    private SuiteRoom suiteRoom;
    @Builder
    public Mark(Long markId, Long memberId, SuiteRoom suiteRoom) {
        this.markId = markId;
        this.memberId = memberId;
        this.suiteRoom = suiteRoom;
    }


    public void addSuiteRoom(SuiteRoom suiteRoom) {
        this.suiteRoom = suiteRoom;
    }

    public ResSuiteRoomListDto toResSuiteRoomListDto(Long participantCount, boolean isHost, Participant participant, Long markCount) {
        return ResSuiteRoomListDto.builder()
                .suiteRoomId(suiteRoom.getSuiteRoomId())
                .title(suiteRoom.getTitle())
                .subject(suiteRoom.getSubject())
                .recruitmentDeadline(suiteRoom.getRecruitmentDeadline())
                .createdDate(getSuiteRoom().getCreatedDate())
                .recruitmentLimit(suiteRoom.getRecruitmentLimit())
                .depositAmount(suiteRoom.getDepositAmount())
                .isPublic(suiteRoom.getIsPublic())
                .isOpen(suiteRoom.getIsOpen())
                .participantCount(participantCount)
                .isHost(isHost)
                .hostNickName(participant.getNickname())
                .markCount(markCount)
                .build();
    }
}
