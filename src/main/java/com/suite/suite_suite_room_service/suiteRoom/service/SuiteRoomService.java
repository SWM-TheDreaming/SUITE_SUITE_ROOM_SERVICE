package com.suite.suite_suite_room_service.suiteRoom.service;

import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SuiteRoomService {
    List<ResSuiteRoomListDto> getSuiteRooms(AuthorizerDto authorizerDto, ReqListUpSuiteRoomDto reqListUpSuiteRoomDto, Pageable pageable);
    ResSuiteRoomDto getSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto);
    void validateTitle(String title);
    void validatePassword(Long suiteRoomId, int password);
    List<ResSuiteRoomListDto> getProgressSuiteRoomList(Long memberId);
    List<ResSuiteRoomListDto> getCompletionSuiteRoomList(Long memberId);
    ResSuiteRoomCreationDto createSuiteRoom(ReqSuiteRoomCreationDto reqSuiteRoomCreationDto, AuthorizerDto authorizerDto);
    void deleteSuiteRoom(Long suiteRoomId, AuthorizerDto authorizerDto);
    void updateSuiteRoom(ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto, AuthorizerDto authorizerDto);
    ResSuiteRoomInfoDto getSuiteRoomInfo(Long suiteRoomId);
    List<ResSuiteRoomListDto> getHonorOfSuiteRooms(Long memberId);

    int getPoint(Long memberId);
    boolean getSuiteRoomStartStatus(Long suiteRoomId);
    ResBeforeStudyDashboard getBeforeStudyDashboard(Long suiteRoomId);
}
