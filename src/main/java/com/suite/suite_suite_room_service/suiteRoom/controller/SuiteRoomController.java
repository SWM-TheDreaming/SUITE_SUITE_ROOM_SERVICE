package com.suite.suite_suite_room_service.suiteRoom.controller;

import com.suite.suite_suite_room_service.suiteRoom.config.ConfigUtil;
import com.suite.suite_suite_room_service.suiteRoom.dto.Message;
import com.suite.suite_suite_room_service.suiteRoom.handler.CustomException;
import com.suite.suite_suite_room_service.suiteRoom.handler.StatusCode;
import com.suite.suite_suite_room_service.suiteRoom.service.SuiteRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/suite")
public class SuiteRoomController {

    private final SuiteRoomService suiteRoomService;
    private final ConfigUtil configUtil;
    @GetMapping("/suiteroom")
    public ResponseEntity<Message> listUpRooms() {
        return null;
    }
    @GetMapping("/suiteroom/detail/{suiteroodId}")
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
    public ResponseEntity<Message> createRoom() {
        return null;
    }
    @PostMapping("/suiteroom/attendance")
    public ResponseEntity<Message> attendanceRoom() {
        return null;
    }
    @PostMapping("/suiteroom/delete")
    public ResponseEntity<Message> deleteRoom() {
        return null;
    }
    @PostMapping("/suiteroom/update")
    public ResponseEntity<Message> updateRoom() {
        return null;
    }
    @PostMapping("/payment/completion")
    public ResponseEntity<Message> paymentCompletion() {
        return null;
    }




}
