package com.suite.suite_suite_room_service.suiteRoom.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.suite.suite_suite_room_service.baseTime.BaseTimeEntity;
import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
@Table(name = "suite_room")
public class SuiteRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suite_room_id", unique = true, nullable = false)
    private Long suiteRoomId;

    @Column(name = "title", unique = true)
    private String title;

    @Column(name = "content", columnDefinition= "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "subject")
    private StudyCategory subject;

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

    @Column(name = "is_open")
    private Boolean isOpen;

    @Column(name = "is_start")
    private Boolean isStart;

    @Column(name = "channel_link")
    private String channelLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_method")
    private StudyType studyMethod;

    @Column(name = "contract_address")
    private String contractAddress;

    @Column(name = "study_start_date")
    private Timestamp studyStartDate;

    @Column(name = "honor_point")
    private Double honorPoint;

    @OneToMany(mappedBy = "suiteRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "suiteRoom", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Mark> marks = new ArrayList<>();

    @Builder
    public SuiteRoom(Long suiteRoomId, String title, String content, StudyCategory subject, Timestamp recruitmentDeadline, Timestamp studyDeadline, Integer recruitmentLimit, Integer depositAmount, Integer minAttendanceRate, Integer minMissionCompleteRate, Boolean isPublic, Integer password, Boolean isOpen, Boolean isStart, String channelLink, StudyType studyMethod, String contractAddress, Timestamp studyStartDate, Double honorPoint) {
        this.suiteRoomId = suiteRoomId;
        this.title = title;
        this.content = content;
        this.subject = subject;
        this.recruitmentDeadline = recruitmentDeadline;
        this.studyDeadline = studyDeadline;
        this.recruitmentLimit = recruitmentLimit;
        this.depositAmount = depositAmount;
        this.minAttendanceRate = minAttendanceRate;
        this.minMissionCompleteRate = minMissionCompleteRate;
        this.isPublic = isPublic;
        this.password = password;
        this.isOpen = isOpen;
        this.isStart = isStart;
        this.channelLink = channelLink;
        this.studyMethod = studyMethod;
        this.contractAddress = contractAddress;
        this.studyStartDate = studyStartDate;
        this.honorPoint = honorPoint;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
        participant.addSuiteRoom(this);
    }

    public void addMark(Mark mark) {
        this.marks.add(mark);
        mark.addSuiteRoom(this);
    }

    public void setHonorPoint(Double honorPoint) {
        this.honorPoint = honorPoint;
    }

    public void updateSuiteRoom(ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto) {
        this.content = reqUpdateSuiteRoomDto.getContent();
        this.channelLink = reqUpdateSuiteRoomDto.getChannelLink();
    }

    public void openSuiteRoom() {
        this.isOpen = true;
    }

    public void startSuiteRoom() {
        this.isStart = true;
        this.studyStartDate = new Timestamp(System.currentTimeMillis());
    }

    public void startErrorSuiteRoom() { this.isStart = false; }


    public ResSuiteRoomListDto toResSuiteRoomListDto(Long participantCount, boolean isHost, Participant participant, Long markCount) {
        return ResSuiteRoomListDto.builder()
                .suiteRoomId(suiteRoomId)
                .title(this.title)
                .subject(this.subject)
                .recruitmentDeadline(this.recruitmentDeadline)
                .createdDate(this.getCreatedDate())
                .recruitmentLimit(this.recruitmentLimit)
                .depositAmount(this.depositAmount)
                .isPublic(this.isPublic)
                .isOpen(this.isOpen)
                .participantCount(participantCount)
                .isHost(isHost)
                .hostNickName(participant.getNickname())
                .markCount(markCount)
                .build();
    }

    public ResSuiteRoomDto toResSuiteRoomDto(Long participantCount, boolean isHost, Long markCount, boolean isMark) {
        return ResSuiteRoomDto.builder()
                .suiteRoomId(suiteRoomId)
                .title(this.title)
                .content(this.content)
                .subject(this.subject)
                .recruitmentDeadline(this.recruitmentDeadline)
                .studyDeadline(this.studyDeadline)
                .recruitmentLimit(this.recruitmentLimit)
                .depositAmount(this.depositAmount)
                .minAttendanceRate(this.minAttendanceRate)
                .minMissionCompleteRate(this.minMissionCompleteRate)
                .isPublic(this.isPublic)
                .isOpen(this.isOpen)
                .channelLink(this.channelLink)
                .studyMethod(this.studyMethod)
                .contractAddress(this.contractAddress)
                .participantCount(participantCount)
                .isHost(isHost)
                .markCount(markCount)
                .isMark(isMark)
                .build();
    }

    public ResSuiteRoomDto toResSuiteRoomDto(Long participantCount, boolean isHost) {
        return ResSuiteRoomDto.builder()
                .title(this.title)
                .content(this.content)
                .subject(this.subject)
                .recruitmentDeadline(this.recruitmentDeadline)
                .studyDeadline(this.studyDeadline)
                .recruitmentLimit(this.recruitmentLimit)
                .depositAmount(this.depositAmount)
                .minAttendanceRate(this.minAttendanceRate)
                .minMissionCompleteRate(this.minMissionCompleteRate)
                .isPublic(this.isPublic)
                .isOpen(this.isOpen)
                .channelLink(this.channelLink)
                .studyMethod(this.studyMethod)
                .contractAddress(this.contractAddress)
                .participantCount(participantCount)
                .isHost(isHost)
                .createdAt(this.getCreatedDate())
                .build();
    }

    public ResBeforeStudyDashboard toResBeforeStudyDashboard(List<ParticipantDto> participantDtoList) {
        return ResBeforeStudyDashboard.builder()
                .suiteRoomId(suiteRoomId)
                .isStart(isStart)
                .studyStartDate(studyStartDate)
                .studyDeadline(studyDeadline)
                .title(title)
                .participantDtoList(participantDtoList).build();
    }

}
