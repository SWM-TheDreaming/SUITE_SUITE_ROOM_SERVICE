package com.suite.suite_suite_room_service.suiteRoom.entity;

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

    @Column(name = "suite_room_id")
    private Long suiteRoomId;

    @Builder
    public Mark(Long markId, Long memberId, Long suiteRoomId) {
        this.markId = markId;
        this.memberId = memberId;
        this.suiteRoomId = suiteRoomId;
    }

}
