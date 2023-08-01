package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.suite.suite_suite_room_service.suiteRoom.config.ConfigUtil;
import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ReqUpdateSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.dto.ResSuiteRoomDto;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.security.dto.AuthorizerDto;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.suite.suite_suite_room_service.suiteRoom.security.JwtInfoExtractor.getSuiteAuthorizer;


@RequiredArgsConstructor
@RestController
@RequestMapping("/suite")
public class SuiteRoomController {

    private final SuiteRoomService suiteRoomService;
    private final ConfigUtil configUtil;

    @GetMapping("/test")
    public AuthorizerDto test() {
        return getSuiteAuthorizer();
    }

    @GetMapping("/suiteroom")
    public ResponseEntity<Message> listUpRooms() {
        List<ResSuiteRoomDto> getAllSuiteRooms = suiteRoomService.getAllSuiteRooms(getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK, getAllSuiteRooms));
    }
    @GetMapping("/suiteroom/detail/{suiteroomId}")
    public ResponseEntity<Message> detailOfRoom() {
        return null;
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
    @PostMapping("/suiteroom/delete/{suiteRoomId}")
    public ResponseEntity<Message> deleteRoom(@PathVariable Long suiteRoomId) {
        suiteRoomService.deleteSuiteRoom(suiteRoomId, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
    @PatchMapping("/suiteroom/update")
    public ResponseEntity<Message> updateRoom(@RequestBody ReqUpdateSuiteRoomDto reqUpdateSuiteRoomDto) {
        suiteRoomService.updateSuiteRoom(reqUpdateSuiteRoomDto, getSuiteAuthorizer());
        return ResponseEntity.ok(new Message(StatusCode.OK));
    }
    @PostMapping("/payment/completion")
    public ResponseEntity<Message> paymentCompletion() {
        return null;
    }




}
