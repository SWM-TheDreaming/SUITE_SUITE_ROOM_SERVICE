package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.security.JwtCreator;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.getSuiteAuthorizer;
import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.renewalTokenValidator;


@RequiredArgsConstructor
@RestController
@RequestMapping("/suite")
public class SuiteRoomController {

    private final SuiteRoomService suiteRoomService;
    private final JwtCreator jwtCreator;

    @GetMapping("/suiteroom")
    public ResponseEntity<Message> listUpSuiteRooms(@RequestBody List<StudyCategory> subjects, Pageable pageable) {
        Token renewalToken = renewalTokenValidator() ? jwtCreator.createToken(Objects.requireNonNull(getSuiteAuthorizer())) : null;
        List<ResSuiteRoomListDto> suiteRooms = suiteRoomService.getSuiteRooms(getSuiteAuthorizer(), subjects, pageable);

        Message.MessageAppender message = new Message.MessageAppender();

        return ResponseEntity.ok(message.messageAppenderCaller("token", renewalToken,StatusCode.OK, suiteRooms));
    }
    @GetMapping("/suiteroom/detail/{suiteRoomId}")
    public ResponseEntity<Message> listUpSuiteRoom(@PathVariable Long suiteRoomId) {
        ResSuiteRoomDto getSuiteRoom = suiteRoomService.getSuiteRoom(suiteRoomId, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK, getSuiteRoom));
    }
    @GetMapping("/progression")
    public ResponseEntity<Message> listUpProgressionRooms() {
        return null;
    }
    @GetMapping("/completion")
    public ResponseEntity<Message> listUpCompletionRooms() {
        return null;
    }
    @PostMapping("/suiteroom/registration")
    public ResponseEntity<Message> createRoom(@RequestBody ReqSuiteRoomDto reqSuiteRoomDto) {
        suiteRoomService.createSuiteRoom(reqSuiteRoomDto, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
    @PostMapping("/suiteroom/attendance")
    public ResponseEntity<Message> attendanceRoom() {
        return null;
    }
    @DeleteMapping("/suiteroom/delete/{suiteRoomId}")
    public ResponseEntity<Message> deleteRoom(@PathVariable Long suiteRoomId) {
        suiteRoomService.deleteSuiteRoom(suiteRoomId, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
    @PatchMapping("/suiteroom/update")
    public ResponseEntity<Message> updateRoom(@RequestBody ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto) {
        suiteRoomService.updateSuiteRoom(reqUpdateSuiteRoomDto, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

}
