package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.ResPaymentParticipantDto;
import com.suite.suite_suite_room_service.suiteRoom.entity.Participant;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;

import java.util.List;

public interface ParticipantService {

    void addParticipant(Long suiteRoomId, AuthorizerDto authorizerDto);
    void removeParticipant(Long suiteRoomId, AuthorizerDto authorizerDto);
    public void updatePaymentParticipant(Long suiteRoomId, AuthorizerDto authorizerDto);

    List<ResPaymentParticipantDto> listUpPaymentParticipants(Long suiteRoomId);

    List<ResPaymentParticipantDto> listUpNotYetPaymentParticipants(Long suiteRoomId);

    void updateParticipantsStatusReadyToStart(Long suiteRoomId, Long memberId);

}
