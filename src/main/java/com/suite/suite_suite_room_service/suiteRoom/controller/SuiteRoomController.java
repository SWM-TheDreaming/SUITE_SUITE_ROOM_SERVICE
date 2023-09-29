package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.suite.suite_suite_room_service.suiteRoom.dto.*;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.security.JwtCreator;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.getSuiteAuthorizer;
import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.renewalTokenValidator;


@RequiredArgsConstructor
@RestController
@RequestMapping("/suite")
public class SuiteRoomController {

    private final SuiteRoomService suiteRoomService;
    private final JwtCreator jwtCreator;

    @PostMapping("/suiteroom")
    public ResponseEntity<Message> listUpSuiteRooms(@RequestBody ReqListUpSuiteRoomDto reqListUpSuiteRoomDto, Pageable pageable) {
        Token renewalToken = renewalTokenValidator() ? jwtCreator.createToken(Objects.requireNonNull(getSuiteAuthorizer())) : null;
        List<ResSuiteRoomListDto> suiteRooms = suiteRoomService.getSuiteRooms(getSuiteAuthorizer(), reqListUpSuiteRoomDto, pageable);

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
        return ResponseEntity.ok(new Message(StatusCode.OK, suiteRoomService.getProgressSuiteRoomList(getSuiteAuthorizer().getMemberId())));
    }
    @GetMapping("/completion")
    public ResponseEntity<Message> listUpCompletionRooms() {
        return ResponseEntity.ok(new Message(StatusCode.OK, suiteRoomService.getCompletionSuiteRoomList(getSuiteAuthorizer().getMemberId())));
    }

    @PostMapping("/suiteroom/registration")
    public ResponseEntity<Message> createRoom(@RequestBody ReqSuiteRoomCreationDto reqSuiteRoomCreationDto) {
        return ResponseEntity.ok(new Message(StatusCode.OK, suiteRoomService.createSuiteRoom(reqSuiteRoomCreationDto, getSuiteAuthorizer())));
    }

    @PostMapping("/suiteroom/validate/password")
    public ResponseEntity<Message> validateSuiteRoomPassword(@RequestBody Map<String, Object> suiteRoomPassword) {
        suiteRoomService.validatePassword(Long.parseLong(suiteRoomPassword.get("suiteRoomId").toString()), (int) suiteRoomPassword.get("password"));
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }

    @PostMapping("/suiteroom/validate/title")
    public ResponseEntity<Message> validateSuiteRoomTitle(@RequestBody Map<String, String> suiteRoomTitle) {
        suiteRoomService.validateTitle(suiteRoomTitle.get("title"));
        return ResponseEntity.ok(new Message(StatusCode.OK));
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
