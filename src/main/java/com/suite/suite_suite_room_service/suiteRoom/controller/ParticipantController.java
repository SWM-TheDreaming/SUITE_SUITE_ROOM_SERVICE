package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.getSuiteAuthorizer;

@RequiredArgsConstructor
@RestController
@RequestMapping("/suite")
public class ParticipantController {
    private final ParticipantService participantService;

    @PostMapping("/suiteroom/attend")
    public ResponseEntity<Message> joinSuiteRoom(@RequestBody Map<String, Long> suiteRoomId) {
        participantService.addParticipant(suiteRoomId.get("suiteRoomId"), getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @PostMapping("/suiteroom/attend/cancel")
    public ResponseEntity<Message> cancelSuiteRoom(@RequestBody Map<String, Long> suiteRoomId) {
        participantService.removeParticipant(suiteRoomId.get("suiteRoomId"), getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
}
