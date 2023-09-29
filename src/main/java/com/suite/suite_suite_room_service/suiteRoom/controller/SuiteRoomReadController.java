package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomInfoDto;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class SuiteRoomReadController {

    private final SuiteRoomService suiteRoomService;
    @GetMapping("/suiteroom/info/{suiteRoomId}")
    public ResponseEntity<ResSuiteRoomInfoDto> getSuiteRoomInfo(@PathVariable Long suiteRoomId) {
        return ResponseEntity.ok(suiteRoomService.getSuiteRoomInfo(suiteRoomId));
    }
}
