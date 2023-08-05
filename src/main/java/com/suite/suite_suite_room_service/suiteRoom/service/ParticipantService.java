package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

import java.util.List;

public interface ParticipantService {

    void addParticipant(Long suiteRoomId, AuthorizerDto authorizerDto);
    void removeParticipant(Long suiteRoomId, AuthorizerDto authorizerDto);
    void updatePaymentParticipant(Long suiteRoomId, Long memberId);

}
