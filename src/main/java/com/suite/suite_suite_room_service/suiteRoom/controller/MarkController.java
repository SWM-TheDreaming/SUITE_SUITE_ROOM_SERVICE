package com.suite.suite_suite_room_service.suiteRoom.controller;


import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.service.MarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.getSuiteAuthorizer;

@RequiredArgsConstructor
@RestController
@RequestMapping("/suite")
public class MarkController {
    private final MarkService markService;

    @PostMapping("/mark")
    public ResponseEntity<Message> markSuiteRoom(@RequestBody Map<String, Long> suiteRoomId) {
        markService.mark(getSuiteAuthorizer().getMemberId(), suiteRoomId.get("suiteRoomId"));
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @GetMapping("/mark/suiteroom")
    public ResponseEntity<Message> listUpMarkOfSuiteRoom() {

        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
}
