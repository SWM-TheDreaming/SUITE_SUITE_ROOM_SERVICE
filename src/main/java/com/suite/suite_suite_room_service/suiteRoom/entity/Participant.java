package com.suite.suite_suite_room_service.suiteRoom.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "participant")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id", unique = true, nullable = false)
    private Long participantId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "status")
    private String status;

    @Column(name = "is_host")
    private Boolean isHost;

//    @ManyToOne
//    @JoinColumn(name = "suite_room_id")
//    @JsonBackReference
//    private SuiteRoom suiteRoom;




}
