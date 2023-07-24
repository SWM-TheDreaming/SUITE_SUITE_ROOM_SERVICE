package com.suite.suite_suite_room_service.suiteRoom.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "suite_room")
public class SuiteRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suite_room_id", unique = true, nullable = false)
    private Long suiteRoomId;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "subject")
    private String subject;

    @Column(name = "recruitment_deadline")
    private Timestamp recruitmentDeadline;

    @Column(name = "study_deadline")
    private Timestamp studyDeadline;

    @Column(name = "recruitment_limit")
    private Integer recruitmentLimit;

    @Column(name = "deposit_amount")
    private Integer depositAmount;

    @Column(name = "min_attendance_rate")
    private Integer minAttendanceRate;

    @Column(name = "min_mission_complete_rate")
    private Integer minMissionCompleteRate;

    @Column(name = "is_public")
    private Boolean isPublic;

    @Column(name = "password")
    private Integer password;

    @Column(name = "channel_link")
    private String channelLink;

    @Column(name = "study_method")
    private String studyMethod;

    @Column(name = "study_location")
    private String studyLocation;

    @Column(name = "contract_address")
    private String contractAddress;

    @OneToMany(mappedBy = "suite_room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Participant> participants = new ArrayList<>();

    @Builder
    public SuiteRoom(Long suiteRoomId, String title, String subject, Timestamp recruitmentDeadline, Timestamp studyDeadline, Integer recruitmentLimit, Integer depositAmount, Integer minAttendanceRate, Integer minMissionCompleteRate, Boolean isPublic, Integer password, String channelLink, String studyMethod, String studyLocation, String contractAddress, Long participantId) {
        this.suiteRoomId = suiteRoomId;
        this.title = title;
        this.subject = subject;
        this.recruitmentDeadline = recruitmentDeadline;
        this.studyDeadline = studyDeadline;
        this.recruitmentLimit = recruitmentLimit;
        this.depositAmount = depositAmount;
        this.minAttendanceRate = minAttendanceRate;
        this.minMissionCompleteRate = minMissionCompleteRate;
        this.isPublic = isPublic;
        this.password = password;
        this.channelLink = channelLink;
        this.studyMethod = studyMethod;
        this.studyLocation = studyLocation;
        this.contractAddress = contractAddress;
    }


}
