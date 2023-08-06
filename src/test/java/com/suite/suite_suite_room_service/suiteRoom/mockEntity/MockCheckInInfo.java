package com.suite.suite_suite_room_service.suiteRoom.mockEntity;


public class MockCheckInInfo {
    private Long suiteRoomId;
    private Long memberId;

    public MockCheckInInfo(Long suiteRoomId, Long memberId) {
        this.suiteRoomId = suiteRoomId;
        this.memberId = memberId;
    }

    public Long getSuiteRoomId() {
        return suiteRoomId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
